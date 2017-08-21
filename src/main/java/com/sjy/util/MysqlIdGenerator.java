package com.sjy.util;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.SQLQuery;
import org.hibernate.engine.query.spi.sql.NativeSQLQueryReturn;
import org.hibernate.engine.spi.NamedSQLQueryDefinition;
import org.hibernate.engine.spi.QueryParameters;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
/*
* @copyright(c) Copyright SJY Corporation 2017.
* @since 2017年8月21日
* @author wy
* @e-mail 42479322@qq.com
* 
* mysql生成自增列
* 
* */
public class MysqlIdGenerator implements IdentifierGenerator, Configurable{

	@Override
	public void configure(Type type, Properties properties, 
			ServiceRegistry serviceregistry) throws MappingException {
		
		
	}

	@SuppressWarnings({ "unchecked", "deprecation", "rawtypes" })
	@Override
	public Serializable generate(SessionImplementor session, Object obj) throws HibernateException {
		//获取当前SEQ
		String seqName = "SEQ_"+obj.getClass().getSimpleName();
		String query = "select next_val from "+seqName;
		NativeSQLQueryReturn queryReturns[] = null;
		NamedSQLQueryDefinition ns = new NamedSQLQueryDefinition(seqName, query, queryReturns, new ArrayList(),
				false, "", 30000, 1, FlushMode.AUTO,
				CacheMode.NORMAL, true, "", new HashMap(), false);
		SQLQuery sqlquery = session.createSQLQuery(ns);
		Long seq = ((BigInteger)sqlquery.uniqueResult()).longValue();
		
		//更新SEQ
		String update = "update "+seqName+" set next_val = next_val +1";
		ns = new NamedSQLQueryDefinition(seqName, update, queryReturns, new ArrayList(),
				false, "", 30000, 1, FlushMode.AUTO,
				CacheMode.NORMAL, true, "", new HashMap(), false);
		SQLQuery updatequery = session.createSQLQuery(ns);
		updatequery.executeUpdate();
		return ++seq;
	}

}
