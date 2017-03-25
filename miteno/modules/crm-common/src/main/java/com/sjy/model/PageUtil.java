package com.sjy.model;

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

}
