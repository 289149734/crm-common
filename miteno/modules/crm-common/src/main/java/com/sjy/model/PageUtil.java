package com.sjy.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

import com.sjy.util.StringUtil;

public class PageUtil {

	public static Pageable getPage(PageVo pageVo) {
		Pageable pageable = null;
		if (StringUtil.isNotBlank(pageVo.getSort())) {
			Sort sort = new Sort(Direction.fromStringOrNull(pageVo.getOrder()), pageVo.getSort());
			pageable = new PageRequest(pageVo.getPage(), pageVo.getLimit(), sort);
		} else {
			pageable = new PageRequest(pageVo.getPage(), pageVo.getLimit());
		}
		return pageable;
	}

	public static Map<String, Object> getSearchParams(HttpServletRequest request) {
		Map<String, Object> params = new HashMap<String, Object>(request.getParameterMap().size());
		for (Iterator<String> iter = request.getParameterMap().keySet().iterator(); iter.hasNext();) {
			String name = iter.next();
			String value = request.getParameter(name);
			if (StringUtil.isNotBlank(value)) {
				params.put(name, value);
			}
		}
		return params;
	}

}
