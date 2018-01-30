package com.sjy.dict.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.sjy.annotation.Dict;
import com.sjy.dict.dao.DictionaryRepository;
import com.sjy.dict.domain.Dictionary;
import com.sjy.redis.RedisService;
import com.sjy.util.BeanUtils;
import com.sjy.util.ResourceUtil;
import com.sjy.util.StringUtil;

import lombok.extern.slf4j.Slf4j;

/**
 * 字典service类
 * 
 * @copyright(c) Copyright GD Corporation 2007.
 * 
 * @author gutianyang
 * @since 0.2
 */
@Slf4j
@Service
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

	/**
	 * 取得字典项文本
	 * 
	 * @param category
	 *            字典类型
	 * @param code
	 *            字典代码
	 * @return 字典项文本
	 */
	public Integer getCodeByText(String category, String text) {
		if (StringUtil.isBlank(text)) {
			return null;
		}
		List<Dictionary> list = findAll(category);
		for (Dictionary dict : list) {
			if (text.trim() == dict.getText()) {
				return dict.getCode();
			}
		}
		return null;
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

	@Transactional(value = TxType.REQUIRES_NEW)
	public void loadFromConstant() {
		List<Dictionary> dicts = new ArrayList<Dictionary>();

		Set<Class<?>> list = ResourceUtil.getClassByScanPackage("com.sjy.constant");
		list.forEach(cls -> {
			if (cls.isAnnotationPresent(Dict.class)) {
				log.debug("Class Name = {}", cls.getName());
				Dict d = cls.getAnnotation(Dict.class);
				log.debug("Dict name = {}", d.name());

				Field[] fields = cls.getFields();
				for (Field field : fields) {
					if (field.isAnnotationPresent(Dict.class)) {
						Dict df = field.getAnnotation(Dict.class);
						int val = 0;
						try {
							Object obj = field.get(cls);
							if (obj instanceof Integer) {
								val = ((Integer) obj).intValue();
							} else if (obj instanceof Long) {
								val = ((Long) obj).intValue();
							} else {
								val = field.getInt(cls);
							}
						} catch (Exception e) {
							log.error("Dict text = {}[{}]", df.text(), "空");
							continue;
						}
						log.debug("Dict text = {}[{}]", df.text(), val);

						List<Dictionary> dictListInDb = dictionaryRepository
								.findByCategoryAndCode(d.name().toUpperCase(), val);
						if (dictListInDb == null || dictListInDb.size() == 0) {
							Dictionary dict = new Dictionary(d.name().toUpperCase(), val, df.text(), df.editable());
							dicts.add(dict);
						}
					}
				}
			}
		});

		log.debug(">>>>>>>>>{}", JSON.toJSONString(dicts, true));
		if (dicts.size() > 0) {
			dictionaryRepository.save(dicts);
		}
	}

	public void initToRedis() {
		// TODO 判断是否需要初始化
		boolean needInit = true;
		if (needInit) {
			long t1 = System.currentTimeMillis();
			// 加载所有字典项
			loadFromConstant();

			Map<String, List<Dictionary>> map = new HashMap<String, List<Dictionary>>();
			List<Dictionary> dicts = dictionaryRepository.findAll();
			dicts.forEach(dict -> {
				String key = DICT_CACHE + ":" + dict.getCategory().trim().toUpperCase();
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
}
