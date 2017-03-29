package com.sjy.table.config;

import lombok.Data;
import groovy.lang.Script;

@Data
public class SqlOpt {
	public String name, cond, type;
	public Script dynaCond;
}
