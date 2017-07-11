package com.sjy.table.config;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.core.io.Resource;

import com.sjy.exception.CrmException;
import com.sjy.util.ResourceUtil;

@Slf4j
public class SqlConfig {

	public static final String STRING = "String";
	public static final String BOOLEAN = "Boolean";
	public static final String INTEGER = "Integer";
	public static final String LONG = "Long";
	public static final String DOUBLE = "Double";
	public static final String FLOAT = "Float";
	public static final String DATE = "Date";
	public static final String ENDDATE = "EndDate";
	public static final String LIKE = "Like";
	public static final String LEFTLIKE = "LeftLike";
	public static final String RIGHTLIKE = "RightLike";

	Map<String, SqlQuery> querys = new HashMap<String, SqlQuery>();
	Map<String, Dimension> sharedDimensions = new HashMap<String, Dimension>();
	Map<String, Long> fileUpdates = new HashMap<String, Long>();
	Resource[] sqlFiles;

	SAXReader saxReader = new SAXReader();

	public SqlConfig() {
		sqlFiles = ResourceUtil.getFiles("classpath*:sql/*.xml");
	}

	public SqlQuery getQuery(String queryName) {
		return querys.get(queryName);
	}

	public Dimension getSharedDimension(String dimName) {
		return sharedDimensions.get(dimName);
	}

	public void checkSqlFiles() {
		try {
			if (sqlFiles == null)
				return;
			for (Resource sqlFile : sqlFiles) {
				Long update = fileUpdates.get(sqlFile.getFilename());
				if (update != null && sqlFile.lastModified() == update)
					continue;
				log.debug("load sql file {}", sqlFile.getFilename());
				fileUpdates.put(sqlFile.getFilename(), sqlFile.lastModified());
				parseXml(sqlFile);
			}
		} catch (IOException e) {
			log.error("解析Sql文件失败", e);
		}
	}

	/**
	 * 对union进行事前处理，产生新的options
	 * 
	 * @param map
	 */
	private void processUnion(Map<String, SqlQuery> map) {
		for (SqlQuery query : map.values()) {
			if (query.union == null)
				continue;

			for (String part : query.union) {
				SqlQuery partQ = map.get(part);
				if (partQ == null) {
					log.error("Unknown sub query {}", part);
					query.union = null;
					break;
				}

				if (query.options != null) {
					List<SqlOpt> myOptions = partQ.options;
					Map<String, SqlOpt> myMap = new HashMap<String, SqlOpt>(
							myOptions.size());
					for (SqlOpt opt : myOptions) {
						myMap.put(opt.name, opt);
					}

					List<SqlOpt> myNewOpts = new ArrayList<SqlOpt>();
					for (SqlOpt opt : query.options) {

						myNewOpts
								.add((SqlOpt) extend(opt, myMap.get(opt.name)));
					}
					partQ.options = myNewOpts; // 产生覆盖后的options
				}

			}
		}

	}

