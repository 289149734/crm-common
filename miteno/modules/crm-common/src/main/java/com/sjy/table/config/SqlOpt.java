package com.sjy.table.config;

import lombok.Data;

@Data
public class SqlOpt {
	public String name, cond, type;
	public Boolean dynaCond = false;
}
