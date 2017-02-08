/**
 * 
 */
package com.sjy.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月19日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
public class BeanUtils extends BeanUtilsBean {
	@Override
	protected Object convert(Object value, Class<?> type) {
		return super.convert(value, type);
	}

	/**
	 * Converts a map to a JavaBean.
	 * 
	 * @param <T>
	 * 
	 * @param type
	 *            type to convert
	 * @param map
	 *            map to convert
	 * @return JavaBean converted
	 * @throws IntrospectionException
	 *             failed to get class fields
	 * @throws IllegalAccessException
	 *             failed to instant JavaBean
	 * @throws InstantiationException
	 *             failed to instant JavaBean
	 * @throws InvocationTargetException
	 *             failed to call setters
	 */
	public <T> T toBean(Class<T> type, Map<String, ? extends Object> map) {
		try {
			T obj = type.newInstance();
			BeanInfo beanInfo = Introspector.getBeanInfo(type);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (map.containsKey(propertyName)) {
					Object value = map.get(propertyName);
					Class<?> valType = descriptor.getPropertyType();
					value = convert(value, valType);
					descriptor.getWriteMethod().invoke(obj, value);
				}
			}
			return obj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Converts a JavaBean to a map.
	 * 
	 * @param bean
	 *            JavaBean to convert
	 * @return map converted
	 * @throws IntrospectionException
	 *             failed to get class fields
	 * @throws IllegalAccessException
	 *             failed to instant JavaBean
	 * @throws InvocationTargetException
	 *             failed to call setters
	 */
	public Map<String, Object> toMap(Object bean) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		try {
			BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++) {
				PropertyDescriptor descriptor = propertyDescriptors[i];
				String propertyName = descriptor.getName();
				if (!propertyName.equals("class")) {
					Method readMethod = descriptor.getReadMethod();
					Object result = readMethod.invoke(bean, new Object[0]);
					if (result != null) {
						returnMap.put(propertyName, result);
					} else {
						returnMap.put(propertyName, "");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnMap;
	}

	public static Map<String, String> bean2Map(Object obj) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BeanUtilsBean2 bub2 = new BeanUtilsBean2();
		return bub2.describe(obj);
	}
}
