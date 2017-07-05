package com.sjy.table.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.type.Type;

import com.sjy.exception.CrmException;
import com.sjy.table.config.SqlQuery;

public final class QueryUtil {

	public static String getHint(SqlQuery query, String addHint) {
		String hint = query.hint;
		if (query.dynaHint != null) {
			// try {
			// hint = query.dynaHint.run().toString();
			// } catch (Exception e) {
			// throw new CrmException("解析动态hint出错:" + query.title, e);
			// }
		}
		if (hint == null && addHint == null)
			return "";

		if (hint == null)
			hint = "";
		if (addHint != null)
			hint = addHint + " " + hint;
		return "/*+" + hint + "*/";
	}

	public static String getTitle(SqlQuery query) {
		String title = query.title;
		if (query.dynaTitle != null) {
			// try {
			// title = query.dynaTitle.run().toString();
			// } catch (Exception e) {
			// throw new CrmException("解析动态title出错:" + query.title, e);
			// }
		}
		return title;
	}

	public static String getTitle(SqlQuery query, String locale) {
		String title = query.title;
		try {
			// if (query.dynaTitle == null) {
			// return title;
			// }
			// Binding b = query.dynaTitle.getBinding();
			// if (b != null)
			// b.setProperty("_c_locale", locale == null ? "zh" : locale);
			// title = query.dynaTitle.run().toString();
		} catch (Exception e) {
			throw new CrmException("解析动态title出错:" + query.title, e);
		}
		return title;
	}

	public static QueryTranslatorImpl compileSql(SessionFactory sessionFactory,
			String originalHql) {
		QueryTranslatorImpl queryTranslator = new QueryTranslatorImpl(
				originalHql, originalHql, Collections.EMPTY_MAP,
				(SessionFactoryImplementor) sessionFactory);
		try {
			queryTranslator.compile(Collections.EMPTY_MAP, false);
			return queryTranslator;
		} catch (Exception me) {
			throw new CrmException("hql translation error", me);
		}
	}

	public static List<Object[]> parseSqlResult(SessionFactory sessionFactory,
			ResultSet rs, int colSize, int maxResult,
			QueryTranslatorImpl translator) throws SQLException {
		List<Object[]> l = maxResult <= 200 ? new ArrayList<Object[]>(maxResult)
				: new ArrayList<Object[]>();
		Object[] os;
		if (translator == null) {
			for (int k = 0; rs.next() && k < maxResult; k++) {
				os = new Object[colSize];
				for (int k1 = 0; k1 < colSize; k1++)
					os[k1] = rs.getObject(k1 + 1);
				l.add(os);
			}
		} else {
			Type[] types = translator.getReturnTypes();
			String[][] alias = translator.getColumnNames();
			Type type = null;
			for (int k = 0; rs.next() && k < maxResult; k++) {
				os = new Object[colSize];
				for (int k1 = 0; k1 < colSize; k1++) {
					type = types[k1];
					if (type != null)
						os[k1] = type.nullSafeGet(rs, alias[k1],
								(SessionImplementor) sessionFactory
										.getCurrentSession(), null);
					else
						os[k1] = rs.getObject(k1 + 1);
				}
				l.add(os);
			}
		}
		return l;
	}

}
