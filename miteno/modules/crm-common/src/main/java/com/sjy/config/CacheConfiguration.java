package com.sjy.config;

import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import com.sjy.util.ResourceUtil;

/**
 * 缓存配置 --标注启动缓存.
 * 
 * @author liyan
 *
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheConfiguration {
	/**
	 * ehcache主要的管理器
	 * 
	 * @param bean
	 * @return
	 */
	@Bean("cacheManager")
	public EhCacheCacheManager ehCacheCacheManager(
			EhCacheManagerFactoryBean bean) {
		return new EhCacheCacheManager(bean.getObject());
	}

	/**
	 * 
	 * 据shared与否的设置, Spring分别通过CacheManager.create() 或new
	 * CacheManager()方式来创建一个ehcache基地.
	 * 也说是说通过这个来设置cache的基地是这里的Spring独用,还是跟别的(如hibernate的Ehcache共享)
	 */
	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
		EhCacheManagerFactoryBean cacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		Resource[] ehcacheFiles = ResourceUtil
				.getFiles("classpath*:ehcache.xml");
		if (ehcacheFiles != null && ehcacheFiles.length >= 0) {
			log.debug(
					"-----------------------EhCacheManagerFactoryBean Init {}-----------------------------------------",
					ehcacheFiles[0].getFilename());
			cacheManagerFactoryBean.setConfigLocation(ehcacheFiles[0]);
		}
		cacheManagerFactoryBean.setShared(true);
		return cacheManagerFactoryBean;
	}
}
