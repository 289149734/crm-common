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
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月9日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@SuppressWarnings("serial")
@MappedSuperclass
@Data
public class UuidRootEntity implements Serializable {

	@Id
	@GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
	@GeneratedValue(generator = "uuid")
	@Column(name = "id", unique = true, length = 36, nullable = false)
	protected String id;

	@Version
	protected Integer version = -1;

}
