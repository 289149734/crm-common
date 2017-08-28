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
	private String code;
	private String message;

	public static RestServiceError build(Type errorType, String message) {
		RestServiceError error = new RestServiceError();
		error.code = errorType.getCode();
		error.message = message;
		return error;
	}

	public enum Type {
		BAD_REQUEST_ERROR("error.badrequest", "Bad request error"), 
		INTERNAL_SERVER_ERROR("error.internalserver", "Unexpected server error"), 
		VALIDATION_ERROR("error.validation", "Found validation issues");

		private String code;
		private String message;

		Type(String code, String message) {
			this.code = code;
			this.message = message;
		}

		public String getCode() {
			return code;
		}

		public String getMessage() {
			return message;
		}
	}
}
