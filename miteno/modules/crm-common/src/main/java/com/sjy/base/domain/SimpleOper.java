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
 * @Title: SimpleOper.java
 * @Package com.sjy.domain
 * @Description: 基础操作员信息
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月24日 下午8:30:46
 * @version V1.0
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_operator")
@Data
public class SimpleOper implements Serializable {

	@Id
	@Column(unique = true, nullable = false)
	Long id;

	Integer version;

	@Column(length = 128)
	String name; // 名称

	@Column(name = "organization_id")
	Long organization;

	@Column(unique = true)
	String loginName; // 登陆名

	@Column(length = 24, unique = true)
	String uniqueId; // 唯一编码

	@Column(length = 40)
	String password; // 密码（加密）
}
