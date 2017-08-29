/**
 * 
 */
package com.sjy.base.service;

import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import com.sjy.base.dao.ReviewRequestRepository;
import com.sjy.base.dao.SimpleOperRepository;
import com.sjy.base.dao.SimpleOrgRepository;
import com.sjy.base.domain.ReviewRequest;
import com.sjy.base.domain.SimpleOper;
import com.sjy.base.domain.SimpleOrg;
import com.sjy.constant.AuditStatus;
import com.sjy.util.UuidRootEntity;

/**
 * @Title: AuditRequestService.java
 * @Package com.sjy.base.service
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月28日 下午7:10:06
 * @version V1.0
 */
@Transactional
@Component
public class ReviewRequestService {

	@Resource
	ReviewRequestRepository reviewRequestRepository;

	@Resource
	SimpleOrgRepository simpleOrgRepository;

	@Resource
	SimpleOperRepository simpleOperRepository;

	/**
	 * 创建审核请求
	 * 
	 * @param auditObj
	 * @param auditObjId
	 * @param createOrg
	 * @param creator
	 * @return
	 */
	public ReviewRequest createRequest(UuidRootEntity entity, SimpleOrg createOrg, SimpleOper creator) {
		String auditObj = entity.getClass().getSimpleName();
		String auditObjId = entity.getId();
		ReviewRequest request = new ReviewRequest();
		request.setAuditObjId(auditObjId);
		request.setAuditObj(auditObj);
		request.setCreateOrg(createOrg);
		request.setCreator(creator);
		request.setRecordTime(new Date());
		request.setStatus(AuditStatus.OPEN);
		return reviewRequestRepository.save(request);
	}

	/**
	 * 通过审核
	 * 
	 * @param auditId
	 * @param auditOrg
	 * @param auditor
	 * @param comments
	 * @return
	 */
	public ReviewRequest passRequest(String auditId, SimpleOrg auditOrg, SimpleOper auditor, String comments) {
		ReviewRequest request = reviewRequestRepository.findOne(auditId);
		request.setAuditOrg(auditOrg);
		request.setAuditor(auditor);
		request.setAuditTime(new Date());
		request.setComments(comments);
		request.setStatus(AuditStatus.REALPASSED);
		return reviewRequestRepository.save(request);
	}

	/**
	 * 未通过审核
	 * 
	 * @param auditId
	 * @param auditOrg
	 * @param auditor
	 * @param comments
	 * @return
	 */
	public ReviewRequest rejectRequest(String auditId, SimpleOrg auditOrg, SimpleOper auditor, String comments) {
		ReviewRequest request = reviewRequestRepository.findOne(auditId);
		request.setAuditOrg(auditOrg);
		request.setAuditor(auditor);
		request.setAuditTime(new Date());
		request.setComments(comments);
		request.setStatus(AuditStatus.REJECTED);
		return reviewRequestRepository.save(request);
	}

	/**
	 * 撤销审核
	 * 
	 * @param auditId
	 * @param auditOrg
	 * @param auditor
	 * @param comments
	 * @return
	 */
	public ReviewRequest cancelRequest(String auditId, SimpleOrg auditOrg, SimpleOper auditor, String comments) {
		ReviewRequest request = reviewRequestRepository.findOne(auditId);
		request.setAuditOrg(auditOrg);
		request.setAuditor(auditor);
		request.setAuditTime(new Date());
		request.setComments(comments);
		request.setStatus(AuditStatus.CANCELED);
		return reviewRequestRepository.save(request);
	}

}
