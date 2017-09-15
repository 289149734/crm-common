package com.sjy.util;

import java.security.MessageDigest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashUtil {

	public static String sha1(String data) {
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(data.getBytes("UTF-8"));
			return StringUtil.bytesToHexStr(crypt.digest());
		} catch (Exception e) {
			log.error("计算SHA1错误", e);
		}
		return null;
	}

}
