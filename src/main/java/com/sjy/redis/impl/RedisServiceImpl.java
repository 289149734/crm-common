/**
 * 
 */
package com.sjy.redis.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import com.sjy.redis.RedisService;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * 
 * @since 2017年1月6日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Service("redisService")
public class RedisServiceImpl implements RedisService {

	@Resource
	RedisTemplate<Serializable, Object> redisTemplate;

	/**
	 * 批量删除对应的value
	 * 
	 * @param keys
	 */
	@Override
	public void remove(final String... keys) {
		for (String key : keys) {
			remove(key);
		}
	}

	/**
	 * 批量删除key
	 * 
	 * @param pattern
	 */
	@Override
	public void removePattern(final String pattern) {
		Set<Serializable> keys = redisTemplate.keys(pattern);
		if (keys.size() > 0)
			redisTemplate.delete(keys);
	}

	/**
	 * 删除对应的value
	 * 
	 * @param key
	 */
	@Override
	public void remove(final String key) {
		if (exists(key)) {
			redisTemplate.delete(key);
		}
	}

	/**
	 * 判断缓存中是否有对应的value
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public boolean exists(final String key) {
		return redisTemplate.hasKey(key);
	}

	/**
	 * 读取缓存
	 * 
	 * @param key
	 * @return
	 */
	@Override
	public Object get(final String key) {
		Object result = null;
		ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
		result = operations.get(key);
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public boolean set(final String key, Object value) {
		boolean result = false;
		try {
			redisTemplate.opsForValue().set(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 写入缓存
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Override
	public boolean set(final String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			redisTemplate.opsForValue().set(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Object popObj(final String key) {
		return redisTemplate.opsForList().leftPop(key);
	}

	@Override
	public Object popObjForSet(final String key) {
		return redisTemplate.opsForSet().pop(key);
	}

	@Override
	public boolean pushObj(String key, Object value) {
		boolean result = false;
		try {
			ListOperations<Serializable, Object> list = redisTemplate.opsForList();
			list.rightPush(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean pushObjForSet(String key, Object value) {
		boolean result = false;
		try {
			SetOperations<Serializable, Object> list = redisTemplate.opsForSet();
			list.add(key, value);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean pushObjForSet(String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			SetOperations<Serializable, Object> list = redisTemplate.opsForSet();
			list.add(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean pushObjAllForSet(String key, List<?> values) {
		boolean result = false;
		try {
			SetOperations<Serializable, Object> list = redisTemplate.opsForSet();
			for (Object val : values) {
				list.add(key, val);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean pushObjAll(String key, List<?> values) {
		boolean result = false;
		try {
			ListOperations<Serializable, Object> list = redisTemplate.opsForList();
			list.rightPushAll(key, values);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean pushObj(String key, Object value, Long expireTime) {
		boolean result = false;
		try {
			ListOperations<Serializable, Object> list = redisTemplate.opsForList();
			list.rightPush(key, value);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public boolean pushObjAll(String key, List<?> values, Long expireTime) {
		boolean result = false;
		try {
			ListOperations<Serializable, Object> list = redisTemplate.opsForList();
			list.rightPushAll(key, values);
			redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public Long getSetSize(String key) {
		return redisTemplate.opsForSet().size(key);
	}

	@Override
	public Long getListSize(String key) {
		return redisTemplate.opsForList().size(key);
	}

	@Override
	public Set<Serializable> keys(String pattern) {
		return redisTemplate.keys(pattern);
	}
}
