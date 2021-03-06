/**
 * 
 */
package com.sjy.util;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.Data;

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
public class RootEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", unique = true, nullable = false)
	protected Long id;

	@Version
	protected Integer version = -1;

}
