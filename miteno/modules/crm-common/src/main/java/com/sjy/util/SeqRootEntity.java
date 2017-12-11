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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Data;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * 
 * @since 2017年1月6日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@SuppressWarnings("serial")
@MappedSuperclass
@Data
public class SeqRootEntity implements Serializable {
	// org.hibernate.id.enhanced.SequenceStyleGenerator
	@Id
	@GenericGenerator(name = "DefaultSeq", strategy = "com.sjy.util.MySequenceStyleGenerator", parameters = {
			@Parameter(name = "prefer_sequence_per_entity", value = "true"),
			@Parameter(name = "sequence_per_entity_suffix", value = "SEQ_"),
			@Parameter(name = "initial_value", value = "100000"), @Parameter(name = "increment_size", value = "1") })
	@GeneratedValue(generator = "DefaultSeq")
	@Column(name = "id", unique = true, nullable = false)
	Long id;

	@Version
	Integer version = -1;
}
