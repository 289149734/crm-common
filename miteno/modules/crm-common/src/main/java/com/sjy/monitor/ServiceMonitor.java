/**
 * 
 */
package com.sjy.monitor;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * 服务类监控
 * 
 * @copyright(c) Copyright SJY Corporation 2017.
 * @since 2017年2月24日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
@Aspect
@Component
public class ServiceMonitor {

	@AfterThrowing(throwing = "ex", value = "execution(public * com.sjy..*ServiceImpl.*(..))")
	public void handlerException(JoinPoint joinPoint, Throwable ex) {
		log.error("+++++++++++++++++++++: " + joinPoint);
		log.error("+++++++++++++++++++++: " + ex);
	}
}
