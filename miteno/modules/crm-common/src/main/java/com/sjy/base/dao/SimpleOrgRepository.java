/**
 * 
 */
package com.sjy.base.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sjy.base.domain.SimpleOrg;

/**
 * @Title: SimpleOrgRepository.java
 * @Package com.sjy.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月24日 下午8:32:17
 * @version V1.0
 */
@Repository
public interface SimpleOrgRepository extends JpaRepository<SimpleOrg, Long> {

	SimpleOrg findByAppId(String appId);

	@Query("select new Map(o.id as id, trim(o.name) as name) from SimpleOrg as o where o.id = ?1 or o.parent = ?1")
	List<Map<String, Object>> findSelfAndChilds(Long orgId);

	@Query("select new Map(o.id as id, trim(o.name) as name) from SimpleOrg as o where o.parent = ?1")
	List<Map<String, Object>> findChilds(Long orgId);

	List<SimpleOrg> findByOrgLevel(Integer orgLevel);

	@Query("select o from SimpleOrg as o,SimpleOrgTree as ot join ot.organization as org left join ot.parentTree as pt where org.id=o.id and (pt.organization.id =?1  or (ot.organization.id=?1 and (ot.parentOrgLevel=(?2 -100) or ot.parentOrgLevel is null))) ")
	List<SimpleOrg> findSelfAndAllChilds(Long orgId,int orgLevel);
}
