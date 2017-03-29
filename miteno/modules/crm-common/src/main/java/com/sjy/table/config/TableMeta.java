package com.sjy.table.config;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

@Data
@SuppressWarnings("serial")
public class TableMeta implements Serializable {
	String title;
	List<SqlColumn> columns;
}
