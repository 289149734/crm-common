/**
 * 
 */
package com.sjy.base.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sjy.util.SeqRootEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Title: SimpleOrg.java
 * @Package com.sjy.domain
 * @Description: 基础机构信息
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月24日 下午8:24:11
 * @version V1.0
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_organization")
@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleOrg extends SeqRootEntity {

	@Column(length = 128)
	String name; // 名称

	@Column(name = "org_level")
	Integer orgLevel; // 机构等级

	@Column(name = "parent_id")
	Long parent; // parent_id //上级单位

}
