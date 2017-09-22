/**
 * 
 */
package com.sjy.monitor;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 服务类监控
 * 
 * @copyright(c) Copyright SJY Corporation 2017.
 * 
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
		if (args == null)
			return;
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

	/**
	 * 监控com.sjy..*Service*.*(..))所有方法
	 */
	@Pointcut("execution(public * com.sjy..*Service*.*(..))")
	public void pointCutServiceMethod() {
	}

	@Around("pointCutServiceMethod()")
	public Object doAroundService(ProceedingJoinPoint joinPoint) throws Throwable {
		long t1 = System.currentTimeMillis();
		Object obj = joinPoint.proceed();
		long t2 = System.currentTimeMillis();
		log.debug("执行时间: {}>>>【Service】层-->{}_{}: {}", (t2 - t1), joinPoint.getTarget().getClass(),
				joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		return obj;
	}

	/**
	 * 监控com.sjy..*Repository.*(..))所有方法
	 */
	@Pointcut("execution(public * com.sjy..*Repository.*(..))")
	public void pointCutDaoMethod() {
	}

	@Around("pointCutDaoMethod()")
	public Object doAroundDao(ProceedingJoinPoint joinPoint) throws Throwable {
		long t1 = System.currentTimeMillis();
		Object obj = joinPoint.proceed();
		long t2 = System.currentTimeMillis();
		log.debug("执行时间: {}>>>【Repository】层-->{}_{}: {}", (t2 - t1), joinPoint.getTarget().getClass(),
				joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		return obj;
	}
}
