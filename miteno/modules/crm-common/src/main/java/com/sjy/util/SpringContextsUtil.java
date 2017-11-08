package com.sjy.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * @Title: SpringContextsUtil.java
 * @Package com.sjy.util
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年11月6日 下午8:52:26
 * @version V1.0
 */
@Component
public class SpringContextsUtil implements ApplicationContextAware {

	private ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public Object getBean(String beanName) {
		return applicationContext.getBean(beanName);
	}

	public <T> T getBean(String beanName, Class<T> clazs) {
		return clazs.cast(getBean(beanName));
	}

}
