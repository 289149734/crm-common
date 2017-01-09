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
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import lombok.Data;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2017年1月6日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@SuppressWarnings("serial")
@MappedSuperclass
@Data
public class SeqRootEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "WxMessageSeq")
	@SequenceGenerator(name = "WxMessageSeq", sequenceName = "seq_WxMessage")
	@Column(name = "id", unique = true, nullable = false)
	Long id;

	@Version
	Integer version = -1;
}
