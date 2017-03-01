/**
 * 
 */
package com.sjy.monitor;

import lombok.extern.slf4j.Slf4j;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

	/**
	 * 执行方法前打印所有参数
	 * 
	 * @param joinPoint
	 */
	@Before(value = "execution(public * com.sjy..*ServiceImpl.*(..))")
	public void logAllArgValues(JoinPoint joinPoint) {
		Signature signature = joinPoint.getSignature();
		log.debug("执行{}_{}传入参数如下:", signature.getDeclaringTypeName(), signature.getName());
		Object[] args = joinPoint.getArgs();
		if (args == null) return;
		int i = 1;
		for (Object arg : args) {
			log.debug("传入参数[{}]...........{}", i++, arg);
		}
	}

	@AfterThrowing(throwing = "ex", value = "execution(public * com.sjy..*ServiceImpl.*(..))")
	public void handlerException(JoinPoint joinPoint, Throwable ex) {
		Signature signature = joinPoint.getSignature();
		log.debug("执行{}_{}抛出异常：{}", signature.getDeclaringTypeName(), signature.getName(), ex);
	}

	@AfterReturning(value = "execution(public * com.sjy..*ServiceImpl.*(..))", argNames = "obj", returning = "obj")
	public void logReturnValue(JoinPoint joinPoint, Object obj) {
		Signature signature = joinPoint.getSignature();
		log.debug("执行{}_{}返回参数：{}", signature.getDeclaringTypeName(), signature.getName(), obj);
	}
}
