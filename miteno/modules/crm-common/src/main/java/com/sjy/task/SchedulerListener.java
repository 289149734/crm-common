/**
 * 
 */
package com.sjy.task;

import lombok.extern.log4j.Log4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Configuration
@Log4j
public class SchedulerListener implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	public MyScheduler myScheduler;

	@Autowired
	public MyJobFactory myJobFactory;

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setJobFactory(myJobFactory);
		return schedulerFactoryBean;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		try {
			log.debug("开始启动自动任务监听...............");
			myScheduler.scheduleJobs();
			log.debug("启动自动任务监听成功...............");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
