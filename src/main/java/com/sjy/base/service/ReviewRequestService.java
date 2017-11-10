package com.sjy.base.service;

import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.sjy.base.dao.ReviewRequestRepository;
import com.sjy.base.domain.ReviewRequest;
import com.sjy.constant.AuditRuleType;
import com.sjy.constant.AuditStatus;
import com.sjy.dict.service.DictService;
import com.sjy.exception.CrmException;
import com.sjy.util.SeqRootEntity;
import com.sjy.util.UuidRootEntity;

/**
 * @Title: AuditRequestService.java
 * @Package com.sjy.base.service
 * @Description: 审核请求服务
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月28日 下午7:10:06
 * @version V1.0
 */
@Transactional
@Service
public class ReviewRequestService {

	@Resource
	ReviewRequestRepository reviewRequestRepository;

	@Resource
	SimpleOperService simpleOperService;

	@Resource
	SimpleOrgService simpleOrgService;

	@Resource
	DictService dictService;

	/**
	 * 创建审核请求
	 * 
	 * @param auditObj
	 * @param auditObjId
	 * @param ruleType
	 * @return
	 */
	public ReviewRequest createRequest(UuidRootEntity entity, Integer ruleType) {
		String auditObj = entity.getClass().getSimpleName();
		String auditObjId = entity.getId();
		ReviewRequest request = reviewRequestRepository.findByAuditObjAndAuditObjIdAndStatus(auditObj, auditObjId,
				AuditStatus.OPEN);
		if (request != null) {
			throw new CrmException("已经提交【{0}】审核申请", dictService.getText(AuditRuleType.CATEGORY, request.getRuleType()));
		}
		request = new ReviewRequest();
		request.setAuditObjId(auditObjId);
		request.setAuditObj(auditObj);
		request.setCreateOrg(simpleOrgService.currentOrg());
		request.setCreator(simpleOperService.currentOper());
		request.setRecordTime(new Date());
		request.setStatus(AuditStatus.OPEN);
		request.setRuleType(ruleType);
		return reviewRequestRepository.save(request);
	}

	public ReviewRequest createRequest(UuidRootEntity entity, Integer ruleType, Long orgId, Long operId) {
		String auditObj = entity.getClass().getSimpleName();
		String auditObjId = entity.getId();
		ReviewRequest request = reviewRequestRepository.findByAuditObjAndAuditObjIdAndStatus(auditObj, auditObjId,
				AuditStatus.OPEN);
		if (request != null) {
			throw new CrmException("已经提交【{0}】审核申请", dictService.getText(AuditRuleType.CATEGORY, request.getRuleType()));
		}
		request = new ReviewRequest();
		request.setAuditObjId(auditObjId);
		request.setAuditObj(auditObj);
		request.setCreateOrg(simpleOrgService.findOne(orgId));
		request.setCreator(simpleOperService.findOne(operId));
		request.setRecordTime(new Date());
		request.setStatus(AuditStatus.OPEN);
		request.setRuleType(ruleType);
		return reviewRequestRepository.save(request);
	}

	/**
	 * 创建审核请求
	 * 
	 * @param auditObj
	 * @param auditObjId
	 * @param ruleType
	 * @return
	 */
	public ReviewRequest createRequest(SeqRootEntity entity, Integer ruleType) {
		String auditObj = entity.getClass().getSimpleName();
		String auditObjId = "" + entity.getId();
		ReviewRequest request = reviewRequestRepository.findByAuditObjAndAuditObjIdAndStatus(auditObj, auditObjId,
				AuditStatus.OPEN);
		if (request != null) {
			throw new CrmException("已经提交【{0}】审核申请", dictService.getText(AuditRuleType.CATEGORY, request.getRuleType()));
		}
		request = new ReviewRequest();
		request.setAuditObjId(auditObjId);
		request.setAuditObj(auditObj);
		request.setCreateOrg(simpleOrgService.currentOrg());
		request.setCreator(simpleOperService.currentOper());
		request.setRecordTime(new Date());
		request.setStatus(AuditStatus.OPEN);
		request.setRuleType(ruleType);
		return reviewRequestRepository.save(request);
	}

	public ReviewRequest createRequest(SeqRootEntity entity, Integer ruleType, Long orgId, Long operId) {
		String auditObj = entity.getClass().getSimpleName();
		String auditObjId = "" + entity.getId();
		ReviewRequest request = reviewRequestRepository.findByAuditObjAndAuditObjIdAndStatus(auditObj, auditObjId,
				AuditStatus.OPEN);
		if (request != null) {
			throw new CrmException("已经提交【{0}】审核申请", dictService.getText(AuditRuleType.CATEGORY, request.getRuleType()));
		}
		request = new ReviewRequest();
		request.setAuditObjId(auditObjId);
		request.setAuditObj(auditObj);
		request.setCreateOrg(simpleOrgService.findOne(orgId));
		request.setCreator(simpleOperService.findOne(operId));
		request.setRecordTime(new Date());
		request.setStatus(AuditStatus.OPEN);
		request.setRuleType(ruleType);
		return reviewRequestRepository.save(request);
	}

