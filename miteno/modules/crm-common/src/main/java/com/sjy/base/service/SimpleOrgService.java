/**
 * 
 */
package com.sjy.base.service;

import java.util.List;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.stereotype.Component;

import com.sjy.base.dao.SimpleOrgRepository;
import com.sjy.base.domain.SimpleOrg;

/**
 * @Title: SimpleOrgService.java
 * @Package com.sjy.base.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月24日 下午8:41:46
 * @version V1.0
 */
@Component
public class SimpleOrgService {

	@Resource
	SimpleOrgRepository simpleOrgRepository;

	@Transactional(TxType.REQUIRES_NEW)
	public int save(List<SimpleOrg> simpleOrgs) {
		List<SimpleOrg> list = simpleOrgRepository.save(simpleOrgs);
		if (list != null)
			return list.size();
		return 0;
	}
}
