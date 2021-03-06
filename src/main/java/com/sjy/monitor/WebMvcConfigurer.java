/**
 * 
 */
package com.sjy.monitor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.sjy.interceptor.LoginHandlerInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 加载拦截器主类
 * 
 * @copyright(c) Copyright SJY Corporation 2017.
 * 
 * @since 2017年3月13日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

	@Bean
	public LoginHandlerInterceptor loginHandlerInterceptor() {
		return new LoginHandlerInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginHandlerInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

	/**
	 * org.springframework.http.converter.HttpMessageNotWritableException: Could not
	 * write content: No serializer found for class
	 * com.google.common.cache.CacheStats and no properties discovered to create
	 * BeanSerializer (to avoid exception, disable
	 * SerializationFeature.FAIL_ON_EMPTY_BEANS)
	 * 
	 * @return
	 */
	@Bean
	public ObjectMapper objectMapper() {
		log.info(
				"设置ObjectMapper-->>FAIL_ON_EMPTY_BEANS = false, DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES = false");
		return new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	}

	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		// return new CookieHttpSessionStrategy();
		return new HeaderHttpSessionStrategy();
	}
}