	private void parseDimension(String fileName, Element root,
			Map<String, SqlColumn> refColumns) {
		Dimension dimension = null;
		@SuppressWarnings("unchecked")
		List<Element> sharedDimNodes = root.selectNodes("dimension");

		Map<String, Dimension> map = new HashMap<String, Dimension>();
		for (Element dimNode : sharedDimNodes) {
			dimension = new Dimension();

			dimension.srcFileName = fileName;

			dimension.entity = dimNode.attributeValue("entity");
			dimension.name = dimNode.attributeValue("name");

			dimension.primaryKey = dimNode.attributeValue("primaryKey");
			if (dimension.primaryKey == null)
				dimension.primaryKey = "id";
			if (dimension.name == null) {
				log.error("****************************************");
				log.error("<dimension>的name属性不能为空");
				log.error("****************************************");
				continue;
			}
			if (dimension.entity == null) {
				log.error("****************************************");
				log.error("<dimension>的entity属性不能为空");
				log.error("****************************************");
				continue;
			}
			// 在本文件中检查
			if (map.containsKey(dimension.name)) {
				log.error("****************************************");
				log.error("在{}中重复定义了dimension:{}", fileName, dimension.name);
				log.error("****************************************");
				continue;
			}
			// 在其他文件中检查
			Dimension ad = sharedDimensions.get(dimension.name);
			if (ad != null && !ad.srcFileName.equals(fileName)) {
				log.error("****************************************");
				log.error("已经在{}中定义了dimension: {}", ad.srcFileName,
						dimension.name);
				log.error("****************************************");
				continue;
			}

			@SuppressWarnings("unchecked")
			List<Element> levelNodes = dimNode.selectNodes("level");
			if (levelNodes.isEmpty()) {
				log.error("****************************************");
				log.error("<dimension>的必须至少有一个level子元素");
				log.error("****************************************");
				continue;
			}

			dimension.levels = new ArrayList<SqlColumn>(levelNodes.size());
			SqlColumn level = null;
			for (Element levelNode : levelNodes) {
				level = parseColumn(levelNode, refColumns);
				if (level.name == null)
					level.name = level.field;
				if (level.dimensionUsage != null) {
					log.error("****************************************");
					log.error("<level>的不能设置dimensionUsage");
					log.error("****************************************");
					level.dimensionUsage = null;
				}
				level.parentDimension = dimension;
				dimension.levels.add(level);
			}

			map.put(dimension.name, dimension);
		}
		sharedDimensions.putAll(map);
	}

