package com.sjy.table.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.sjy.dict.service.DictService;
import com.sjy.exception.CrmException;
import com.sjy.table.config.Dimension;
import com.sjy.table.config.PageResult;
import com.sjy.table.config.SqlColumn;
import com.sjy.table.config.SqlConfig;
import com.sjy.table.config.SqlOpt;
import com.sjy.table.config.SqlQuery;
import com.sjy.table.config.TableMeta;
import com.sjy.table.util.FormatUtil;
import com.sjy.table.util.QueryUtil;
import com.sjy.util.DateUtils;
import com.sjy.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("dataQueryService")
public class DataQueryService {

	@Resource
	SessionFactory sessionFactory;

	@Resource
	DataSource dataSource;

	@Resource
	DictService dictService;

	private int MAXCOUNT = 10000; // 查询时返回最多的count

	SqlConfig config = new SqlConfig();

	public static final String DB_DATEFORMAT = "yyyyMMddHHmmss";

	public static ThreadLocal<Map<String, Integer>> columIndexMapLocal = new ThreadLocal<Map<String, Integer>>();
	@SuppressWarnings("rawtypes")
	public static ThreadLocal<Map> paramLocal = new ThreadLocal<Map>();
	public static ThreadLocal<Object> rowLocal = new ThreadLocal<Object>();
	public static ThreadLocal<Object> rowNewLocal = new ThreadLocal<Object>();
	public static ThreadLocal<Object> valueLocal = new ThreadLocal<Object>();
	public static ThreadLocal<PageResult> pageResultLocal = new ThreadLocal<PageResult>();

	private boolean isOracle = false;
	private boolean isMySql = false;

	public DataQueryService(Environment env) {
		String dialect = env.getProperty("spring.jpa.database", "MYSQL");
		MAXCOUNT = Integer.parseInt(env.getProperty("application.list.maxcount", "50000"));
		checkSqlFiles();
		isOracle = dialect.equalsIgnoreCase("oracle");
		isMySql = dialect.equalsIgnoreCase("mysql");
	}

	public SqlQuery getQuery(String queryName) {
		return config.getQuery(queryName);
	}

	public Dimension getSharedDimension(String dimName) {
		return config.getSharedDimension(dimName);
	}

	public void appendField(StringBuffer sb, SqlQuery query, Map<String, Object> params) {
		if (query.fieldStr != null) {
			sb.append("select ");
			sb.append(query.fieldStr);
			sb.append(' ');
		}
	}

	public void appendStatment(StringBuffer sb, SqlQuery query, Map<String, Object> params) {
		String stmt = query.stmt;
		if (query.dynaStmt != null) {
			try {
				stmt = FormatUtil.replaceAll(query.stmt, params);// query.dynaStmt.run().toString();
			} catch (Exception e) {
				throw new CrmException("解析动态sql出错:" + query.stmt, e);
			}
		}

		sb.append(stmt);

		boolean hasWhere = stmt.lastIndexOf(" where ") >= 0;
		appendCond(sb, query, params, hasWhere);
	}

	public boolean appendCond(StringBuffer sb, SqlQuery query, Map<String, Object> params, boolean hasWhere) {
		String value;
		String cond;
		Object v;
		for (SqlOpt opt : query.options) {
			v = params.get(opt.name);
			value = v == null ? null : v.toString();
			if (value != null && (opt.dynaCond != null || opt.cond != null)) {
				if (!hasWhere) {
					sb.append(" where 1=1");
					hasWhere = true;
				}
				if (opt.dynaCond) {
					try {
						// cond = opt.dynaCond.run().toString();
						cond = FormatUtil.replaceAll(opt.cond, params);
					} catch (Exception e) {
						throw new CrmException("解析动态sql出错:" + opt.cond, e);
					}
				} else {
					cond = opt.cond;
				}

				if (cond != null) {
					sb.append(" and ");
					log.debug("{}-{}", cond, parseHardValue(value, opt.type));
					cond = cond.replaceAll("\\?", "" + parseHardValue(value, opt.type));
					sb.append(cond);
				}
			}
		}
		return hasWhere;
	}

