/**
 * 
 */
package com.sjy.task;

import lombok.extern.slf4j.Slf4j;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.sjy.task.dao.ScheduleJobRepository;
import com.sjy.task.domain.ScheduleJob;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
@Component
public class MyScheduler {
	@Autowired
	SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	ScheduleJobRepository scheduleJobRepository;

	public void scheduleJobs() throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		startJobs(scheduler);
	}

	private void startJobs(Scheduler scheduler) throws Exception {
		Iterable<ScheduleJob> jobs = scheduleJobRepository.findNormalJobs();
		for (ScheduleJob job : jobs) {
			if (job.getJob() == null) continue;

			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobName(), job.getJobGroup());
			// 获取trigger，即在spring配置文件中定义的 bean id="myTrigger"
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

			if (trigger == null) {
				JobDetail jobDetail = JobBuilder.newJob(job.getJob().getClass()).withIdentity(job.getJobName(), job.getJobGroup()).build();
				jobDetail.getJobDataMap().put("scheduleJob", job);

				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

				// 按新的cronExpression表达式构建一个新的trigger
				trigger = TriggerBuilder.newTrigger().withIdentity(job.getJobName(), job.getJobGroup()).withSchedule(scheduleBuilder).build();

				scheduler.scheduleJob(jobDetail, trigger);
			} else {
				// Trigger已存在，那么更新相应的定时设置
				// 表达式调度构建器
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());

				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();

				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(triggerKey, trigger);
			}

			log.debug("【{}-{}】加载成功", job.getJobDesc(), job.getJobName());
		}
	}
}
