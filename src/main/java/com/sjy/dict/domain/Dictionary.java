/**
 * 
 */
package com.sjy.dict.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sjy.util.UuidRootEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * 字典数据表
 * 
 * @copyright(c) Copyright SJY Corporation 2017.
 * 
 * @since 2017年3月29日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SuppressWarnings("serial")
@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "category", "code" }) })
public class Dictionary extends UuidRootEntity {

	// 字典类型
	@Column(length = 32)
	String category;

	// 字典编码
	Integer code;

	// 字典数据
	@Column(length = 64)
	String text;

	// 是否可编辑
	Boolean editable = false;
}