	public void appendOrderBy(StringBuffer sb, SqlQuery query, String orderBy) {
		if (orderBy != null) {
			orderBy = orderBy.trim();
			int i = orderBy.indexOf('.');
			String token = orderBy;
			if (i > 0)
				token = orderBy.substring(0, i);
			else {
				i = orderBy.indexOf(' ');
				if (i > 0)
					token = orderBy.substring(0, i);
			}
			try {
				int j = Integer.parseInt(token);
				if (query.columns != null && query.columns.size() > j && query.columns.get(j).field != null) {
					sb.append(" order by ");
					sb.append(query.columns.get(j).field);
					if (i > 0)
						sb.append(orderBy.substring(i));

				} else
					log.warn("not support order by :" + query.stmt + "," + orderBy);
			} catch (NumberFormatException nfe) {
				log.warn("not support order by :" + query.stmt + "," + orderBy);
			}
		} else if (query.orderby != null) {
			sb.append(" order by ");
			sb.append(query.orderby);
		}

	}

	/**
	 * 返回分页数据
	 * 
	 * @param stmt
	 * @param params
	 * @param start
	 *            起始位置
	 * @param maxResult
	 *            数量
	 * @return
	 */
	public PageResult selectData(String queryName, Map<String, Object> params, String orderBy, int start, int maxResult,
			String showFields) {
		return internalSelectData(queryName, params, orderBy, start, maxResult, showFields, false);
	}

	private String compileCountStmt(SqlQuery query, String fromStmt) {
		StringBuffer sb = new StringBuffer();
		sb.append("select 1 ");
		sb.append(fromStmt);
		String countStmt = sb.toString();

		countStmt = query.isNative ? countStmt : QueryUtil.compileSql(sessionFactory, countStmt).getSQLString();
		sb = new StringBuffer();
		sb.append("select count(*) from (");

		sb.append("select ");
		sb.append(QueryUtil.getHint(query, isOracle ? "first_rows" : null));

		sb.append(" 1 ");
		countStmt = countStmt.substring("select 1 ".length());

		sb.append(countStmt);

		if (isOracle) {
			// 不允许超过5000
			if (countStmt.lastIndexOf(" where ") >= 0)
				sb.append(" and rownum<");
			else
				sb.append(" where rownum<");
			sb.append(MAXCOUNT + 1);
		} else if (isMySql) {
			sb.append(" limit ");
			sb.append(MAXCOUNT);
		}
		sb.append(") x");
		return sb.toString();
	}

	private String compileStatStmt(String stat, String fromStmt, String orderStmt, boolean isNative) {
		StringBuffer sb = new StringBuffer();
		sb.append("select ");
		sb.append(stat);
		sb.append(' ');
		sb.append(fromStmt);

		String stmt = sb.toString();
		stmt = isNative ? stmt : QueryUtil.compileSql(sessionFactory, stmt).getSQLString();
		return stmt;
	}

	private Object[] compileStmt(SqlQuery query, String fromStmt, String orderStmt, Map<String, Object> params,
			PageResult pr) {
		StringBuffer sb = new StringBuffer();
		appendField(sb, query, params);
		sb.append(fromStmt);
		sb.append(orderStmt);
		String stmt = sb.toString();
		QueryTranslatorImpl translator = null;
		if (!query.isNative) {
			translator = QueryUtil.compileSql(sessionFactory, stmt);
			stmt = translator.getSQLString();
		}
		sb = new StringBuffer();
		int start = pr.start;
		int maxResult = pr.maxResult;
		if (isOracle) {
			if (start > 0)
				sb.append("select * from (");
			sb.append("select ");
			sb.append(QueryUtil.getHint(query, null));
			sb.append("x.*, rownum as rownum_ from (");
			sb.append(stmt);
			sb.append(")x");
			if (pr.totalElements > 1000) { // 如果值很少，则不加rownum上限
				sb.append(" where rownum<");
				sb.append(start + maxResult + 1);
			}
			if (start > 0) {
				sb.append(") where rownum_>");
				sb.append(start);
			}
			stmt = sb.toString();
		} else if (isMySql) {
			sb.append(stmt);
			sb.append(" limit ");
			sb.append(start);
			sb.append(",");
			sb.append(maxResult);
			stmt = sb.toString();
		}
		return new Object[] { stmt, translator };
	}

