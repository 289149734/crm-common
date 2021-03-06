/**
 * 
 */
package com.sjy.table.controller;

import io.swagger.annotations.ApiOperation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.sjy.dict.domain.Dictionary;
import com.sjy.dict.service.DictService;
import com.sjy.model.PageUtil;
import com.sjy.model.PageVo;
import com.sjy.table.config.PageResult;
import com.sjy.table.config.TableMeta;
import com.sjy.table.service.DataQueryService;
import com.sjy.util.StringUtil;

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
@RequestMapping("/api")
public class DataQueryController {

	@Resource
	DataQueryService dataQueryService;

	@Resource
	DictService dictService;

	@SuppressWarnings("unchecked")
	@ApiOperation(value = "查询字典项", notes = "通过编号查询字典项信息")
	@GetMapping(value = "/dict/{dictName}")
	@ResponseBody
	public List<Dictionary> getDict(@PathVariable String dictName) {
		try {
			return dictService.findAll(dictName);
		} catch (Exception e) {
			log.error("查询数据错误：", e);
		}
		return Collections.EMPTY_LIST;
	}

	@ApiOperation(value = "删除字典项", notes = "删除字典项信息")
	@DeleteMapping(value = "/dict")
	@ResponseBody
	public void deleteDicts(@RequestParam("dictIds") List<String> dictIds) {
		try {
			dictService.delete(dictIds);
		} catch (Exception e) {
			log.error("删除数据错误：", e);
		}
	}

	@ApiOperation(value = "新增字典项", notes = "新增字典项信息")
	@PostMapping(value = "/dict")
	@ResponseBody
	public Dictionary saveDicts(@Valid @RequestBody Dictionary dict) {
		try {
			return dictService.save(dict);
		} catch (Exception e) {
			log.error("删除数据错误：", e);
		}
		return null;
	}

	@ApiOperation(value = "更新字典项", notes = "更新字典项信息")
	@PutMapping(value = "/dict")
	@ResponseBody
	public Dictionary updateDicts(@Valid @RequestBody Dictionary dict) {
		try {
			return dictService.save(dict);
		} catch (Exception e) {
			log.error("删除数据错误：", e);
		}
		return null;
	}

	@ApiOperation(value = "查询列表头", notes = "通过编号查询列表信息")
	@GetMapping(value = "/columns/{queryName}")
	@ResponseBody
	public TableMeta listHeader(HttpServletRequest request,
			@PathVariable String queryName) {
		try {
			Map<String, Object> params = PageUtil.getSearchParams(request);
			TableMeta tm = dataQueryService.getTableMeta(queryName, params);
			return tm;
		} catch (Exception e) {
			log.error("查询数据错误：", e);
		}
		return null;
	}

	@ApiOperation(value = "查询列表信息", notes = "通过编号查询列表信息")
	@GetMapping(value = "/list/{queryName}")
	@ResponseBody
	public PageResult list(HttpServletRequest request,
			@PathVariable String queryName, PageVo pageVo) {
		try {
			Map<String, Object> params = PageUtil.getSearchParams(request);
			log.debug("PageVo = {}", JSON.toJSONString(pageVo, false));
			String orderBy = null;
			if (StringUtil.isNotBlank(pageVo.getSort())) {
				orderBy = pageVo.getSort() + " " + pageVo.getOrder();
			}
			return dataQueryService.selectData(queryName, params, orderBy,
					pageVo.getOffset(), pageVo.getLimit(), null);
		} catch (Exception e) {
			log.error("查询数据错误：", e);
			PageResult pr = new PageResult();
			pr.start = pageVo.getOffset();
			pr.maxResult = pageVo.getLimit();
			pr.totalElements = 0;
			return pr;
		}
	}

}
