package com.sjy.table.config;

import java.io.File;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class SqlQuery {
	public String summary;
	public String name, stmt, template, stat, title, statTitle, statOper,
			statDateTime;
	// 李炎 2012-06-07
	public boolean isCollect = true;
	public String method;
	public Boolean dynaStmt = false, dynaTitle = false, dynaStatTitle = false,
			dynaStatOper = false, dynaStatDateTime = false, dynaHint = false;
	public List<SqlOpt> options;
	public List<MethodParam> params;
	public List<SqlColumn> columns;
	public Map<String, SqlColumn> dimensions;
	public Map<String, SqlColumn> measures;
	public Map<String, Integer> columnIndexMap;
	public String[] union;
	public String fieldStr;
	public String orderby;

	public boolean isNative;
	public String extend;
	public String hint;

	File srcFile; // 来源文件，用来判断是否重复定义
}