	/**
	 * 返回分页数据
	 * 
	 * @param stmt
	 * @param params
	 * @param start
	 *            起始位置
	 * @param maxResult
	 *            数量
	 * @return
	 */
	@SuppressWarnings({ "unchecked" })
	private PageResult internalSelectData(String queryName, Map<String, Object> params, String orderBy, int start,
			int maxResult, String showFields, boolean export) {
		PageResult pr = new PageResult();
		paramLocal.set(params);
		pageResultLocal.set(pr);
		try {
			if (params == null)
				params = Collections.EMPTY_MAP;
			SqlQuery query = getQuery(queryName);
			if (query == null)
				throw new CrmException("Unknown query " + queryName);

			columIndexMapLocal.set(query.columnIndexMap);

			if (start < 0)
				start = 0;
			if (maxResult < 1) {
				maxResult = 20;
			}
			pr.start = start;
			pr.maxResult = maxResult;
			List<Object[]> results = null;
			if (query.stmt != null) {
				StringBuffer sb = new StringBuffer();
				appendStatment(sb, query, params);
				String fromStmt = sb.toString();

				sb = new StringBuffer();
				appendOrderBy(sb, query, orderBy);
				String orderStmt = sb.toString();

				Connection conn = null;
				ResultSet rs = null, rs1 = null;
				Statement st = null, st1 = null;
				try {
					conn = dataSource.getConnection();

					// 1 算总数
					st1 = conn.createStatement();
					String countStmt = compileCountStmt(query, fromStmt);
					log.debug("countStmt : {}", countStmt);
					rs1 = st1.executeQuery(countStmt);
					if (rs1.next()) {
						pr.totalElements = ((Number) rs1.getObject(1)).intValue();
					}

					if (pr.totalElements > 0) {
						// 2. total>=5000,算统计值
						if (query.stat != null && pr.totalElements < MAXCOUNT) {
							rs1.close();
							String statStmt = compileStatStmt(query.stat, fromStmt, orderStmt, query.isNative);
							log.debug("statStmt : ");
							log.debug(statStmt);
							rs1 = st1.executeQuery(statStmt);
							if (rs1.next()) {
								int statC = query.stat.split(",").length + 1;
								Object[] stats = new Object[statC];
								stats[0] = pr.totalElements;
								for (int k = 1; k < stats.length; k++) {
									stats[k] = rs1.getObject(k);
									if (stats[k] == null)
										stats[k] = 0;
								}
								pr.stats = stats;
							}
						}

						// 2.查明细
						Object[] ss = compileStmt(query, fromStmt, orderStmt, params, pr);
						String stmt = (String) ss[0];
						log.debug("stmt : {}", stmt);
						QueryTranslatorImpl translator = (QueryTranslatorImpl) ss[1];
						if (isOracle || isMySql) {
							st = conn.createStatement();
							rs = st.executeQuery(stmt);
							results = QueryUtil.parseSqlResult(sessionFactory, rs, query.columns.size(), maxResult,
									translator);
						}
					}

				} catch (SQLException sq) {
					throw new CrmException(sq);
				} catch (Exception ce) {
					throw new CrmException(ce);
				} finally {
					if (rs != null)
						try {
							rs.close();
						} catch (Exception e) {
						}

					if (rs1 != null)
						try {
							rs1.close();
						} catch (Exception e) {
						}

					if (st != null)
						try {
							st.close();
						} catch (Exception e) {
						}

					if (st1 != null)
						try {
							st1.close();
						} catch (Exception e) {
						}
					if (conn != null)
						try {
							conn.close();
						} catch (Exception e) {
						}
				}
			}
			if (results == null)
				results = Collections.EMPTY_LIST;

			toColumnValues(pr, query, results, showFields, export);
			pr.title = QueryUtil.getTitle(query, pr.locale);
			if (query.dynaStatTitle != null && pr.stats != null) {
				// try {
				// pr.statTitle = query.dynaStatTitle.run().toString();
				// } catch (Exception e) { // not throw
				// throw new CrmException(
				// "解析动态statTitle出错:" + query.statTitle, e);
				// }
			}
			if (query.dynaStatOper != null) {
				// try {
				// pr.statOper = query.dynaStatOper.run().toString();
				// } catch (Exception e) { // not throw
				// throw new CrmException("解析动态statOper出错:" + query.statTitle,
				// e);
				// }
			}
			if (query.dynaStatDateTime != null) {
				// try {
				// pr.statDateTime = query.dynaStatDateTime.run().toString();
				// } catch (Exception e) { // not throw
				// throw new CrmException("解析动态statDateTime出错:"
				// + query.statTitle, e);
				// }
			}
			pr.template = query.template;
			pr.oversize = pr.totalElements == MAXCOUNT;
			pr.total = pr.totalElements;
			return pr;
		} finally {
			columIndexMapLocal.set(null);
			paramLocal.set(null);
			pageResultLocal.set(null);
		}
	}

