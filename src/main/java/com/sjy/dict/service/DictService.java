package com.sjy.dict.service;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import com.sjy.dict.dao.DictionaryRepository;
import com.sjy.dict.domain.Dictionary;
import com.sjy.redis.RedisService;
import com.sjy.util.StringUtil;

/**
 * 字典service类
 * 
 * @copyright(c) Copyright GD Corporation 2007.
 * @author gutianyang
 * @since 0.2
 */
@Component
public class DictService {

	public final static String PRFIX_DICT = "DICT_";
	public static final String DICT_CACHE = "DICTCACHE";

	@Resource
	RedisService redisService;

	@Resource
	DictionaryRepository dictionaryRepository;

	public void save(List<Dictionary> entities) {
		dictionaryRepository.save(entities);
	}

	/**
	 * 取得字典项文本
	 * 
	 * @param category
	 *            字典类型
	 * @param code
	 *            字典代码
	 * @return 字典项文本
	 */
	public String getText(String category, int code) {
		try {
			category = category.toUpperCase();
			String key = DICT_CACHE + ":" + category + ":" + code;
			String text = (String) redisService.get(key.toUpperCase());
			if (StringUtil.isNotBlank(text))
				return text.trim();

			List<Dictionary> list = dictionaryRepository
					.findByCategory(category);
			for (Dictionary obj : list) {
				key = DICT_CACHE + ":" + category + ":" + obj.getCode();
				redisService.set(key.toUpperCase(), obj.getText());
				if (code == obj.getCode()) {
					text = obj.getText();
				}
			}
			return text.trim();
		} catch (Exception e) {
			return "" + code;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Dictionary> findAll(String category) {
		List<Dictionary> dicts = null;
		category = category.toUpperCase();
		String key = DICT_CACHE + ":" + category;
		if (redisService.exists(key)) {
			dicts = (List<Dictionary>) redisService.get(key);
		} else {
			dicts = dictionaryRepository.findByCategory(category);
			redisService.set(key, dicts);
		}
		if (dicts == null) {
			dicts = Collections.EMPTY_LIST;
		}
		return dicts;
	}
}
