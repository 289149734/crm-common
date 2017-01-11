/**
 * 
 */
package com.sjy.task.service.impl;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.stereotype.Component;

import com.sjy.task.constant.ScheduleJobStatus;
import com.sjy.task.dao.ScheduleJobResultRepository;
import com.sjy.task.domain.ScheduleJob;
import com.sjy.task.domain.ScheduleJobResult;
import com.sjy.task.service.ScheduleJobService;

/**
 * ScheduleJobResult服务类
 * 
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Component("scheduleJobService")
public class ScheduleJobServiceImpl implements ScheduleJobService {

	@Resource
	ScheduleJobResultRepository scheduleJobResultRepository;

	@Transactional(value = TxType.REQUIRES_NEW)
	@Override
	public String initScheduleJob(ScheduleJob job, Date businessDate) {
		ScheduleJobResult result = new ScheduleJobResult();
		result.setJob(job);
		result.setRecordDate(new Date());
		result.setJobStatus(ScheduleJobStatus.INIT);
		result.setBusinessDate(businessDate);
		result = scheduleJobResultRepository.save(result);
		return result.getId();
	}

	@Override
	public Date getLastJobDate(ScheduleJob job) {
		Date date = scheduleJobResultRepository.findLastJobDate(job);
		if (date == null) {
			Calendar c = Calendar.getInstance();
			c.set(2016, 6, 1, 0, 0, 0); // 设置系统启用时间
			date = c.getTime();
		}
		return date;
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	@Override
	public void updateScheduleJob(String id, int jobStatus, Date finishDate, long costTimes, String errorMsg) {
		scheduleJobResultRepository.updateJob(id, finishDate, costTimes, jobStatus, errorMsg);
	}

}
