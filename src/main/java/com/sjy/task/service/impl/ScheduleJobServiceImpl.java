/**
 * 
 */
package com.sjy.task.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.stereotype.Component;

import com.sjy.constant.JobStatus;
import com.sjy.task.constant.ScheduleJobStatus;
import com.sjy.task.dao.ScheduleJobResultRepository;
import com.sjy.task.dao.ScheduleSubJobResultRepository;
import com.sjy.task.domain.ScheduleJob;
import com.sjy.task.domain.ScheduleJobResult;
import com.sjy.task.domain.ScheduleSubJobResult;
import com.sjy.task.service.ScheduleJobService;
import com.sjy.util.DateUtils;

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

	@Resource
	ScheduleSubJobResultRepository scheduleSubJobResultRepository;

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

	@Transactional(value = TxType.REQUIRES_NEW)
	@Override
	public void intiScheduleSubJob(String scheduleJobResultId, Map<String, String> params) {
		ScheduleJobResult result = scheduleJobResultRepository.findOne(scheduleJobResultId);
		List<ScheduleSubJobResult> entities = new ArrayList<ScheduleSubJobResult>();
		for (String key : params.keySet()) {
			ScheduleSubJobResult subResult = new ScheduleSubJobResult();
			subResult.setScheduleJobResult(result);
			subResult.setJobStatus(JobStatus.INIT);
			subResult.setRecordDate(new Date());
			subResult.setTaskName(key);
			subResult.setBusinessData(params.get(key));
			entities.add(subResult);
		}
		scheduleSubJobResultRepository.save(entities);
	}

	@Override
	public Date getLastJobDate(ScheduleJob job) {
		Date date = scheduleJobResultRepository.findLastJobDate(job);
		if (date == null) {
			Calendar c = Calendar.getInstance();
			c.set(2016, 6, 1, 0, 0, 0); // 设置系统启用时间
			c.set(Calendar.MILLISECOND, 0);
			date = c.getTime();
		} else {
			date = DateUtils.getNextDay(date);
		}
		return date;
	}

	@Override
	public Date getLastJobMonth(ScheduleJob job) {
		Date date = scheduleJobResultRepository.findLastJobDate(job);
		if (date == null) {
			Calendar c = Calendar.getInstance();
			c.set(2016, 6, 1, 0, 0, 0); // 设置系统启用时间
			c.set(Calendar.MILLISECOND, 0);
			date = c.getTime();
		} else {
			date = DateUtils.getNextMonthDay(date);
		}
		return date;
	}

	@Transactional(value = TxType.REQUIRES_NEW)
	@Override
	public void updateScheduleJob(String id, int jobStatus, Date finishDate, long costTimes, String errorMsg) {
		scheduleJobResultRepository.updateJob(id, finishDate, costTimes, jobStatus, errorMsg);
	}

	@Override
	public List<ScheduleSubJobResult> getAllSubJob(String jobId) {
		return scheduleSubJobResultRepository.findAllSubJob(jobId);
	}

}
