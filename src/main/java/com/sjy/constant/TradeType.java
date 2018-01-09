package com.sjy.constant;

import java.util.ArrayList;
import java.util.List;

import com.sjy.annotation.Dict;

/**
 * 本类提供TradeType字典的常量定义
 * 
 * @copyright(c) Copyright SJY Corporation 2016.
 * 
 * @since 2016年12月19日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Dict(name = "trade_type")
public class TradeType {

	public static final int CASH_IN = 100; // 柜面充值
	public static final int WEB_IN = 101; // 网上充值
	public static final int BANK_IN = 102; // 银行卡充值
	
	@Dict(text = "微信公众号充值")
	public static final int WEI_XIN = 168; // 微信公众号充值
	public static final int ALI_PAY = 169; // 支付宝收银台充值
	public static final int H5_IN = 162; // 微信收银台充值
	public static final int APP_IN = 163; // APP收银台充值
	public static final int POS_IN = 164; // 传统POS收银台充值
	public static final int PC_IN = 165; // PC收银台充值
	public static final int ONLINE_WEIXIN_IN = 166; // 微信公众号充值

	public static final int ACCUMULATE = 103; // 累积（后台自动充值）
	public static final int RET_CLIENT = 104; // 其他//黑名单卡发生消费，中石油返还给客户
	public static final int TOPUP_RETURN_CASH = 105; // 充值返现

	public static final int CASH_OUT = 110; // 退现金
	public static final int REFUND = 111; // 账户内退款
	public static final int EXPIRE = 113; // 失效
	public static final int CLEAR = 114; // 销户清零

	public static final int CARD_CONSUME = 150; // 终端消费
	public static final int CASH_CONSUME = 152; // 现金消费
	public static final int ALLIANCE_CONSUME = 149; // 营销联盟消费
	public static final int BANK_CONSUME = 153; // 银行卡消费
	public static final int ALI_CONSUME = 148; // 支付宝消费
	public static final int WX_CONSUME = 154; // 微信消费
	@Dict(text = "微信商城消费")
	public static final int ONLINE_CONSUME = 157; // 微信商城消费
	public static final int H5_CONSUME = 158; // 虚拟终端消费[暂不用]
	public static final int COUNTER_CONSUME = 159; // 柜面消费
	@Dict(text = "微信收银台消费")
	public static final int CONSUME_H5 = 181; // 微信收银台消费
	@Dict(text = "APP收银台消费")
	public static final int CONSUME_APP = 182; // APP收银台消费
	@Dict(text = "传统POS收银台消费")
	public static final int CONSUME_POS = 183; // 传统POS收银台消费
	public static final int CONSUME_PC = 184; // PC收银台消费
	public static final int CONSUME_ONLINE_WEIXIN = 185; // 线上公众号消费[暂不用]

	public static final int REVERSE_CONSUME = 151; // 冲正

	public static final int POINT_EXCHANGE = 155; // 积分兑换[线下]
	public static final int POINT_EXCHANGE_ONLINE = 160; // 积分兑换[线上]

	public static final int COUPON_CONSUME = 156; // 优惠券核销[线下]
	public static final int COUPON_CONSUME_ONLINE = 161; // 优惠券核销[线上]

	public static final int TRANSFER_IN = 120; // 转帐(进)
	public static final int TRANSFER_OUT = 121; // 转帐(出)

	public static final int SMS_PAY = 250; // 消费短信提醒扣取年费交易类型
	public static final int FEE_RECORD = 400; // 佣金费用

	public static final int TIMESCARD_CONSUME = 401; // 次卡核销[线下]
	public static final int TIMESCARD_CONSUME_ONLINE = 402; // 次卡核销[线上]

	/**
	 * 获取所有消费交易类型
	 * 
	 * @return
	 */
	public static List<Integer> consume() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(CARD_CONSUME);
		list.add(CASH_CONSUME);
		list.add(ALLIANCE_CONSUME);
		list.add(BANK_CONSUME);
		list.add(ALI_CONSUME);
		list.add(WX_CONSUME);
		list.add(ONLINE_CONSUME);
		list.add(H5_CONSUME);
		list.add(CONSUME_H5);
		list.add(COUNTER_CONSUME);
		list.add(CONSUME_APP);
		list.add(CONSUME_POS);
		list.add(CONSUME_PC);
		list.add(CONSUME_ONLINE_WEIXIN);
		return list;
	}

	/**
	 * 获取所有充值交易类型
	 * 
	 * @return
	 */
	public static List<Integer> topup() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(CASH_IN);
		list.add(WEB_IN);
		list.add(BANK_IN);
		list.add(WEI_XIN);
		list.add(ALI_PAY);
		list.add(H5_IN);
		list.add(APP_IN);
		list.add(POS_IN);
		list.add(PC_IN);
		list.add(ONLINE_WEIXIN_IN);
		return list;
	}

	/**
	 * 获取所有优惠券核销交易类型
	 * 
	 * @return
	 */
	public static List<Integer> coupon() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(COUPON_CONSUME);
		list.add(COUPON_CONSUME_ONLINE);
		return list;
	}
}