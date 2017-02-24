/**
 * 
 */
package com.sjy.util;

import lombok.extern.slf4j.Slf4j;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * @copyright(c) Copyright SJY Corporation 2017.
 * @since 2017年2月23日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
@SuppressWarnings("serial")
public class MyPhysicalNamingStrategyStandardImpl extends PhysicalNamingStrategyStandardImpl {

	@Override
	public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
		if (!name.getText().toUpperCase().startsWith("TBL_")) {
			String tableName = "TBL_" + name.getText();
			if (tableName.toUpperCase().endsWith("DTO")) {
				tableName = tableName.substring(0, tableName.length() - 3);
			}
			name = new Identifier(tableName, name.isQuoted());
		}
		log.debug("加载数据库表-----------{}", name.getText());
		return name;
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
		String sequenceName = name.getText();
		if (sequenceName.toUpperCase().endsWith("DTO")) {
			sequenceName = sequenceName.substring(0, sequenceName.length() - 3);
			name = new Identifier(sequenceName, name.isQuoted());
		}
		log.debug("加载数据库序列-----------{}", name.getText());
		return name;
	}

}
