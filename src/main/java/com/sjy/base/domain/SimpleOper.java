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
@EqualsAndHashCode(callSuper = true)
public class SimpleOper extends SeqRootEntity {

	@Column(length = 128)
	String name; // 名称

	@Column(name = "organization_id")
	Long organization;
}
