/**
 * 
 */
package com.sjy.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import com.sjy.base.domain.SimpleOrg;
import com.sjy.base.domain.SimpleOrgTree;

/**
 * @Title: SimpleOrgTreeRepository.java
 * @Package com.sjy.base.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月31日 下午7:47:49
 * @version V1.0
 */
@Component
public interface SimpleOrgTreeRepository extends JpaRepository<SimpleOrgTree, Long> {

	@Query("select o from SimpleOrgTree as o where o.organization.id=? order by o.parentOrgLevel desc")
	List<SimpleOrgTree> findByOrgId(Long orgId);

	SimpleOrgTree findByOrganizationAndParentOrgLevelAndParentTree(SimpleOrg organization, Integer parentOrgLevel,
			SimpleOrgTree parentTree);

}
