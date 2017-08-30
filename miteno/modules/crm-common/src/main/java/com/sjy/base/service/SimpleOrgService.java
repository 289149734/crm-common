/**
 * 
 */
package com.sjy.base.service;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;

import com.sjy.base.dao.SimpleOrgRepository;
import com.sjy.base.domain.SimpleOrg;
import com.sjy.constant.SessionParamType;
import com.sjy.exception.CrmException;

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
	private HttpSession session;

	@Resource
	private HttpServletRequest request;

	@Resource
	SessionRepository<Session> sessionRepository;

	@Resource
	SimpleOrgRepository simpleOrgRepository;

	@Transactional(TxType.REQUIRES_NEW)
	public int save(List<SimpleOrg> simpleOrgs) {
		List<SimpleOrg> list = simpleOrgRepository.save(simpleOrgs);
		if (list != null)
			return list.size();
		return 0;
	}

	/**
	 * 获取当前机构
	 * 
	 * @return
	 */
	public SimpleOrg currentOrg() {
		Session sess = sessionRepository.getSession(session.getId());
		Long orgId = (Long) sess.getAttribute(SessionParamType.CURRENT_ORG);
		if (orgId == null) {
			throw new CrmException("当前机构不能为空");
		}
		SimpleOrg org = simpleOrgRepository.findOne(orgId);
		if (org == null) {
			throw new CrmException("当前机构不能为空");
		}
		return org;
	}

	/**
	 * 通过AppId获取机构
	 * 
	 * @param appId
	 * @return
	 */
	public SimpleOrg getOrgByAppId(String appId) {
		return simpleOrgRepository.findByAppId(appId);
	}

	/**
	 * 通过orgId查询直接子机构
	 * 
	 * @param orgId
	 * @return
	 */
	public List<Map<String, Object>> findChilds(Long orgId) {
		return simpleOrgRepository.findChilds(orgId);
	}
}
