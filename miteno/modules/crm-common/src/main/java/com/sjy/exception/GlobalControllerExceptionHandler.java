/**
 * 
 */
package com.sjy.exception;

import java.sql.SQLException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.dubbo.rpc.RpcException;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.exceptions.JedisException;

/**
 * @Title: GlobalControllerExceptionHandler.java
 * @Package com.sjy.exception
 * @Description: Controller全局异常处理
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月28日 下午4:57:45
 * @version V1.0
 */
@Slf4j
@ControllerAdvice
public class GlobalControllerExceptionHandler {

	// 异常处理方法：
	// 根据特定的异常返回指定的 HTTP 状态码
	@ResponseStatus(value = HttpStatus.BAD_REQUEST) // 400
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public RestServiceError handleValidationException(ConstraintViolationException ex) {
		Set<ConstraintViolation<?>> errors = ex.getConstraintViolations();
		StringBuilder strBuilder = new StringBuilder();
		for (ConstraintViolation<?> violation : errors) {
			log.debug("{}", JSON.toJSONString(violation, true));
			strBuilder.append(violation.getMessage() + "\n");
		}
		log.debug("错误信息：{}", strBuilder.toString());
		return RestServiceError.build(CrmExceptionType.Customize_Error, strBuilder.toString());
	}

	// 通用异常的处理，返回500
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) // 500
	@ExceptionHandler(CrmException.class)
	@ResponseBody
	public RestServiceError handleException(CrmException ex) {
		log.error("【CrmException异常类型】-------------------", ex);
		if (ex.getCode() == CrmExceptionType.Customize_Error) {
			return RestServiceError.build(CrmExceptionType.Customize_Error, ex.getMessage());
		} else {
			return RestServiceError.build(ex.getCode(), "系统异常，请稍后再试");
		}
	}

	// 通用异常的处理，返回500
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) // 500
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public RestServiceError handleException(Exception ex) {
		// log.debug("{}-{}-{}-{}", ex.getClass(), (ex instanceof
		// InvalidFormatException),
		// (ex instanceof JsonMappingException), (ex instanceof
		// JsonProcessingException));
		log.error("【通用异常的处理】-------------------", ex);
		if (ex instanceof RpcException) {
			return RestServiceError.build(CrmExceptionType.Database_Error);
		} else if (ex instanceof JedisException) {
			return RestServiceError.build(CrmExceptionType.Database_Error);
		} else if (ex instanceof SQLException) {
			return RestServiceError.build(CrmExceptionType.Database_Error);
		} else if (ex instanceof HttpRequestMethodNotSupportedException) {
			return RestServiceError.build(CrmExceptionType.Api_Not_Exist);
		} else if ((ex instanceof HttpMessageConversionException) || (ex instanceof InvalidFormatException)
				|| (ex instanceof JsonMappingException) || (ex instanceof JsonProcessingException)) {
			return RestServiceError.build(CrmExceptionType.Api_Param_Error);
		} else {
			return RestServiceError.build(CrmExceptionType.System_Error);
		}
	}

}
