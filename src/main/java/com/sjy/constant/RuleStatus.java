package com.sjy.constant;

import com.sjy.annotation.Dict;

/**
 * @Title: RuleStatus.java
 * @Package com.sjy.constant
 * @Description: 规则定义状态
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年6月22日 下午2:24:47
 * @version V1.0
 */
@Dict(name = "rule_status")
public final class RuleStatus {
	public static final String CATEGORY = "rule_status";

	@Dict(text = "运行")
	public static final int NORMAL = 1; // 运行

	@Dict(text = "等待审核")
	public static final int REQUEST = 2; // 等待审核

	@Dict(text = "审核未通过")
	public static final int DECLNED = 3; // 审核未通过

	@Dict(text = "停用")
	public static final int CANCELED = 4; // 停用

	@Dict(text = "草稿")
	public static final int DRAFT = 5; // 草稿

	@Dict(text = "暂停")
	public static final int SUSPEND = 6; // 暂停

}
