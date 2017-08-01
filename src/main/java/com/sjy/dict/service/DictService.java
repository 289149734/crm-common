package com.sjy.dict.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.sjy.dict.dao.DictionaryRepository;
import com.sjy.dict.domain.Dictionary;
import com.sjy.redis.RedisService;
import com.sjy.util.BeanUtils;
import com.sjy.util.StringUtil;

/**
 * 字典service类
 * 
 * @copyright(c) Copyright GD Corporation 2007.
 * @author gutianyang
 * @since 0.2
 */
@Slf4j
@Component
public class DictService {

	public static final String DICT_CACHE = "DICTCACHE";

	@Resource
	RedisService redisService;

	@Resource
	DictionaryRepository dictionaryRepository;

	public void save(List<Dictionary> entities) {
		dictionaryRepository.save(entities);
		initToRedis();
	}

	public Dictionary save(Dictionary entity) {
		Dictionary entityDb = entity;
		if (StringUtil.isNotBlank(entity.getId())) {
			entityDb = dictionaryRepository.findOne(entity.getId());
			BeanUtils.copyBeanNotNull2Bean(entity, entityDb);
		}
		entity = dictionaryRepository.save(entityDb);
		initToRedis();
		return entity;
	}

	public void delete(List<String> dictIds) {
		dictIds.forEach(dictId -> {
			dictionaryRepository.delete(dictId);
		});
		initToRedis();
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
		List<Dictionary> list = findAll(category);
		String text = "" + code;
		for (Dictionary dict : list) {
			if (code == dict.getCode()) {
				text = dict.getText();
				break;
			}
		}
		return text.trim();
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

	public void initToRedis() {
		long t1 = System.currentTimeMillis();
		Map<String, List<Dictionary>> map = new HashMap<String, List<Dictionary>>();
		List<Dictionary> dicts = dictionaryRepository.findAll();
		dicts.forEach(dict -> {
			String key = DICT_CACHE + ":"
					+ dict.getCategory().trim().toUpperCase();
			List<Dictionary> list = map.get(key);
			if (list == null)
				list = new ArrayList<Dictionary>();
			list.add(dict);
			map.put(key, list);
		});

		map.forEach((k, v) -> {
			redisService.set(k, v);
		});
		long t2 = System.currentTimeMillis();
		log.debug("-------加载字典[{}]项成功,耗时:{}(ms)-------", map.size(), (t2 - t1));
	}
}
