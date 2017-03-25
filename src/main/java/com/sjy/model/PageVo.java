/**
 * 
 */
package com.sjy.model;

import lombok.Data;

/**
 * @copyright(c) Copyright SJY Corporation 2017.
 * @since 2017年3月17日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Data
public class PageVo {
	int offset;
	int limit;
	String sort;
	String order;

	public int getPage() {
		return offset / limit;
	}
}
