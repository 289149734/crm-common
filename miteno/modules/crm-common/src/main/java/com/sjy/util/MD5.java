package com.sjy.util;

import java.security.MessageDigest;

/**
 * User: rizenguo Date: 2014/10/23 Time: 15:43
 */
public class MD5 {
	private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	/**
	 * 转换字节数组为16进制字串
	 * 
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	public static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (byte aB : b) {
			resultSb.append(byteToHexString(aB));
		}
		return resultSb.toString();
	}

	/**
	 * 转换byte到16进制
	 * 
	 * @param b
	 *            要转换的byte
	 * @return 16进制格式
	 */
	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * MD5编码
	 * 
	 * @param origin
	 *            原始字符串
	 * @return 经过MD5加密之后的结果
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = origin;
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(resultString.getBytes("UTF-8"));
			resultString = byteArrayToHexString(md.digest());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultString;
	}

	public static void main(String[] args) {
		String str = "appid=wx433b05bbcc5778da&body=支付集成测试&detail=支付集成测试&fee_type=CNY&mch_id=1275820201&nonce_str=402881e855497dc60155573c88c10006&notify_url=http://vchat.paysys.cn/JWechat/rest/weixinPayController/notify/weixinpay/402881e854354dc70154354f91ab0001&openid=oBBEBwZh-y4LhdG7MrjCjWH01gIM&out_trade_no=018614163140063218257&spbill_create_ip=8.8.8.8&total_fee=29200&trade_type=JSAPI&key=587e1720d40f44d6c781a2600dc8fe61";
		System.out.println(MD5Encode(str));
	}
}
