/**
 * 
 */
package com.sjy.base.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.stereotype.Service;

import com.sjy.base.dao.SimpleOrgRepository;
import com.sjy.base.dao.SimpleOrgTreeRepository;
import com.sjy.base.domain.SimpleOrg;
import com.sjy.base.domain.SimpleOrgTree;

/**
 * @Title: SimpleOrgTreeService.java
 * @Package com.sjy.base.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月31日 下午7:48:13
 * @version V1.0
 */
@Transactional
@Service
public class SimpleOrgTreeService {

	@Resource
	SimpleOrgRepository simpleOrgRepository;

	@Resource
	SimpleOrgTreeRepository simpleOrgTreeRepository;

	public void createTree(SimpleOrg org, SimpleOrgTree parentTree, Integer parentOrgLevel) {
		SimpleOrgTree orgTree = simpleOrgTreeRepository.findByOrganizationAndParentOrgLevelAndParentTree(org,
				parentOrgLevel, parentTree);
		if (orgTree == null) {
			orgTree = new SimpleOrgTree();
			orgTree.setOrganization(org);
			orgTree.setOrgLevel(org.getOrgLevel());
			orgTree.setParentOrgLevel(parentOrgLevel);
			if (parentTree != null) {
				orgTree.setParentTree(parentTree);
			}
			simpleOrgTreeRepository.save(orgTree);
		}
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	public void updateOrgTree(List<SimpleOrg> orgs) {
		orgs.forEach(org -> {
			List<SimpleOrgTree> pOrgTrees = simpleOrgTreeRepository.findByOrgId(org.getParent());
			if (pOrgTrees != null && pOrgTrees.size() > 0) {
				createTree(org, pOrgTrees.get(0), pOrgTrees.get(0).getOrgLevel());

				pOrgTrees.forEach(pOrgTree -> {
					if (pOrgTree.getParentTree() != null) {
						createTree(org, pOrgTree.getParentTree(), pOrgTree.getParentTree().getOrgLevel());
					}
				});
			} else {
				createTree(org, null, null);
			}
		});
	}
}
