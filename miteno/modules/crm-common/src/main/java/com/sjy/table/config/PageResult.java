package com.sjy.table.config;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class PageResult implements Serializable {
	public int start, maxResult, totalElements, total;
	public int pageSize;// 总页数
	public List<Object[]> objects;
	public List<Map<String, Object>> content;
	public String title;
	public String summary;
	public Object[] stats; // 统计值
	public String statTitle;
	public String statOper; // 统计操作员
	public String statDateTime; // 统计时间
	public List<SqlColumn> columns;
	public boolean oversize = false;
	public String template = null;
	public String exportType = null;
	public String locale;
	int pageWidth = 595, pageHeight = 842;

}
