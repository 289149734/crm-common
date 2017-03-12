/**
 * 
 */
package com.sjy.util;

import java.io.File;

import lombok.extern.slf4j.Slf4j;

/**
 * @copyright(c) Copyright SJY Corporation 2017.
 * @since 2017年3月9日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
public class FileUtils extends org.apache.commons.io.FileUtils {

	/**
	 * 获取文件名称[不含后缀名] 不去掉文件目录的空格
	 * 
	 * @param
	 * @return String
	 */
	public static String getFilePrefix2(String fileName) {
		int splitIndex = fileName.lastIndexOf(".");
		return fileName.substring(0, splitIndex);
	}

	public static boolean delete(String strFileName) {
		File fileDelete = new File(strFileName);

		if (!fileDelete.exists() || !fileDelete.isFile()) {
			log.info("错误: " + strFileName + "不存在!");
			return false;
		}

		log.info("--------成功删除文件---------" + strFileName);
		return fileDelete.delete();
	}
}
