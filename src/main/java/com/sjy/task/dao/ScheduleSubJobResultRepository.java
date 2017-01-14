/**
 * 
 */
package com.sjy.task.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.sjy.task.domain.ScheduleSubJobResult;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2017年1月14日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Component
public interface ScheduleSubJobResultRepository extends CrudRepository<ScheduleSubJobResult, String> {

	@Query("select o from ScheduleSubJobResult as o left join o.scheduleJobResult as job where job.id = ?1")
	List<ScheduleSubJobResult> findAllSubJob(String jobId);

}
