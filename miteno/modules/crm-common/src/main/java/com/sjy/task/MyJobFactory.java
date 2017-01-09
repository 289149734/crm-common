/**
 * 
 */
package com.sjy.task;

import java.lang.reflect.Method;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;
import org.springframework.stereotype.Component;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月21日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Component
public class MyJobFactory extends AdaptableJobFactory {
	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;

	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		// 调用父类的方法
		Object jobInstance = super.createJobInstance(bundle);

		// 进行注入
		capableBeanFactory.autowireBean(jobInstance);

		// 执行初始化方法
		Method initMethod = jobInstance.getClass().getMethod("initTask");
		if (initMethod != null) {
			initMethod.invoke(jobInstance);
		}

		return jobInstance;
	}
}
