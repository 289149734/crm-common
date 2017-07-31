package com.sjy.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sjy.dict.service.DictService;

/**
 * @Title: DictionaryConfiguration.java
 * @Package com.sjy.config
 * @Description: 系统字典初始化加载
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年7月31日 下午4:50:17
 * @version V1.0
 */
@Configuration
public class DictionaryConfiguration {

	@Bean(initMethod = "initToRedis")
	public DictService initDict() {
		return new DictService();
	}

}
