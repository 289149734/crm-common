/**
 * 
 */
package com.sjy.monitor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**");
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
		log.info("设置ObjectMapper【FAIL_ON_EMPTY_BEANS】 = false");
		return new ObjectMapper().disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
	}

}
