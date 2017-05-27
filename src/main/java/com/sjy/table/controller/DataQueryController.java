/**
 * 
 */
package com.sjy.table.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.sjy.model.PageUtil;
import com.sjy.model.PageVo;
import com.sjy.table.config.PageResult;
import com.sjy.table.service.DataQueryService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 公共表单查询服务
 * 
 * @copyright(c) Copyright SJY Corporation 2017.
 * 
 * @since 2017年3月29日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Slf4j
@RestController
@RequestMapping("/d")
public class DataQueryController {

	@Resource
	DataQueryService dataQueryService;

	@ApiOperation(value = "查询列表信息", notes = "通过编号查询列表信息")
	@RequestMapping(value = "/list/{queryName}", method = RequestMethod.GET)
	@ResponseBody
	public PageResult list(HttpServletRequest request, @PathVariable String queryName, PageVo pageVo) {
		try {
			String requestUri = request.getRequestURI();
			Map<String, Object> params = PageUtil.getSearchParams(request);
			log.debug("requestUri = {}, params = {}", requestUri, params);
			return dataQueryService.selectData(queryName, params, null, pageVo.getOffset(), pageVo.getLimit(), null);
		} catch (Exception e) {
			log.error("查询数据错误：", e);
		}
		return null;
	}

}
