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

	@Dict(text = "充值[线下]")
	public static final int RECAHRGE = 100;
	@Dict(text = "充值[线上]")
	public static final int RECAHRGE_ONLINE = 101;

	@Dict(text = "消费[线下]")
	public static final int CONSUME = 159;
	@Dict(text = "消费[线上]")
	public static final int CONSUME_ONLINE = 157;

	public static final int CASH_IN = 100; // 柜面充值
	public static final int WEB_IN = 101; // 网上充值
	public static final int BANK_IN = 102; // 银行卡充值
	public static final int WEI_XIN = 168; // 微信支付充值
	public static final int ALI_PAY = 169; // 支付宝支付充值

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
	public static final int ONLINE_CONSUME = 157; // 消费[线上]
	public static final int H5_CONSUME = 158; // 虚拟终端消费
	public static final int COUNTER_CONSUME = 159; // 柜面消费

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
		list.add(CONSUME);
		list.add(CONSUME_ONLINE);
		return list;
	}

	/**
	 * 获取所有充值交易类型
	 * 
	 * @return
	 */
	public static List<Integer> topup() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(RECAHRGE);
		list.add(RECAHRGE_ONLINE);
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