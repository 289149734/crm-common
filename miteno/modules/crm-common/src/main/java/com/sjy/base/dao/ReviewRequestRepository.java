/**
 * 
 */
package com.sjy.base.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sjy.base.domain.ReviewRequest;

/**
 * @Title: AuditRequestRepository.java
 * @Package com.sjy.base.dao
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月28日 下午7:09:07
 * @version V1.0
 */
@Repository
public interface ReviewRequestRepository extends JpaRepository<ReviewRequest, String> {

	ReviewRequest findByAuditObjAndAuditObjIdAndStatus(String auditObj, String auditObjId, Integer status);

}
