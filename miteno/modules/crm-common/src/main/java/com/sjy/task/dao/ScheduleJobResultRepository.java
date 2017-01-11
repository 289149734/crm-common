/**
 * 
 */
package com.sjy.task.dao;

import java.util.Date;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.sjy.task.domain.ScheduleJob;
import com.sjy.task.domain.ScheduleJobResult;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Component
public interface ScheduleJobResultRepository extends CrudRepository<ScheduleJobResult, Long> {

	@Query("select max(o.businessDate) from ScheduleJobResult as o where o.job = ?1")
	Date findLastJobDate(ScheduleJob job);

	@Modifying
	@Query("update ScheduleJobResult as o set o.finishDate = ?2, o.costTimes = ?3, o.jobStatus = ?4, o.errorMsg = ?5 where o.id = ?1")
	int updateJob(String id, Date finishDate, long costTimes, int jobStatus, String errorMsg);
}