	/**
	 * 通过审核
	 * 
	 * @param auditId
	 * @param comments
	 * @return
	 */
	public ReviewRequest passRequest(String auditId, String comments) {
		ReviewRequest request = reviewRequestRepository.findOne(auditId);
		if (request.getStatus() != AuditStatus.OPEN) {
			throw new CrmException("审核状态【{0}】才用允许审核操作", dictService.getText(AuditStatus.CATEGORY, AuditStatus.OPEN));
		}
		request.setAuditOrg(simpleOrgService.currentOrg());
		request.setAuditor(simpleOperService.currentOper());
		request.setAuditTime(new Date());
		request.setComments(comments);
		request.setStatus(AuditStatus.PASSED);
		return reviewRequestRepository.save(request);
	}

	public ReviewRequest passRequest(String auditId, String comments, Long orgId, Long operId) {
		ReviewRequest request = reviewRequestRepository.findOne(auditId);
		if (request.getStatus() != AuditStatus.OPEN) {
			throw new CrmException("审核状态【{0}】才用允许审核操作", dictService.getText(AuditStatus.CATEGORY, AuditStatus.OPEN));
		}
		request.setAuditOrg(simpleOrgService.findOne(orgId));
		request.setAuditor(simpleOperService.findOne(operId));
		request.setAuditTime(new Date());
		request.setComments(comments);
		request.setStatus(AuditStatus.PASSED);
		return reviewRequestRepository.save(request);
	}

	/**
	 * 未通过审核
	 * 
	 * @param auditId
	 * @param comments
	 * @return
	 */
	public ReviewRequest rejectRequest(String auditId, String comments) {
		ReviewRequest request = reviewRequestRepository.findOne(auditId);
		if (request.getStatus() != AuditStatus.OPEN) {
			throw new CrmException("审核状态【{0}】才用允许审核操作", dictService.getText(AuditStatus.CATEGORY, AuditStatus.OPEN));
		}
		request.setAuditOrg(simpleOrgService.currentOrg());
		request.setAuditor(simpleOperService.currentOper());
		request.setAuditTime(new Date());
		request.setComments(comments);
		request.setStatus(AuditStatus.REJECTED);
		return reviewRequestRepository.save(request);
	}

	public ReviewRequest rejectRequest(String auditId, String comments, Long orgId, Long operId) {
		ReviewRequest request = reviewRequestRepository.findOne(auditId);
		if (request.getStatus() != AuditStatus.OPEN) {
			throw new CrmException("审核状态【{0}】才用允许审核操作", dictService.getText(AuditStatus.CATEGORY, AuditStatus.OPEN));
		}
		request.setAuditOrg(simpleOrgService.findOne(orgId));
		request.setAuditor(simpleOperService.findOne(operId));
		request.setAuditTime(new Date());
		request.setComments(comments);
		request.setStatus(AuditStatus.REJECTED);
		return reviewRequestRepository.save(request);
	}

	/**
	 * 撤销审核
	 * 
	 * @param auditId
	 * @param comments
	 * @return
	 */
	public ReviewRequest cancelRequest(UuidRootEntity entity) {
		String auditObj = entity.getClass().getSimpleName();
		String auditObjId = entity.getId();
		ReviewRequest request = reviewRequestRepository.findByAuditObjAndAuditObjIdAndStatus(auditObj, auditObjId,
				AuditStatus.OPEN);
		if (request == null) {
			throw new CrmException("审核状态【{0}】才用允许撤销审核操作", dictService.getText(AuditStatus.CATEGORY, AuditStatus.OPEN));
		}
		request.setStatus(AuditStatus.CANCELED);
		return reviewRequestRepository.save(request);
	}

	/**
	 * 撤销审核
	 * 
	 * @param auditId
	 * @param comments
	 * @return
	 */
	public ReviewRequest cancelRequest(SeqRootEntity entity) {
		String auditObj = entity.getClass().getSimpleName();
		String auditObjId = "" + entity.getId();
		ReviewRequest request = reviewRequestRepository.findByAuditObjAndAuditObjIdAndStatus(auditObj, auditObjId,
				AuditStatus.OPEN);
		if (request == null) {
			throw new CrmException("审核状态【{0}】才用允许撤销审核操作", dictService.getText(AuditStatus.CATEGORY, AuditStatus.OPEN));
		}
		request.setStatus(AuditStatus.CANCELED);
		return reviewRequestRepository.save(request);
	}

}
