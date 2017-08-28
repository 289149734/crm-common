/**
 * 
 */
package com.sjy.exception;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.alibaba.fastjson.JSON;

import lombok.extern.slf4j.Slf4j;

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
		return RestServiceError.build(RestServiceError.Type.VALIDATION_ERROR, strBuilder.toString());
	}

	// 通用异常的处理，返回500
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) // 500
	@ExceptionHandler(CrmException.class)
	@ResponseBody
	public RestServiceError handleException(CrmException ex) {
		return RestServiceError.build(RestServiceError.Type.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

	// 通用异常的处理，返回500
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR) // 500
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public RestServiceError handleException(Exception ex) {
		log.debug("【异常类型】-------------------..{}", ex.toString());
		return RestServiceError.build(RestServiceError.Type.INTERNAL_SERVER_ERROR, ex.getMessage());
	}

}
