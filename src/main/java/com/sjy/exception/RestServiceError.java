/**
 * 
 */
package com.sjy.exception;

import lombok.Data;

/**
 * @Title: RestServiceError.java
 * @Package com.sjy.exception
 * @Description: 错误实体
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月28日 下午4:59:25
 * @version V1.0
 */
@Data
public class RestServiceError {
	private int code;
	private String message;

	public static RestServiceError build(int errorType, String message) {
		RestServiceError error = new RestServiceError();
		error.code = errorType;
		if (message == null) {
			error.message = CrmExceptionType.getMsg(errorType);
		} else {
			error.message = message;
		}
		return error;
	}

	public static RestServiceError build(int errorType) {
		RestServiceError error = new RestServiceError();
		error.code = errorType;
		error.message = CrmExceptionType.getMsg(errorType);
		return error;
	}

}
