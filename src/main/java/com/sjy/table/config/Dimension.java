package com.sjy.table.config;

import java.io.File;
import java.util.List;

import lombok.Data;

@Data
public class Dimension {
	public String name, entity, primaryKey;
	public List<SqlColumn> levels;

	File srcFile; // 来源文件，用来判断是否重复定义
}