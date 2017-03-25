/**
 * 
 */
package com.sjy.monitor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 加载拦截器主类
 * 
 * @copyright(c) Copyright SJY Corporation 2017.
 * @since 2017年3月13日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Configuration
public class WebMvcConfigurer extends WebMvcConfigurerAdapter {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoginHandlerInterceptor()).addPathPatterns("/**");
		super.addInterceptors(registry);
	}

}