	private void toColumnValues(PageResult pr, SqlQuery query, List<Object[]> results, String showFields,
			boolean export) {
		if (query.columns == null || query.columns.size() == 0)
			return;

		List<Map<String, Object>> contents = new ArrayList<Map<String, Object>>(results.size());

		List<Object[]> columnValues = new ArrayList<Object[]>(results.size());
		Object[] rowData = null;
		List<SqlColumn> columns = null;

		List<Integer> dataColIndex = new ArrayList<Integer>();

		if (showFields == null) {
			columns = new ArrayList<SqlColumn>(query.columns.size());
			SqlColumn column = null;
			for (int i = 0; i < query.columns.size(); i++) {
				column = query.columns.get(i);
				if (!export || !column.hidden) {
					columns.add(column);
					dataColIndex.add(i);
				}
			}
		} else {
			// 输出指定列
			StringTokenizer st = new StringTokenizer(showFields);
			List<Integer> fList = new ArrayList<Integer>(st.countTokens());
			while (st.hasMoreTokens()) {
				String s = st.nextToken();
				try {
					int f = Integer.parseInt(s);
					if (f >= 0 && f < query.columns.size())
						fList.add(f);
				} catch (Exception e) {

				}
			}

			columns = new ArrayList<SqlColumn>(fList.size());
			SqlColumn column = null;
			for (int i = 0; i < fList.size(); i++) {
				column = query.columns.get(fList.get(i));
				if (!export || !column.hidden) {
					columns.add(column);
					dataColIndex.add(i);
				}
			}
		}
		Integer[] dataColIndexArray = dataColIndex.toArray(new Integer[0]);
		try {
			for (Object result : results) {
				rowData = new Object[columns.size()];

				rowLocal.set(result);
				rowNewLocal.set(rowData);

				Map<String, Object> content = new HashMap<String, Object>();

				SqlColumn column = null;
				for (int i = 0; i < columns.size(); i++) {
					column = columns.get(i);
					if (result instanceof Object[]) {
						if (((Object[]) result).length > dataColIndexArray[i])
							rowData[i] = ((Object[]) result)[dataColIndexArray[i]];
					} else if (dataColIndexArray[i] == 0) {
						rowData[i] = result;
					}
					if (column.value != null)
						try {
							valueLocal.set(rowData[i]);
							// rowData[i] = column.dynaValue.run();
						} catch (Exception e) {
							throw new CrmException("parse column value error:" + column.value, e);
						}
					if (rowData[i] != null) {
						if (rowData[i] instanceof String) {
							rowData[i] = ((String) rowData[i]).trim();
						} else if (rowData[i] instanceof Boolean) {
							rowData[i] = ((Boolean) rowData[i]) ? "是" : "否";
						}
						if (column.dict != null) {
							try {
								Integer dictValue = (Integer) (rowData[i] == null ? 0
										: (rowData[i] instanceof Integer ? rowData[i]
												: Integer.parseInt(rowData[i] + "")));
								rowData[i] = dictService.getText(column.dict, dictValue);
								// log.info("{}-{}-{}", column.dict, dictValue, rowData[i]);
							} catch (Exception e) {
								throw new CrmException("parse column value error:" + column.value, e);
							}
						}
						if (column.format != null) {
							rowData[i] = DateUtils.format(rowData[i], column.format);
						}
					}

					String key = column.getName();
					if (StringUtil.isBlank(key)) {
						key = column.getField().split("\\.")[1];
					}
					content.put(key, rowData[i]);

				}
				columnValues.add(rowData);

				contents.add(content);
			}
		} finally {
			rowLocal.set(null);
			rowNewLocal.set(null);
			valueLocal.set(null);
		}
		pr.columns = columns;
		pr.objects = columnValues;
		pr.content = contents;
	}

	@Scheduled(cron = "0/2 * * * * ? ")
	public void checkSqlFiles() {
		config.checkSqlFiles();
	}

