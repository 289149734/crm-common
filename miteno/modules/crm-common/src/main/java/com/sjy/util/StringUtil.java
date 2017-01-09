package com.sjy.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class StringUtil extends StringUtils {
	static final String HEX_FORMAT = "%02x";

	public static String bytesToHexStr(byte b[]) {
		return bytesToHexStr(b, 0, b.length);
	}

	public static String bytesToHexStr(byte b[], int start, int len) {
		if (start >= b.length || b == null || b.length == 0) return "";
		StringBuffer str = new StringBuffer();
		for (int i = start; i < start + len; i++) {
			str.append(String.format(HEX_FORMAT, b[i]));
		}
		return str.toString();
	}

	/**
	 * 将十六进制字符串转换为字节数组
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] hexStrToBytes(String str) {
		if (str.length() % 2 != 0) {
			str = "0" + str;
		}
		byte[] temp = new byte[str.length() / 2];
		for (int i = 0; i < str.length(); i = i + 2) {
			temp[i / 2] = (byte) (Byte.parseByte(str.substring(i, i + 1), 16) * 16 + Byte.parseByte(str.substring(i + 1, i + 2), 16));
		}
		return temp;
	}

	public static void hexStrToBytes(String str, byte[] b, int from) {
		if (str.length() % 2 != 0) {
			str = "0" + str;
		}
		hexStrToBytes(str, b, from, str.length() / 2);
	}

	/**
	 * 将十六进制字符串转换为字节数组
	 * 
	 * @param str
	 * @return
	 */
	public static void hexStrToBytes(String str, byte[] b, int from, int length) {
		if (str.length() % 2 != 0) {
			str = "0" + str;
		}
		for (int i = 0; i < Math.min(str.length(), length * 2) && (from + i / 2) < b.length; i = i + 2) {
			b[from + i / 2] = (byte) (Byte.parseByte(str.substring(i, i + 1), 16) * 16 + Byte.parseByte(str.substring(i + 1, i + 2), 16));
		}
	}

	/**
	 * 将hexStr转换成逆向的byte数组,特地为南开系统使用
	 * 
	 * @param str
	 * @return
	 */
	public static byte[] hexStrToInverseBytes(String str) {
		if (str.length() % 2 != 0) {
			str = "0" + str;
		}
		byte[] temp = new byte[str.length() / 2];
		for (int i = 0; i < str.length(); i = i + 2) {
			temp[temp.length - 1 - i / 2] = (byte) (Byte.parseByte(str.substring(i, i + 1), 16) * 16 + Byte.parseByte(
					str.substring(i + 1, i + 2), 16));
		}
		return temp;
	}

	/**
	 * 将逆向的byte数组转换成hexStr,特地为南开系统使用
	 * 
	 * @param str
	 * @return
	 */
	public static String inverseBytesToHexStr(byte b[], int start, int len) {
		StringBuffer str = new StringBuffer();
		for (int i = start + len - 1; i >= start; i--) {
			str.append(String.format(HEX_FORMAT, b[i]));
		}
		return str.toString();
	}

	public static String inverseBytesToHexStr(byte b[]) {
		int start = 0;
		int len = b.length;
		StringBuffer str = new StringBuffer();
		for (int i = start + len - 1; i >= start; i--) {
			str.append(String.format(HEX_FORMAT, b[i]));
		}
		return str.toString();
	}

	/**
	 * 
	 * @param i
	 *            数据
	 * @param len
	 *            转换后的字节数组长度
	 * @return
	 */
	public static byte[] intToHex(int i, int len) {
		return StringUtil.hexStrToBytes(StringUtil.lengthFix(Integer.toHexString(i), len * 2, '0', false));
	}

	/**
	 * int类型高低位调换
	 * 
	 * @param b
	 * @return
	 */
	public static String intToHexStr(int i) {
		String hexStr = StringUtil.lengthFix(Integer.toHexString(i), 8, '0', false);
		return StringUtil.bytesToHexStr(StringUtil.hexStrToInverseBytes(hexStr));
	}

	public static int hexStrToInt(String hexStr) {
		String _hexStr = StringUtil.bytesToHexStr(StringUtil.hexStrToInverseBytes(hexStr));
		return Integer.parseInt(_hexStr, 16);
	}

	/**
	 * 根据条件修剪String
	 * 
	 * @param text
	 *            原始数据
	 * @param length
	 *            需要的长度
	 * @param ch
	 *            添加的字符
	 * @param end
	 *            true在末尾，false在头部
	 * @return 符合要求的String
	 * 
	 *         20050301 update by guty 汉字算作长度为2
	 */
	public static String lengthFix(String text, int length, char ch, boolean end) {
		if (text == null) text = "";
		int tempLength = text.getBytes().length;
		if (length == tempLength) return text;

		if (length > tempLength) {
			char[] fix = new char[length - tempLength];
			for (int i = 0; i < fix.length; i++) {
				fix[i] = ch;
			}
			StringBuffer buffer = new StringBuffer(text);
			if (end) {
				buffer = buffer.append(fix);
			} else {
				buffer = buffer.insert(0, fix);
			}
			return buffer.toString();
		} else {
			if (end) return new String(text.getBytes(), 0, length);
			else return new String(text.getBytes(), tempLength - length, length);
		}
	}

	// for dyna language call
	public static String lengthFix(String text, int length, String ch, boolean end) {
		return lengthFix(text, length, ch.charAt(0), end);
	}

	public static int indexOfIgnoreCase(String source, String prefix) {
		int index = source.toUpperCase().indexOf(prefix.toUpperCase());
		if (index > 0) {
			return index + prefix.length();
		}
		return index;
	}

	public static String getBCC(byte[] data) {
		String ret = "";
		byte BCC[] = new byte[1];
		for (int i = 0; i < data.length; i++) {
			BCC[0] = (byte) (BCC[0] ^ data[i]);
		}
		String hex = Integer.toHexString(BCC[0] & 0xFF);
		if (hex.length() == 1) {
			hex = '0' + hex;
		}
		ret += hex.toUpperCase();
		return ret;
	}

	public static byte[] X_or(byte[] b1, int off1, byte[] b2, int off2, int len) {
		byte[] b3 = new byte[len];
		for (int i = 0; i < len; i++) {
			b3[i] = (byte) (b1[off1 + i] ^ b2[off2 + i]);
		}
		return b3;
	}

	public static String replaceByChar(String str, char ch, int start, int end) {
		int len = str.length();
		end = len - end;
		return str.substring(0, start) + lengthFix("", end - start, ch, false) + str.substring(end);
	}

	public static String replaceByChar(String str, String ch, int start, int end) {
		int len = str.length();
		end = len - end;
		return str.substring(0, start) + lengthFix("", end - start, ch.charAt(0), false) + str.substring(end);
	}

	public static String getHexIp(String ip) {
		StringBuffer sb = new StringBuffer();
		String[] ss = ip.split("\\.");
		for (String s : ss) {
			sb.append(String.format(HEX_FORMAT, Integer.valueOf(s)));
		}
		return sb.toString().toUpperCase();
	}

	public static void main(String[] args) throws Exception {
		byte[] bb = "李炎".getBytes("GBK");
		System.out.println(bytesToHexStr(bb, 0, bb.length > 5 ? 5 : bb.length));

		System.out.println(getHexIp("192.168.0.61"));
		System.out.println(getHexIp("255.255.255.0"));

		// System.out.println(getBCC("gg000000D0001710100".getBytes("GBK")));
		// System.out.println(getBCC("gg000000D0001720100".getBytes("GBK")));
		// System.out.println(getBCC("gg000000D00017201FE".getBytes("GBK")));
		// System.out.println(getBCC("ggg000000D0F0072".getBytes("GBK")));

		byte[] bytes = hexStrToBytes("");
		System.out.println(bytes.length);
		System.out.println(inverseBytesToHexStr(bytes, 0, bytes.length));
		String[] params = "jdbc:sqlserver://192.168.1.113:1433; DatabaseName=officeoa".split(";");
		for (String param : params) {
			int index = indexOfIgnoreCase(param, "DatabaseName=");
			if (index > 0) {
				System.out.println(param + ":" + index);
				System.out.println(param.substring(index));
			}
		}

		String[] array = new String[] { "a", "b", "c" };
		System.out.println(StringUtils.join(array, ", "));
		List<String> list = new ArrayList<String>();
		list.add("liyan1");
		list.add("liyan2");
		list.add("liyan3");
		System.out.println(StringUtils.join(list, ','));

		if ("08:54".compareTo("08:00") >= 0 && "08:54".compareTo("09:30") < 0) {
			System.out.println("&&&&&&&&&&&&&&&&&&&&&&&");
		}

		int i = 0x8000;
		String hex = intToHexStr(i);
		System.out.println(hex);
		int j = hexStrToInt(hex);
		System.out.println(j);

		for (int ii = 0; ii < 10; ii++) {
			String t = System.currentTimeMillis() + "";
			String eftSn = StringUtil.lengthFix(t, 6, '0', false);
			System.out.println(eftSn);
			Thread.sleep(10);
		}
	}
}
