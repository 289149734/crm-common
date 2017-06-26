package com.sjy.util;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

/**
 * @Title: ResourceUtil.java
 * @Package com.sjy.util
 * @Description: 读取资源文件工具类
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年6月22日 下午3:30:46
 * @version V1.0
 */
@Slf4j
public class ResourceUtil {

	public static Resource[] getFiles(String locationPattern) {
		try {
			ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
			return resourcePatternResolver.getResources(locationPattern);
		} catch (Exception e) {
			log.error("读文件失败", e);
			return null;
		}
	}

}
