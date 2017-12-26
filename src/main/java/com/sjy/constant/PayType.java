/**
 * 
 */
package com.sjy.constant;

import com.sjy.annotation.Dict;

/**
 * @Title: PayType.java
 * @Package com.sjy.constant
 * @Description: TODO(用一句话描述该文件做什么)
 * @author liyan
 * @email 289149734@qq.com
 * @date 2017年10月27日 下午9:19:27
 * @version V1.0
 */
@Dict(name = "pay_type", editable = true)
public class PayType {

	@Dict(text = "现金支付")
	public static final int CASH_PAY = 1;
	@Dict(text = "银行卡支付")
	public static final int BANK_PAY = 3; // 银行卡支付
	@Dict(text = "积分累积")
	public static final int OTHER_PAY = 4; // 积分累积
	@Dict(text = "充值返现")
	public static final int PAYMENT_TYPE_TOPUP_RETURNCASH = 7; // 充值返现
	@Dict(text = "微信支付")
	public static final int WEI_XIN = 9; // 微信支付
	@Dict(text = "支付宝支付")
	public static final int ALIPAY = 10; // 支付宝支付
	@Dict(text = "会员卡支付")
	public static final int MEMBER_CARD = 11; // 会员卡支付
	@Dict(text = "联盟支付")
	public static final int ALLIANCE = 5; // 联盟支付
	@Dict(text = "易付宝支付")
	public static final int SuNing = 17; // 易付宝支付
	@Dict(text = "会员积分支付")
	public static final int MEMBER_POINT = 18; // 会员积分支付

}
