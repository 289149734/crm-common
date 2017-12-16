/**
 * 
 */
package com.sjy.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: CrmExceptionType.java
 * @Package com.sjy.exception
 * @Description: 错误编码
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年9月22日 下午4:27:26
 * @version V1.0
 */
public class CrmExceptionType {

	public static Map<Integer, String> exceptionMap = new HashMap<Integer, String>();
	public final static int Login_Timeoue = 100;
	public final static int Database_Error = 101;
	public final static int Api_Not_Exist = 102;
	public final static int Api_Param_Error = 103;
	public final static int Dubbo_Service_Error = 104;
	public final static int Redis_Error = 105;
	public final static int Customize_Error = 998;
	public final static int System_Error = 999;

	static {
		exceptionMap.put(Login_Timeoue, "登录超时, 请重新登录");
		exceptionMap.put(Database_Error, "数据库操作异常");
		exceptionMap.put(Api_Not_Exist, "该功能API不存在或者异常");
		exceptionMap.put(Api_Param_Error, "API参数格式错误");
		exceptionMap.put(Dubbo_Service_Error, "系统服务异常");
		exceptionMap.put(Redis_Error, "Redis服务异常");
		exceptionMap.put(Customize_Error, "系统自定义异常");
		exceptionMap.put(System_Error, "系统异常");
	}

	public static String getMsg(int code) {
		String msg = exceptionMap.get(code);
		if (msg == null) {
			msg = "系统未定义异常";
		}
		return msg;
	}
}
