/**
 * 
 */
package com.sjy.constant;

import java.util.Map;

/**
 * 系统接口直接响应结果
 * 
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2017年1月17日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
public class Response {

	public final static String CODE = "responseCode";
	public final static String MSG = "responseMsg";

	public static Map<String, Object> putSuccessResult(Map<String, Object> result, String msg) {
		result.put(CODE, "00");
		result.put(MSG, msg);
		return result;
	}

	public static Map<String, Object> putFailureResult(Map<String, Object> result, String msg) {
		result.put(CODE, "01");
		result.put(MSG, msg);
		return result;
	}

	public static Map<String, Object> putFailureResult(Map<String, Object> result, String code, String msg) {
		result.put(CODE, code);
		result.put(MSG, msg);
		return result;
	}

}