	public TableMeta getTableMeta(String queryName, Map<String, Object> params) {
		paramLocal.set(params);
		try {
			SqlQuery query = getQuery(queryName);
			if (query == null)
				throw new CrmException("Unknown query " + queryName);
			TableMeta meta = new TableMeta();
			meta.setTitle(QueryUtil.getTitle(query, (String) params.get("_c_locale")));

			List<SqlColumn> columns = new ArrayList<SqlColumn>(query.columns.size());
			for (SqlColumn column : query.columns) {
				column.setField(column.name);
				if (column.align == null) {
					column.setAlign("center");
				}
				if (!column.hidden) {
					columns.add(column.clone());
				}
			}

			meta.setColumns(columns);
			return meta;
		} finally {
			paramLocal.set(null);
		}
	}

	private void checkInject(String value) {
		if (value.indexOf(" or ") >= 0 || value.indexOf(")or(") >= 0)
			throw new CrmException("dubious parameter: " + value);
	}

	public Object parseHardValue(String value, String type) {
		Object trueValue = null;
		if (value == null)
			throw new CrmException("value cant be null");

		if (type == null || type.equals(SqlConfig.STRING)) {
			checkInject(value);
			trueValue = "'" + value + "'";
		} else if (type.equals(SqlConfig.INTEGER)) {
			trueValue = Integer.parseInt(value);
		} else if (type.equals(SqlConfig.DATE)) {
			if (isOracle)
				trueValue = "to_date('" + DateUtils.format(FormatUtil.parseDate(value), DB_DATEFORMAT)
						+ "','yyyymmddhh24miss')";
			else
				trueValue = "'" + value + "'";
		} else if (type.equals(SqlConfig.ENDDATE)) {
			if (isOracle)
				trueValue = "to_date('" + DateUtils.format(FormatUtil.parseDateEnd(value), DB_DATEFORMAT)
						+ "','yyyymmddhh24miss')";
			else
				trueValue = "'" + FormatUtil.formatDate(FormatUtil.parseDateEnd(value)) + "'";

		} else if (type.equals(SqlConfig.LONG)) {
			trueValue = Long.parseLong(value);
		} else if (type.equals(SqlConfig.BOOLEAN)) {
			trueValue = value.equals("true");
		} else if (type.equals(SqlConfig.FLOAT)) {
			trueValue = Float.parseFloat(value);
		} else if (type.equals(SqlConfig.DOUBLE)) {
			trueValue = Double.parseDouble(value);
		} else if (type.equals(SqlConfig.LIKE)) {
			checkInject(value);
			trueValue = value;
			if (!value.startsWith("%"))
				trueValue = "%" + trueValue;
			if (!value.endsWith("%"))
				trueValue = trueValue + "%";
			trueValue = "'" + trueValue + "'";
		} else if (type.equals(SqlConfig.LEFTLIKE)) {
			checkInject(value);
			trueValue = value;
			if (!value.endsWith("%"))
				trueValue = trueValue + "%";
			trueValue = "'" + trueValue + "'";
		} else if (type.equals(SqlConfig.RIGHTLIKE)) {
			checkInject(value);
			trueValue = value;
			if (!value.startsWith("%"))
				trueValue = "%" + trueValue;
			trueValue = "'" + trueValue + "'";
		}
		return trueValue;
	}

	public Object parseValue(String value, String type) {
		Object trueValue = null;
		if (value == null || value.length() == 0) {
			// replace an empty string with null value
			trueValue = null;
		} else if (type == null || type.equals(SqlConfig.STRING)) {
			trueValue = value;
		} else if (type.equals(SqlConfig.INTEGER)) {
			trueValue = Integer.parseInt(value);
		} else if (type.equals(SqlConfig.DATE)) {
			trueValue = FormatUtil.parseDate(value);
		} else if (type.equals(SqlConfig.ENDDATE)) {
			trueValue = FormatUtil.parseDateEnd(value);
		} else if (type.equals(SqlConfig.LONG)) {
			trueValue = Long.parseLong(value);
		} else if (type.equals(SqlConfig.BOOLEAN)) {
			trueValue = value.equals("true") ? Boolean.TRUE : Boolean.FALSE;
		} else if (type.equals(SqlConfig.FLOAT)) {
			trueValue = Float.parseFloat(value);
		} else if (type.equals(SqlConfig.DOUBLE)) {
			trueValue = Double.parseDouble(value);
		} else if (type.equals(SqlConfig.LIKE)) {
			trueValue = value;
			if (!value.startsWith("%"))
				trueValue = "%" + trueValue;
			if (!value.endsWith("%"))
				trueValue = trueValue + "%";
		}
		return trueValue;
	}
}
