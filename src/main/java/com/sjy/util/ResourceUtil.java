package com.sjy.util;

import java.util.HashSet;
import java.util.Set;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import lombok.extern.slf4j.Slf4j;

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

	public static Resource getFile(String location) {
		try {
			ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
			return resourcePatternResolver.getResource(location);
		} catch (Exception e) {
			log.error("读文件失败", e);
			return null;
		}
	}

	/**
	 * 扫描scanPackage返回包含的类
	 * 
	 * @param scanPackage
	 *            : "com.sjy.constant"
	 * @return
	 */
	public static Set<Class<?>> getClassByScanPackage(String scanPackage) {
		log.debug("{}", "classpath*:" + scanPackage.replace(".", "/") + "/*.class");
		org.springframework.core.io.Resource[] resources = ResourceUtil
				.getFiles("classpath*:" + scanPackage.replace(".", "/") + "/*.class");
		log.debug("数量：{}", resources.length);
		Set<Class<?>> list = new HashSet<Class<?>>(resources.length);
		for (org.springframework.core.io.Resource resource : resources) {
			String clsName = scanPackage + "." + resource.getFilename();
			// log.debug("{}", clsName.replace(".class", ""));
			Class<?> cls;
			try {
				cls = Class.forName(clsName.replace(".class", ""));
			} catch (ClassNotFoundException e) {
				log.error("不存在类：{}", clsName);
				continue;
			}
			// log.debug("{}", cls);
			list.add(cls);
		}
		return list;
	}
}
