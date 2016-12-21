/**
 * 
 */
package com.sjy.task.service;

import java.util.Date;

import com.sjy.task.domain.ScheduleJob;

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
	Long initScheduleJob(ScheduleJob job, Date businessDate);

	/**
	 * 获取最后执行任务时间
	 * 
	 * @param job
	 * @return
	 */
	Date getLastJobDate(ScheduleJob job);

	/**
	 * 更新任务执行结果
	 * 
	 * @param id
	 * @param jobStatus
	 * @param errorMsg
	 * @return
	 */
	void updateScheduleJob(Long id, int jobStatus, Date finishDate, long costTimes, String errorMsg);
}
