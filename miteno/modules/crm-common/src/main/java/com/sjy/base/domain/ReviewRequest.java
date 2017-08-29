package com.sjy.base.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.sjy.util.UuidRootEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Title: AuditRequest.java
 * @Package com.sjy.base.domain
 * @Description: 审核数据的Entity
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月28日 下午6:48:46
 * @version V1.0
 */
@SuppressWarnings("serial")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class ReviewRequest extends UuidRootEntity {

	@Column(name = "audit_status")
	Integer status = 0; // 审核状态

	@Column(name = "audit_rule_type")
	Integer ruleType; // 规则类型

	Integer orgLevel; // 当前的审核机构级别

	@Column(length = 36, nullable = false)
	String auditObjId; // 审核的对象

	@Column(length = 64, nullable = false)
	String auditObj;// 审核对象名称

	@JoinColumn(name = "createOrgId")
	@ManyToOne(fetch = FetchType.LAZY)
	SimpleOrg createOrg; // 创建机构

	@JoinColumn(name = "creatorId")
	@ManyToOne(fetch = FetchType.LAZY)
	SimpleOper creator; // 创建者

	Date recordTime; // 创建时间

	@JoinColumn(name = "auditorId")
	@ManyToOne(fetch = FetchType.LAZY)
	SimpleOper auditor; // 审核人

	@JoinColumn(name = "auditOrgId")
	@ManyToOne(fetch = FetchType.LAZY)
	SimpleOrg auditOrg; // 审核机构

	Date auditTime; // 审核时间

	@JoinColumn(name = "nextOrganizationId")
	@ManyToOne(fetch = FetchType.LAZY)
	SimpleOrg nextOrganization;

	String comments;

}