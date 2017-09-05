/**
 * 
 */
package com.sjy.base.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Service;

import com.sjy.base.dao.SimpleOperRepository;
import com.sjy.base.domain.SimpleOper;
import com.sjy.constant.SessionParamType;
import com.sjy.exception.CrmException;

/**
 * @Title: SimpleOperService.java
 * @Package com.sjy.base.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月24日 下午9:00:18
 * @version V1.0
 */
@Service
public class SimpleOperService {
	@Resource
	private HttpSession session;

	@Resource
	private HttpServletRequest request;

	@Resource
	SessionRepository<Session> sessionRepository;

	@Resource
	SimpleOperRepository simpleOperRepository;

	@Transactional(TxType.REQUIRES_NEW)
	public int save(List<SimpleOper> simpleOpers) {
		List<SimpleOper> list = simpleOperRepository.save(simpleOpers);
		simpleOperRepository.flush();
		if (list != null)
			return list.size();
		return 0;
	}

	/**
	 * 获取当前操作员
	 * 
	 * @return
	 */
	public SimpleOper currentOper() {
		Session sess = sessionRepository.getSession(session.getId());
		Long operId = (Long) sess.getAttribute(SessionParamType.CURRENT_OPER);
		if (operId == null) {
			throw new CrmException("当前操作员不能为空");
		}
		SimpleOper oper = simpleOperRepository.findOne(operId);
		if (oper == null) {
			throw new CrmException("当前操作员不能为空");
		}
		return oper;
	}

	/**
	 * 根据operId获取操作员
	 * 
	 * @return
	 */
	public SimpleOper findOne(Long operId) {
		return simpleOperRepository.findOne(operId);
	}
}
