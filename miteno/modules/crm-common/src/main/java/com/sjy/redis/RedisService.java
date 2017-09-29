/**
 * 
 */
package com.sjy.redis;

import java.util.List;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2017年1月6日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
public interface RedisService {

	void remove(final String... keys);

	void removePattern(final String pattern);

	void remove(final String key);

	boolean exists(final String key);

	Object get(final String key);

	boolean set(final String key, Object value);

	boolean set(final String key, Object value, Long expireTime);

	/**
	 * 获取链接第一个数据，先进先出，后进后出
	 * 
	 * @param key
	 * @return
	 */
	Object popObj(final String key);

	Object popObjForSet(final String key);

	boolean pushObj(final String key, Object value);

	boolean pushObjForSet(final String key, Object value);

	boolean pushObjAllForSet(final String key, List<?> values);

	boolean pushObjAll(final String key, List<?> values);

	boolean pushObj(final String key, Object value, Long expireTime);

	boolean pushObjAll(final String key, List<?> values, Long expireTime);

	Long getSetSize(final String key);

	Long getListSize(final String key);
}