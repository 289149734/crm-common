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
			name = new Identifier("TBL_" + name.getText(), name.isQuoted());
		}
		log.debug("加载数据库表-----------" + name.getText());
		return name;
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier name, JdbcEnvironment context) {
		log.debug("加载数据库序列-----------" + name.getText());
		return name;
	}

}
