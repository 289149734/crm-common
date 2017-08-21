/**
 * 
 */
package com.sjy.util;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.Data;

import org.hibernate.annotations.GenericGenerator;

/**
 * @copyright(c) Copyright SJY Corporation 2017.
 * @since 2017年8月21日
 * @author wy
 * @e-mail 42479322@qq.com
 * 
 */
@SuppressWarnings("serial")
@MappedSuperclass
@Data
public class MySqlSeqRootEntity implements Serializable {

	@Id
	@GenericGenerator(name = "mysqlSeq", strategy = "com.sjy.util.MysqlIdGenerator")
	@GeneratedValue(generator = "mysqlSeq")
	@Column(name = "id", unique = true, length = 36, nullable = false)
	protected Long id;

	@Version
	protected Integer version = -1;

}
