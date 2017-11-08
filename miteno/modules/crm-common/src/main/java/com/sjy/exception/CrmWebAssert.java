/**
 * 
 */
package com.sjy.exception;

/**
 * @Title: WebAssert.java
 * @Package com.sjy.exception
 * @Description: 异常
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年10月30日 下午8:39:14
 * @version V1.0
 */
public class CrmWebAssert {

	public static void isTrue(boolean flag, String message) {
		if (flag) {
			throw new CrmException(message);
		}
	}

	public static void isFalse(boolean flag, String message) {
		if (!flag) {
			throw new CrmException(message);
		}
	}

	public static void isNull(Object obj, String message) {
		if (obj == null) {
			throw new CrmException(message);
		}
	}

	public static void notNull(Object obj, String message) {
		if (obj != null) {
			throw new CrmException(message);
		}
	}

}
