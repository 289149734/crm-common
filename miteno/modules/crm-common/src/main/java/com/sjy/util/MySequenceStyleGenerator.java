/**
 * 
 */
package com.sjy.util;

import java.util.Properties;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.relational.QualifiedName;
import org.hibernate.boot.model.relational.QualifiedNameParser;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.internal.util.config.ConfigurationHelper;

/**
 * @copyright(c) Copyright SJY Corporation 2017.
 * @since 2017年2月23日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
public class MySequenceStyleGenerator extends SequenceStyleGenerator {

	@Override
	protected QualifiedName determineSequenceName(Properties params, Dialect dialect, JdbcEnvironment jdbcEnv) {
		final String sequencePerEntitySuffix = ConfigurationHelper.getString(CONFIG_SEQUENCE_PER_ENTITY_SUFFIX, params, DEF_SEQUENCE_SUFFIX);
		// JPA_ENTITY_NAME value honors <class ... entity-name="..."> (HBM) and
		// @Entity#name (JPA) overrides.
		final String defaultSequenceName = ConfigurationHelper.getBoolean(CONFIG_PREFER_SEQUENCE_PER_ENTITY, params, false) ? sequencePerEntitySuffix
				+ params.getProperty(JPA_ENTITY_NAME)
				: DEF_SEQUENCE_NAME;

		final String sequenceName = ConfigurationHelper.getString(SEQUENCE_PARAM, params, defaultSequenceName);
		if (sequenceName.contains(".")) {
			return QualifiedNameParser.INSTANCE.parse(sequenceName);
		} else {
			// todo : need to incorporate implicit catalog and schema names
			final Identifier catalog = jdbcEnv.getIdentifierHelper().toIdentifier(ConfigurationHelper.getString(CATALOG, params));
			final Identifier schema = jdbcEnv.getIdentifierHelper().toIdentifier(ConfigurationHelper.getString(SCHEMA, params));
			return new QualifiedNameParser.NameParts(catalog, schema, jdbcEnv.getIdentifierHelper().toIdentifier(sequenceName));
		}
	}

}
