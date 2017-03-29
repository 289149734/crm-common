package com.sjy.table.config;

import lombok.Data;

@Data
public class MeasureTitle {
	public String name;
	public MeasureTitle parent;
	public String title;
	public int level;
	public String tKey;
}