	private void parseQuery(String fileName, Element root,
			Map<String, SqlColumn> refColumns) {
		@SuppressWarnings("unchecked")
		List<Element> sqlNodes = root.selectNodes("sql");
		SqlQuery q = null;
		SqlOpt opt = null;
		Map<String, SqlQuery> map2 = new HashMap<String, SqlQuery>();
		for (Element sqlNode : sqlNodes) {

			q = new SqlQuery();
			q.name = sqlNode.attributeValue("name");
			q.srcFileName = fileName;
			if (q.name == null) {
				log.error("****************************************");
				log.error("<sql>的name属性不能为空");
				log.error("****************************************");
				continue;
			}

			// 在本文件中检查
			if (map2.containsKey(q.name)) {
				log.error("****************************************");
				log.error("在{}中重复定义了query:{}", fileName, q.name);
				log.error("****************************************");
				continue;
			}
			// 在其他文件中检查
			SqlQuery ad = querys.get(q.name);
			if (ad != null && !ad.srcFileName.equals(fileName)) {
				log.error("****************************************");
				log.error("已经在{}中定义了query:{}", ad.srcFileName, q.name);
				log.error("****************************************");
				continue;
			}

			// 李炎2013-07-07
			q.isCollect = !"false".equals(sqlNode.attributeValue("isCollect"));
			q.stmt = sqlNode.attributeValue("stmt");
			q.isNative = Boolean.parseBoolean(sqlNode.attributeValue(
					"isNative", "false"));
			q.template = sqlNode.attributeValue("template");
			q.orderby = sqlNode.attributeValue("orderby");
			q.method = sqlNode.attributeValue("method");
			q.stat = sqlNode.attributeValue("stat");
			String s = sqlNode.attributeValue("union");
			if (s != null) {
				// 在xml中union太长，可能换行
				String[] unions = s.trim().replaceAll("\r|\n| ", "").split(",");
				q.union = unions;
			}

			if (q.stmt != null && q.method != null) {
				log.error("****************************************");
				log.error("<sql>的stmt和method属性不能同时有值");
				log.error("****************************************");
				continue;
			}
			if (q.stmt == null && q.method == null && q.union == null) {
				log.error("****************************************");
				log.error("<sql>的stmt/method/union属性不能同时为空");
				log.error("****************************************");
				continue;
			}

			q.hint = sqlNode.attributeValue("hint");
			if (q.hint != null && q.hint.indexOf('$') >= 0) {
				try {
					q.dynaHint = true;
				} catch (Throwable e) {
					log.error("****************************************");
					log.error("<sql>的hint属性解析出错:{}", q.hint, e);
					log.error("****************************************");
				}
			}
			q.title = sqlNode.attributeValue("title");
			if (q.title != null && q.title.indexOf('$') >= 0) {
				try {
					q.dynaTitle = true;
				} catch (Throwable e) {
					log.error("****************************************");
					log.error("<sql>的title属性解析出错:{}", q.title, e);
					log.error("****************************************");
				}
			}
			if (q.stmt != null || q.union != null) {
				if (q.stmt != null && q.orderby == null) {

					if (q.stmt.indexOf("order by") < 0) {
						int i = q.stmt.indexOf(" as ");
						if (i > 0) {
							i = i + 4;
							int j = q.stmt.indexOf(",", i);
							int k = q.stmt.indexOf(" ", i);
							if (j < 0)
								j = k;
							else if (k > 0 && j > k)
								j = k;

							if (j > 0)
								q.orderby = q.stmt.substring(i, j).trim()
										+ ".id asc";
							else
								q.orderby = q.stmt.substring(i).trim()
										+ ".id asc";
						}
					}
				}
				@SuppressWarnings("unchecked")
				List<Element> optNodes = sqlNode.selectNodes("option");

				q.options = new ArrayList<SqlOpt>(optNodes.size());
				for (Element optNode : optNodes) {
					opt = new SqlOpt();
					opt.name = optNode.attributeValue("name");

					opt.cond = optNode.attributeValue("cond");

					opt.type = optNode.attributeValue("type");

					if (opt.name == null) {
						log.error("****************************************");
						log.error("<option>的name属性不能为空");
						log.error("****************************************");
						continue;
					}
					if (opt.cond != null) {

						if (opt.cond.indexOf(" or ") >= 0) {
							opt.cond = '(' + opt.cond + ')';
						}
						// 判断是否为like, 当传入参数时，在parseValue方法中会补充%%
						if (opt.cond.indexOf(" like ") >= 0 && opt.type == null)
							opt.type = LIKE;

						if (opt.cond.indexOf('$') >= 0) {
							try {
								opt.dynaCond = true;
							} catch (Throwable e) {
								log.error("****************************************");
								log.error("<option>的cond属性解析出错:{}", opt.cond, e);
								log.error("****************************************");
							}
						}

					}
					q.options.add(opt);
				}

			} else {
				@SuppressWarnings("unchecked")
				List<Element> paramNodes = sqlNode.selectNodes("param");

				q.params = new ArrayList<MethodParam>(paramNodes.size());
				MethodParam param = null;
				for (Element paramNode : paramNodes) {
					param = new MethodParam();
					param.value = paramNode.attributeValue("value");
					if (param.value == null) {
						log.error("****************************************");
						log.error("<param>的value属性不能为空");
						log.error("****************************************");
						continue;
					}

					try {
						param.dynaValue = true;
					} catch (Throwable e) {
						log.error("****************************************");
						log.error("<param>的value属性解析出错:{}", param.value, e);
						log.error("****************************************");
					}
					q.params.add(param);
				}

			}

			@SuppressWarnings("unchecked")
			List<Element> columnNodes = sqlNode.selectNodes("column");

			SqlColumn column = null;
			if (!columnNodes.isEmpty()) {

				q.columnIndexMap = new HashMap<String, Integer>(
						columnNodes.size());
				q.columns = new ArrayList<SqlColumn>(columnNodes.size());
				StringBuffer sb = new StringBuffer();

				for (Element columnNode : columnNodes) {
					if (sb.length() > 0)
						sb.append(',');
					column = parseColumn(columnNode, refColumns);

					if (column.name != null)
						q.columnIndexMap.put(column.name, q.columns.size()); // column的索引
					q.columns.add(column);
					sb.append(column.field != null ? column.field : 0);
				}
				q.fieldStr = sb.toString();
			}

			@SuppressWarnings("unchecked")
			List<Element> dimNodes = sqlNode.selectNodes("dimension");
			if (!dimNodes.isEmpty()) {

				q.dimensions = new HashMap<String, SqlColumn>(dimNodes.size());

				for (Element dimNode : dimNodes) {
					column = parseColumn(dimNode, refColumns);
					if (column.name == null) {
						log.error("****************************************");
						log.error("<dimension>的name属性不能为空");
						log.error("****************************************");
						continue;
					}
					q.dimensions.put(column.name, column);
				}
			}
			@SuppressWarnings("unchecked")
			List<Element> measureNodes = sqlNode.selectNodes("measure");
			if (!measureNodes.isEmpty()) {

				// parse measureTitle first
				@SuppressWarnings("unchecked")
				List<Element> mtNodes = sqlNode.selectNodes("measureTitle");
				Map<String, MeasureTitle> measureTitles = new HashMap<String, MeasureTitle>(
						mtNodes.size());

				for (Element mtNode : mtNodes) {
					parseMeasureTitle(measureTitles, null, mtNode);
				}

				q.measures = new HashMap<String, SqlColumn>(measureNodes.size());

				int highestLevel = 0;
				for (Element measureNode : measureNodes) {
					column = parseColumn(measureNode, refColumns);
					if (column.name == null) {
						log.error("****************************************");
						log.error("<measure>的name属性不能为空");
						log.error("****************************************");
						continue;
					}
					String parentTitle = measureNode
							.attributeValue("parentTitle");
					if (parentTitle != null) {
						MeasureTitle mt = measureTitles.get(parentTitle);

						if (mt == null) {
							log.error("****************************************");
							log.error("not found measureTitle {}", parentTitle);
							log.error("****************************************");
							continue;
						}
						column.parentTitle = mt;
						int level = 1;
						while (mt != null) {
							if (mt.level < level)
								mt.level = level;
							highestLevel = Math.max(highestLevel, level);
							level = mt.level + 1;
							mt = mt.parent;
						}
					}
					q.measures.put(column.name, column);
				}

				if (highestLevel > 0) {
					for (SqlColumn measure : q.measures.values()) {
						String[] mts = new String[highestLevel + 1];
						int[] spans = new int[highestLevel + 1];
						MeasureTitle mt = measure.parentTitle;
						if (mt != null) {
							spans[highestLevel - mt.level + 1] = mt.level;
							mts[highestLevel - mt.level + 1] = measure.title;
							while (true) {

								if (mt.parent == null) {
									spans[0] = highestLevel + 1 - mt.level;
									mts[0] = mt.title;
									break;
								} else {
									spans[highestLevel - mt.parent.level + 1] = mt.parent.level
											- mt.level;
									mts[highestLevel - mt.parent.level + 1] = mt.title;
								}
								mt = mt.parent;
							}
						} else {
							spans[0] = highestLevel + 1;
							mts[0] = measure.title;
						}
						measure.parentTitlesSpans = spans;
						measure.parentTitles = mts;
					}
				}

			}
			if (q.stmt != null && q.stmt.indexOf('$') >= 0) {
				try {
					q.dynaStmt = true;
				} catch (Throwable e) {
					log.error("****************************************");
					log.error("<sql>的stmt属性解析出错: {}", q.stmt, e);
					log.error("****************************************");
				}
			}

			q.statTitle = sqlNode.attributeValue("statTitle");
			if (q.statTitle != null) {
				try {
					q.dynaStatTitle = true;
				} catch (Throwable e) {
					log.error("****************************************");
					log.error("<sql>的statTitle属性解析出错: {}", q.statTitle, e);
					log.error("****************************************");
				}
			}

			q.statOper = sqlNode.attributeValue("statOper");
			if (q.statOper != null) {
				try {
					q.dynaStatOper = true;
				} catch (Throwable e) {
					log.error("****************************************");
					log.error("<sql>的statOper属性解析出错: {}", q.statOper, e);
					log.error("****************************************");
				}
			}

			q.statDateTime = sqlNode.attributeValue("statDateTime");
			if (q.statDateTime != null) {
				try {
					q.dynaStatDateTime = true;
				} catch (Throwable e) {
					log.error("****************************************");
					log.error("<sql>的statDateTime属性解析出错: {}", q.statDateTime, e);
					log.error("****************************************");
				}
			}

			q.extend = sqlNode.attributeValue("extends");
			if (q.extend != null) {
				if (!map2.containsKey(q.extend)) {
					log.error("****************************************");
					log.error("<sql>中没有发现继承的父sql: {}", q.extend);
					log.error("****************************************");
					continue;
				}
				extendQuery(q, map2.get(q.extend));
			}

			map2.put(q.name.trim(), q);
		}
		processUnion(map2);
		querys.putAll(map2);
	}

