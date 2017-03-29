/**
 * 
 */
package com.sjy.dict.domain;

import javax.persistence.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import com.sjy.util.UuidRootEntity;

/**
 * 字典数据表
 * 
 * @copyright(c) Copyright SJY Corporation 2017.
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
public class Dictionary extends UuidRootEntity {

	// 字典类型
	String category;

	// 字典编码
	Integer code;

	// 字典数据
	String text;

	// 是否可编辑
	Boolean editable = false;
}
