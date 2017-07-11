package com.sjy.table.config;

import java.io.Serializable;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SqlColumn implements Serializable {
	public final static String ALIGN_RIGHT = "right";
	public final static String ALIGN_LEFT = "left";
	public String title, value, width, align;
	public Boolean hidden;
	public String field;
	public SqlColumn overrideDim; // union子查询使用的field
	public String dict, format;
	public String dimensionUsage, calculation; // 统计字段用到
	public Dimension parentDimension; // 如果此sqlcolumn是sharedDimension的一个level
	public String name;
	public String dimensionJoin;
	String srcFileName; // 来源文件，用来判断是否重复定义 ,refColumn用
	public MeasureTitle parentTitle; // measure的parentTitle
	public String[] parentTitles;
	public int[] parentTitlesSpans; // 纵向span
	public String type;// 通过设置导出excel单元格类型：number,date,string(default)

	public SqlColumn clone() {
		SqlColumn column = new SqlColumn();
		column.align = align;
		column.title = title;
		column.value = value;
		column.hidden = hidden;
		column.field = field;
		column.name = this.name;
		column.format = this.format;
		column.dict = this.dict;
		column.type = this.type;
		return column;
	}

}
