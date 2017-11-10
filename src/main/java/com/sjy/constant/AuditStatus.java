package com.sjy.constant;

import com.sjy.annotation.Dict;

/**
 * 审核类型
 * 
 * @copyright(c) Copyright GD Corporation 2007.
 * 
 * @author gutianyang
 * @author wangyuguo
 * @since 0.2
 */
@Dict(name = "audit_status")
public class AuditStatus {
	public static final String CATEGORY = "audit_status";

	@Dict(text = "初始")
	public static final int DRAFT = 5;
	@Dict(text = "等待审核")
	public static final int OPEN = 1;
	@Dict(text = "审核通过")
	public static final int PASSED = 2;
//	@Dict(text = "通过")
//	public static final int REALPASSED = 3;
	@Dict(text = "未通过")
	public static final int REJECTED = 11;
	@Dict(text = "撤销")
	public static final int CANCELED = 12;

}