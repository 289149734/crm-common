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
import org.springframework.stereotype.Service;

import com.sjy.base.dao.SimpleOrgRepository;
import com.sjy.base.domain.SimpleOrg;
import com.sjy.constant.SessionParamType;
import com.sjy.exception.CrmException;
import com.sjy.exception.CrmExceptionType;

import lombok.extern.slf4j.Slf4j;

/**
 * @Title: SimpleOrgService.java
 * @Package com.sjy.base.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月24日 下午8:41:46
 * @version V1.0
 */
@Slf4j
@Service
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
		String sessionId = session.getId();
		log.debug("获取当前登录机构：{}", sessionId);
		Session sess = sessionRepository.getSession(sessionId);
		if (sess == null) {
			throw new CrmException(CrmExceptionType.Login_Timeoue);
		}
		Long orgId = (Long) sess.getAttribute(SessionParamType.CURRENT_ORG);
		if (orgId == null) {
			throw new CrmException(CrmExceptionType.Login_Timeoue);
		}
		log.debug("当前机构ID：{}", orgId);
		SimpleOrg org = simpleOrgRepository.findOne(orgId);
		if (org == null) {
			throw new CrmException(CrmExceptionType.Customize_Error, "机构信息不存在，请重新登录");
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

	/**
	 * 通过orgId查询直接子机构
	 * 
	 * @param orgId
	 * @return
	 */
	public List<Map<String, Object>> findSelfAndChilds(Long orgId) {
		return simpleOrgRepository.findSelfAndChilds(orgId);
	}

	/**
	 * 通过orgLevel查询机构
	 * 
	 * @param orgLevel
	 * @return
	 */
	public List<SimpleOrg> findByOrgLevel(Integer orgLevel) {
		return simpleOrgRepository.findByOrgLevel(orgLevel);
	}

	/**
	 * 通过orgId获取机构
	 * 
	 * @param orgId
	 * @return
	 */
	public SimpleOrg findOne(Long orgId) {
		return simpleOrgRepository.findOne(orgId);
	}

	/**
	 * 获取当前机构的上级发卡机构
	 * 
	 * @param org
	 * @return
	 */
	public SimpleOrg getIssuerOrg(SimpleOrg org) {
		SimpleOrg issuerOrg = org;
		while (issuerOrg != null && !issuerOrg.isIssuer()) {
			issuerOrg = findOne(issuerOrg.getParent());
		}
		return issuerOrg;
	}
}
