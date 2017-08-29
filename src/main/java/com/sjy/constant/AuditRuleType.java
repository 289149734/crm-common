/**
 * 
 */
package com.sjy.constant;

import com.sjy.annotation.Dict;

/**
 * @Title: RuleDefinitionRuleType.java
 * @Package com.sjy.constant
 * @Description: 规则类型
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年8月29日 下午4:22:21
 * @version V1.0
 */
@Dict(name = "audit_rule_type")
public class AuditRuleType {
	public static final String CATEGORY = "audit_rule_type";

	@Dict(text = "启用规则")
	public static final int Enabled = 1;

	@Dict(text = "停用规则")
	public static final int Disabled = 2;

	@Dict(text = "暂停规则")
	public static final int Suspend = 3;

	@Dict(text = "重新启用规则")
	public static final int Re_enable = 4;

}
