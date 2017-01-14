/**
 * 
 */
package com.sjy.task.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sjy.task.domain.ScheduleJob;
import com.sjy.task.domain.ScheduleSubJobResult;

/**
 * ScheduleJobResult服务接口
 * 
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
public interface ScheduleJobService {

	/**
	 * 创建任务
	 * 
	 * @param job
	 * @param businessDate
	 * @return
	 */
	String initScheduleJob(ScheduleJob job, Date businessDate);

	/**
	 * 创建子任务
	 * 
	 * @param scheduleJobResultId
	 * @param params
	 */
	void intiScheduleSubJob(String scheduleJobResultId, Map<String, String> params);

	/**
	 * 获取最后执行任务时间
	 * 
	 * @param job
	 * @return
	 */
	Date getLastJobDate(ScheduleJob job);

	/**
	 * 获取最后执行任务时间(月)
	 * 
	 * @param job
	 * @return
	 */
	Date getLastJobMonth(ScheduleJob job);

	/**
	 * 更新任务执行结果
	 * 
	 * @param id
	 * @param jobStatus
	 * @param errorMsg
	 * @return
	 */
	void updateScheduleJob(String id, int jobStatus, Date finishDate, long costTimes, String errorMsg);

	/**
	 * 查询所有子任务
	 * 
	 * @param jobId
	 * @return
	 */
	List<ScheduleSubJobResult> getAllSubJob(String jobId);
}
