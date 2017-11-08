package com.sjy.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FormatUtil {
	final static int MULTIPLIER = 100;

	/**
	 * 分转换为元.
	 * 
	 * @param fen
	 *            分
	 * @return 元
	 */
	public static String fromFenToYuan(final String fen) {
		String yuan = "";
		Pattern pattern = Pattern.compile("^[1-9][0-9]*{1}");
		Matcher matcher = pattern.matcher(fen);
		if (matcher.matches()) {
			yuan = new BigDecimal(fen).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
		} else {
			log.error("参数格式不正确!");
		}
		return yuan;
	}

	/**
	 * 分->元
	 * 
	 * @param amount
	 * @return
	 */
	public static String fenToYuan(long amount) {
		return BigDecimal.valueOf(amount).divide(new BigDecimal(MULTIPLIER)).setScale(2).toString();
	}

	/**
	 * 分->元
	 * 
	 * @param amount
	 * @return
	 */
	public static Long yuanToFen(String amount) {
		return new BigDecimal(amount).max(new BigDecimal(MULTIPLIER)).longValue();
	}
}
