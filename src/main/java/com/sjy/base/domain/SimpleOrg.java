/**
 * 
 */
package com.sjy.base.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

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
public class SimpleOrg implements Serializable {

	@Id
	@Column(unique = true, nullable = false)
	Long id;

	Integer version;

	@Column(length = 128)
	String name; // 名称

	@Column(name = "org_level")
	Integer orgLevel; // 机构等级

	@Column(name = "parent_id")
	Long parent; // parent_id //上级单位

	@Column(length = 64, name = "orgAppId", unique = true)
	String appId; // 微信公众号ID
	
	@Column(length = 64, unique = true)
	String mchId; // 微信商户ID
	
	@Column(length = 32/*, unique = true*/)
	String wxAccountId; // 微信平台对应商户ID

	@Column(name = "isIssuer")
	boolean isIssuer = false; // 是否发卡

	@Column(name = "photo_id")
	Long photoId;
	
	@Column(name = "address")
	String address;
	
	@Column(name = "phone")
	String phone;
	
	@Column(name = "fullName")
	String fullName;
}
