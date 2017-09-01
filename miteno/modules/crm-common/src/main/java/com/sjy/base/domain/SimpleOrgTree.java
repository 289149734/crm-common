/**
 * 
 */
package com.sjy.base.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.sjy.util.UuidRootEntity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Title: SimpleOrgTree.java
 * @Package com.sjy.base.domain
 * @Description: 基础机构树信息
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月31日 下午5:48:57
 * @version V1.0
 */
@SuppressWarnings("serial")
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
public class SimpleOrgTree extends UuidRootEntity {

	/**
	 * organization
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	SimpleOrg organization;
	/**
	 * 上级机构等级 parentOrganizationLevel
	 */
	Integer parentOrgLevel;
	/**
	 * 上级机构树 parentTree
	 */
	@OneToOne(fetch = FetchType.LAZY)
	SimpleOrgTree parentTree;
	/**
	 * 本机构等级 the current organizationLevel
	 */
	@Column(name = "org_level")
	Integer orgLevel;

}
