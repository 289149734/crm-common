/**
 * 
 */
package com.sjy.task;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.ResourceUtils;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
@Configuration
public class SchedulerListener implements
		ApplicationListener<ContextRefreshedEvent> {

	public final static String fileName = "classpath*:quartz.properties";

	@Autowired
	public MyScheduler myScheduler;

	@Autowired
	public MyJobFactory myJobFactory;

	public Properties quartzProperties() {
		try {
			File quartzFile = ResourceUtils.getFile(fileName);
			Properties cfg = new Properties();
			cfg.load(new FileInputStream(quartzFile));
			log.debug("加载{}文件成功", fileName);
			for (Object key : cfg.keySet()) {
				log.debug("{} = {}", key, cfg.get(key));
			}
			return cfg;
		} catch (Exception e) {
			log.debug("加载{}文件失败", fileName);
			return null;
		}
	}

	@Bean
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setJobFactory(myJobFactory);
		Properties cfg = quartzProperties();
		if (cfg != null)
			schedulerFactoryBean.setQuartzProperties(cfg);
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
