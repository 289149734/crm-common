package com.sjy.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import lombok.extern.slf4j.Slf4j;

/**
 * xml工具类
 * 
 * @author miklchen
 * 
 */
@Slf4j
public class XMLUtil {

	/*
	 * public static void main(String[] args) throws JDOMException, IOException {
	 * String s =
	 * "<?xml version=\"1.0\" encoding=\"utf-8\" ?><root><token_id>14c720994062372cddb305de7120a795</token_id></root>"
	 * ; Map<String, String> m = XMLUtil.doXMLParse(s);
	 * 
	 * // String s2 = //
	 * "<?xml version=\"1.0\" encoding=\"utf-8\" ?><root><err_info>[23500901]您的操作已提交，请确认是否已生效。</err_info></root>"
	 * ;
	 * 
	 * String weixinPrepay = "<xml><return_code><![CDATA[SUCCESS]]></return_code>" +
	 * "<return_msg><![CDATA[OK]]></return_msg>" +
	 * "<appid><![CDATA[wxb512901288a94943]]></appid>" +
	 * "<mch_id><![CDATA[1220321501]]></mch_id>" +
	 * "<nonce_str><![CDATA[Kg8ZgDGx3Lhs4uo1]]></nonce_str>" +
	 * "<sign><![CDATA[4B26F5376CE8EFF690D68619E0A0EA61]]></sign>" +
	 * "<result_code><![CDATA[SUCCESS]]></result_code>" +
	 * "<prepay_id><![CDATA[wx201501182317239609f8cec70033211771]]></prepay_id>" +
	 * "<trade_type><![CDATA[JSAPI]]></trade_type>" + "</xml>"; Map<String, String>
	 * payMap = XMLUtil.doXMLParse(weixinPrepay);
	 * 
	 * Map<String, String> payMap2 = XMLUtil.doXMLParse(weixinPrepay); }
	 */

	/**
	 * Converter Map<Object, Object> instance to xml string. Note: currently, we
	 * aren't consider more about some collection types, such as array,list,
	 * 
	 * @param dataMap
	 *            the data map
	 * @return the string
	 */
	public static String converterWeixinpayXML(Map<String, String> dataMap) {
		synchronized (XMLUtil.class) {
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append("<?xml version='1.0' encoding='UTF-8' ?>");
			strBuilder.append("<xml>");
			Set<String> objSet = dataMap.keySet();
			for (Object key : objSet) {
				if (key == null) {
					continue;
				}
				strBuilder.append("<").append(key.toString()).append(">");
				Object value = dataMap.get(key);
				strBuilder.append(coverter(value).trim());
				strBuilder.append("</").append(key.toString()).append(">");
			}
			strBuilder.append("</xml>");
			return strBuilder.toString();
		}
	}

	/**
	 * Converter Map<Object, Object> instance to xml string. Note: currently, we
	 * aren't consider more about some collection types, such as array,list,
	 * 
	 * @param dataMap
	 *            the data map
	 * @return the string
	 */
	public static String converter(Map<Object, Object> dataMap, String xmlRootStart, String xmlRootEnd) {
		synchronized (XMLUtil.class) {
			StringBuilder strBuilder = new StringBuilder();
			strBuilder.append(xmlRootStart);
			Set<Object> objSet = dataMap.keySet();
			for (Object key : objSet) {
				if (key == null) {
					continue;
				}
				strBuilder.append("\n");
				strBuilder.append("<").append(key.toString()).append(">\n");
				Object value = dataMap.get(key);
				strBuilder.append(coverter(value));
				strBuilder.append("</").append(key.toString()).append(">\n");
			}
			strBuilder.append(xmlRootEnd);
			return strBuilder.toString();
		}
	}

	public static String coverter(Object[] objects) {
		StringBuilder strBuilder = new StringBuilder();
		for (Object obj : objects) {
			strBuilder.append("<item className=").append(obj.getClass().getName()).append(">\n");
			strBuilder.append(coverter(obj));
			strBuilder.append("</item>\n");
		}
		return strBuilder.toString();
	}

	public static String coverter(Collection<?> objects) {
		StringBuilder strBuilder = new StringBuilder();
		for (Object obj : objects) {
			strBuilder.append("<item className=").append(obj.getClass().getName()).append(">\n");
			strBuilder.append(coverter(obj));
			strBuilder.append("</item>\n");
		}
		return strBuilder.toString();
	}

