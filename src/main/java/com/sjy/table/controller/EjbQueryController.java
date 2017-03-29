/**
 * 
 */
package com.sjy.table.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sjy.model.PageVo;
import com.sjy.table.config.PageResult;
import com.sjy.table.service.EjbQueryService;
import com.sjy.util.StringUtil;

/**
 * 公共表单查询服务
 * 
 * @copyright(c) Copyright SJY Corporation 2017.
 * @since 2017年3月29日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
@RestController
@RequestMapping("/ejbQueryController")
public class EjbQueryController {

	@Resource
	EjbQueryService ejbQueryService;

	private Map<String, Object> getSearchParams(HttpServletRequest request) {
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

	@RequestMapping(value = "/list/{queryName}", method = RequestMethod.GET)
	public PageResult list(HttpServletRequest request, @PathVariable String queryName, PageVo pageVo) {
		try {
			String requestUri = request.getRequestURI();
			Map<String, Object> params = getSearchParams(request);
			log.debug("requestUri = {}, params = {}", requestUri, params);
			return ejbQueryService.selectData(queryName, params, null, pageVo.getOffset(), pageVo.getLimit(), null);
		} catch (Exception e) {
			log.error("查询数据错误：", e);
		}
		return null;
	}

}
