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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.BeanUtilsBean2;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * 
 * @since 2016年12月19日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
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
					if (value instanceof String && value != null) {
						value = ((String) value).trim();
					}
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

	public List<Map<String, Object>> toList(List<? extends Object> list) {
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		for (Object obj : list) {
			data.add(toMap(obj));
		}
		return data;
	}

	public static Map<String, String> bean2Map(Object obj)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BeanUtilsBean2 bub2 = new BeanUtilsBean2();
		Map<String, String> result = bub2.describe(obj);
		result.remove("class");
		return result;
	}

	public static Map<String, String> bean2Map(Object obj, boolean hasNull)
			throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		BeanUtilsBean2 bub2 = new BeanUtilsBean2();
		Map<String, String> result = bub2.describe(obj);
		result.remove("class");
		if (!hasNull) {
			Map<String, String> resultNoNull = new HashMap<String, String>();
			result.forEach((key, val) -> {
				if (StringUtil.isNotBlank(val)) {
					resultNoNull.put(key, val);
				}
			});
			return resultNoNull;
		}
		return result;
	}

	/**
	 * 
	 * @param databean
	 * @param tobean
	 */
	public static void copyBeanNotNull2Bean(Object databean, Object tobean) {
		PropertyDescriptor origDescriptors[] = PropertyUtils.getPropertyDescriptors(databean);
		for (int i = 0; i < origDescriptors.length; i++) {
			String name = origDescriptors[i].getName();
			// String type = origDescriptors[i].getPropertyType().toString();
			if ("class".equals(name)) {
				continue; // No point in trying to set an object's class
			}
			if (PropertyUtils.isReadable(databean, name) && PropertyUtils.isWriteable(tobean, name)) {
				try {
					Object value = PropertyUtils.getSimpleProperty(databean, name);
					if (value != null) {
						// copyProperty(tobean, name, value);
						PropertyUtils.setProperty(tobean, name, value);
					}
				} catch (java.lang.IllegalArgumentException ie) {
					; // Should not happen
				} catch (Exception e) {
					; // Should not happen
				}

			}
		}
	}

	public void clearAllProperties(final Object obj) throws IllegalAccessException, InvocationTargetException {

		// Validate existence of the specified beans
		if (obj == null) {
			throw new IllegalArgumentException("No destination bean specified");
		}
		if (log.isDebugEnabled()) {
			log.debug("BeanUtils.clearAllProperties(" + obj + ")");
		}

		// Copy the properties, converting as necessary
		if (obj instanceof DynaBean) {
			final DynaProperty[] origDescriptors = ((DynaBean) obj).getDynaClass().getDynaProperties();
			for (DynaProperty origDescriptor : origDescriptors) {
				final String name = origDescriptor.getName();
				// Need to check isReadable() for WrapDynaBean
				// (see Jira issue# BEANUTILS-61)
				if (getPropertyUtils().isWriteable(obj, name)) {
					copyProperty(obj, name, null);
				}
			}
		} else if (obj instanceof Map) {
			@SuppressWarnings("unchecked")
			final// Map properties are always of type <String, Object>
			Map<String, Object> propMap = (Map<String, Object>) obj;
			for (final Map.Entry<String, Object> entry : propMap.entrySet()) {
				final String name = entry.getKey();
				if (getPropertyUtils().isWriteable(obj, name)) {
					copyProperty(obj, name, null);
				}
			}
		} else /* if (orig is a standard JavaBean) */ {
			final PropertyDescriptor[] origDescriptors = getPropertyUtils().getPropertyDescriptors(obj);
			for (PropertyDescriptor origDescriptor : origDescriptors) {
				final String name = origDescriptor.getName();
				if ("class".equals(name)) {
					continue; // No point in trying to set an object's class
				}
				if (getPropertyUtils().isWriteable(obj, name)) {
					copyProperty(obj, name, null);
				}
			}
		}

	}
}