	/**
	 * Coverter.
	 * 
	 * @param object
	 *            the object
	 * @return the string
	 */
	public static String coverter(Object object) {
		if (object instanceof Object[]) {
			return coverter((Object[]) object);
		}
		if (object instanceof Collection) {
			return coverter((Collection<?>) object);
		}
		StringBuilder strBuilder = new StringBuilder();
		if (isObject(object)) {
			Class<? extends Object> clz = object.getClass();
			Field[] fields = clz.getDeclaredFields();

			for (Field field : fields) {
				field.setAccessible(true);
				String fieldName = field.getName();
				Object value = null;
				try {
					value = field.get(object);
				} catch (IllegalArgumentException e) {
					continue;
				} catch (IllegalAccessException e) {
					continue;
				}
				strBuilder.append("<").append(fieldName).append(" className=\"").append(value.getClass().getName())
						.append("\">\n");
				if (isObject(value)) {
					strBuilder.append(coverter(value));
				} else {
					strBuilder.append(value.toString() + "\n");
				}
				strBuilder.append("</").append(fieldName).append(">\n");
			}
		} else if (object == null) {
			strBuilder.append("null\n");
		} else {
			strBuilder.append(object.toString() + "\n");
		}
		return strBuilder.toString();
	}

	/**
	 * Checks if is object.
	 * 
	 * @param obj
	 *            the obj
	 * 
	 * @return true, if is object
	 */
	private static boolean isObject(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			return false;
		}
		if (obj instanceof Integer) {
			return false;
		}
		if (obj instanceof Double) {
			return false;
		}
		if (obj instanceof Float) {
			return false;
		}
		if (obj instanceof Byte) {
			return false;
		}
		if (obj instanceof Long) {
			return false;
		}
		if (obj instanceof Character) {
			return false;
		}
		if (obj instanceof Short) {
			return false;
		}
		if (obj instanceof Boolean) {
			return false;
		}
		return true;
	}

	/**
	 * 解析xml,返回第一级元素键值对。如果第一级元素有子节点，则此节点的值是子节点的xml数据。
	 * 
	 * @param strxml
	 * @return
	 * @throws JDOMException
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> doXMLParse(String strxml) throws IOException, DocumentException {
		Map<String, String> m = new HashMap<String, String>();
		if (null == strxml || "".equals(strxml)) {
			return null;
		}

		Document doc = DocumentHelper.parseText(strxml);
		Element root = doc.getRootElement();
		@SuppressWarnings("unchecked")
		List<Element> list = root.elements();
		Iterator<Element> it = list.iterator();
		while (it.hasNext()) {
			Element e = (Element) it.next();
			String k = e.getName();
			String v = "";
			@SuppressWarnings("unchecked")
			List<Element> children = e.elements();
			if (children.isEmpty()) {
				v = e.getTextTrim();
			} else {
				v = XMLUtil.getChildrenText(children);
			}

			m.put(k, v);
		}
		return m;
	}

	/**
	 * 获取子结点的xml
	 * 
	 * @param children
	 * @return String
	 */
	public static String getChildrenText(List<Element> children) {
		StringBuffer sb = new StringBuffer();
		if (!children.isEmpty()) {
			Iterator<Element> it = children.iterator();
			while (it.hasNext()) {
				Element e = it.next();
				String name = e.getName();
				String value = e.getTextTrim();
				@SuppressWarnings("unchecked")
				List<Element> list = e.elements();
				sb.append("<" + name + ">");
				if (!list.isEmpty()) {
					sb.append(XMLUtil.getChildrenText(list));
				}
				sb.append(value);
				sb.append("</" + name + ">");
			}
		}

		return sb.toString();
	}

	/**
	 * 获取xml编码字符集
	 * 
	 * @param strxml
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 * @throws JDOMException
	 */
	public static String getXMLEncoding(String strxml) throws IOException, DocumentException {
		Document doc = DocumentHelper.parseText(strxml);
		return doc.getXMLEncoding();
	}

	public static String toXml(Object obj) {
		// 创建输出流
		StringWriter sw = new StringWriter();
		try {
			// 利用jdk中自带的转换类实现
			JAXBContext context = JAXBContext.newInstance(obj.getClass());

			Marshaller marshaller = context.createMarshaller();
			// 省略头信息
			marshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);
			// 格式化xml输出的格式
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			// 将对象转换成输出流形式的xml
			marshaller.marshal(obj, sw);
		} catch (Exception e) {
			log.error("Bean转换Xml失败", e);
		}
		return sw.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T toObject(Class<T> clsName, String xml) {
		try {
			// 利用jdk中自带的转换类实现
			JAXBContext context = JAXBContext.newInstance(clsName);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			return (T) unmarshaller.unmarshal(new StringReader(xml));
		} catch (Exception e) {
			log.error("Xml转换Bean失败", e);
		}
		return null;
	}
}
