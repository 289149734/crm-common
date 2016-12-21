/**
 * 
 */
package com.sjy.task.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import com.sjy.task.domain.ScheduleJob;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Component
public interface ScheduleJobRepository extends CrudRepository<ScheduleJob, Long> {

	@Query("select o from ScheduleJob as o where o.jobStatus = 1")
	List<ScheduleJob> findNormalJobs();
}
