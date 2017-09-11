package com.sjy.encrypt;

import com.sjy.util.StringUtil;

public final class EncryptText {
	public static String ROOT_KEY = "2004200320022001";

	public static byte[] encrypt(byte[] encryptKey) {
		return Des.encrypt(ROOT_KEY.getBytes(), encryptKey);
	}

	public static byte[] decrypt(byte[] encryptKey) {
		return Des.decrypt(ROOT_KEY.getBytes(), encryptKey);
	}

	public static String encrypt(String encryptKey) {
		return StringUtil.bytesToHexStr(Des.encrypt(ROOT_KEY, encryptKey));
	}

	public static String decrypt(String encryptKey) {
		return Des.decrypt(ROOT_KEY, StringUtil.hexStrToBytes(encryptKey)).trim();
	}

	public static byte[] getSignKey(String tripDesKey) {
		try {
			String key = StringUtil.bytesToHexStr(tripDesKey.getBytes("utf-8")).toUpperCase();
			String key1 = key.substring(2, 10);
			String key2 = key.substring(5, 13);
			String key3 = key.substring(10, 18);
			String key4 = key.substring(20, 28);
			String sessionKey = key2 + key4 + key1 + key3;
			return StringUtil.hexStrToInverseBytes(sessionKey);
		} catch (Exception e) {
			return null;
		}
	}
}