	/**
	 * 从parent Query 继承options,columns,dimension
	 * 
	 * @param child
	 * @param parent
	 */
	private void extendQuery(SqlQuery child, SqlQuery parent) {
		if (child.orderby == null && parent.orderby != null)
			child.orderby = parent.orderby;
		if (child.hint == null && parent.hint != null)
			child.hint = parent.hint;

		if (parent.options != null) {
			@SuppressWarnings("unchecked")
			List<SqlOpt> myList = child.options != null ? child.options
					: Collections.EMPTY_LIST;
			Map<String, SqlOpt> myMap = new HashMap<String, SqlOpt>(
					myList.size());
			for (SqlOpt opt : myList) {
				myMap.put(opt.name, opt);
			}

			List<SqlOpt> myNewList = new ArrayList<SqlOpt>();
			for (SqlOpt opt : parent.options) {
				myNewList.add((SqlOpt) extend(opt, myMap.remove(opt.name)));
			}
			for (SqlOpt opt : myMap.values()) {
				myNewList.add(opt);
			}
			child.options = myNewList; // 产生覆盖后的options
		}

		if (parent.dimensions != null) {
			@SuppressWarnings("unchecked")
			Map<String, SqlColumn> myMap = child.dimensions != null ? child.dimensions
					: Collections.EMPTY_MAP;

			Map<String, SqlColumn> myNewMap = new HashMap<String, SqlColumn>();
			for (SqlColumn dim : parent.dimensions.values()) {
				myNewMap.put(dim.name,
						(SqlColumn) extend(dim, myMap.remove(dim.name)));
			}
			for (SqlColumn dim : myMap.values()) {
				myNewMap.put(dim.name, dim);
			}
			child.dimensions = myNewMap; // 产生覆盖后的dimensions
		}

		if (parent.measures != null) {
			@SuppressWarnings("unchecked")
			Map<String, SqlColumn> myMap = child.measures != null ? child.measures
					: Collections.EMPTY_MAP;

			Map<String, SqlColumn> myNewMap = new HashMap<String, SqlColumn>();
			for (SqlColumn dim : parent.measures.values()) {
				myNewMap.put(dim.name,
						(SqlColumn) extend(dim, myMap.remove(dim.name)));
			}
			for (SqlColumn dim : myMap.values()) {
				myNewMap.put(dim.name, dim);
			}
			child.measures = myNewMap; // 产生覆盖后的measures
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, SqlColumn> parseSharedColumns(String fileName,
			Element root) {
		List<Element> columnNodes = root.selectNodes("refColumn");

		if (columnNodes.isEmpty())
			return Collections.EMPTY_MAP;

		Map<String, SqlColumn> map = new HashMap<String, SqlColumn>(
				columnNodes.size());
		SqlColumn column = null;
		for (Element columnNode : columnNodes) {
			column = parseColumn(columnNode, Collections.EMPTY_MAP);

			column.srcFileName = fileName;
			// 在本文件中检查
			if (map.containsKey(column.name)) {
				log.error("****************************************");
				log.error("在{}中重复定义了refColumn:{}", fileName, column.name);
				log.error("****************************************");
				continue;
			}

			map.put(column.name, column);

		}
		return map;
	}

	private void parseXml(Resource f) {
		try {
			Document document = saxReader.read(f.getInputStream());
			Element root = document.getRootElement();
			Map<String, SqlColumn> refColumns = parseSharedColumns(
					f.getFilename(), root);
			parseDimension(f.getFilename(), root, refColumns);
			parseQuery(f.getFilename(), root, refColumns);
		} catch (Exception de) {
			log.error("解析{}出错", f.getFilename(), de);
		}

	}

	private void parseMeasureTitle(Map<String, MeasureTitle> nameTitles,
			MeasureTitle parent, Element mtNode) {
		MeasureTitle mt = new MeasureTitle();
		mt.name = mtNode.attributeValue("name");
		mt.title = mtNode.attributeValue("title");
		if (mt.title == null)
			mt.title = "";
		String s = mtNode.attributeValue("level");
		if (s != null)
			mt.level = Integer.parseInt(s);
		mt.parent = parent;
		if (mt.name != null) {
			if (nameTitles.containsKey(mt.name)) {
				log.error("****************************************");
				log.error(">>measureTitle {} duplicated", mt.name);
				log.error("****************************************");
				return;
			}
			nameTitles.put(mt.name, mt);
		}
		@SuppressWarnings("unchecked")
		List<Element> cNodes = mtNode.selectNodes("measureTitle");
		if (!cNodes.isEmpty()) {
			for (Element cNode : cNodes) {
				parseMeasureTitle(nameTitles, mt, cNode);
			}
		}
	}

	private SqlColumn parseColumn(Element columnNode,
			Map<String, SqlColumn> refColumns) {
		String ref = columnNode.attributeValue("ref");
		SqlColumn refColumn = null;
		if (ref != null && refColumns != null) {
			refColumn = refColumns.get(ref);
			if (refColumn == null) {
				log.error("****************************************");
				log.error("没有定义refColumn {}", ref);
				log.error("****************************************");
			}
		}

		SqlColumn column = new SqlColumn();
		column.title = columnNode.attributeValue("title");
		if (column.title == null && refColumn != null)
			column.title = refColumn.title;
		if (column.title == null)
			column.title = "";
		column.value = columnNode.attributeValue("value");
		if (column.value == null && refColumn != null)
			column.value = refColumn.value;
		column.field = columnNode.attributeValue("field");
		if (column.field == null && refColumn != null)
			column.field = refColumn.field;
		if (column.field != null) {

			int aI = column.field.lastIndexOf(" as ");
			if (aI > 0) {
				// column.field = column.field.substring(aI+4);
				column.field = null;// TODO hibernate 'as' bug
			}
		}

		column.name = columnNode.attributeValue("name");
		if (column.name == null && refColumn != null)
			column.name = refColumn.name;

		column.dimensionUsage = columnNode.attributeValue("dimensionUsage");
		if (column.dimensionUsage == null && refColumn != null)
			column.dimensionUsage = refColumn.dimensionUsage;

		column.dimensionJoin = columnNode.attributeValue("dimensionJoin");
		if (column.dimensionJoin == null && refColumn != null)
			column.dimensionJoin = refColumn.dimensionJoin;

		column.calculation = columnNode.attributeValue("calculation");
		if (column.calculation == null && refColumn != null)
			column.calculation = refColumn.calculation;

		column.width = columnNode.attributeValue("width");
		if (column.width == null && refColumn != null)
			column.width = refColumn.width;

		column.align = columnNode.attributeValue("align");
		if (column.align == null && refColumn != null)
			column.align = refColumn.align;

		if (columnNode.attributeValue("hidden") == null && refColumn != null)
			column.hidden = refColumn.hidden;
		else
			column.hidden = "true".equals(columnNode.attributeValue("hidden"));

		column.format = columnNode.attributeValue("format");
		if (column.format == null && refColumn != null)
			column.format = refColumn.format;

		column.dict = columnNode.attributeValue("dict");
		if (column.dict == null && refColumn != null)
			column.dict = refColumn.dict;

		column.type = columnNode.attributeValue("type");
		if (column.type == null && refColumn != null) {
			column.type = refColumn.type;
		}
		return column;
	}

	private Object extend(Object parent, Object childParam) {
		Class<?> c = parent.getClass();
		try {
			Object clone = c.newInstance();
			for (Field f : c.getDeclaredFields()) {
				int modifies = f.getModifiers();
				if (!Modifier.isPublic(modifies) || Modifier.isFinal(modifies))
					continue;
				Object v = f.get(parent);
				Object cv = childParam != null ? f.get(childParam) : null;
				f.set(clone, cv != null ? cv : v);
			}
			return clone;
		} catch (Exception e) {
			throw new CrmException("extend " + c + " failed", e);
		}
	}
}
