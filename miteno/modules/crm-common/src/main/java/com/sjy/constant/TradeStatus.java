/**
 * 
 */
package com.sjy.constant;

/**
 * 交易状态
 * 
 * @copyright(c) Copyright SJY Corporation 2016.
 * 
 * @since 2017年1月12日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
public class TradeStatus {
	public static final int NORMAL = 1; // 正常(交易状态)
	public static final int REVERSAL = 2; // 冲正
	public static final int REVERSED = 3; // 被冲正
	public static final int TEMP = 4; // 临时交易
	public static final int CANCEL = 5; // 撤销
	public static final int CANCELED = 6; // 被撤销
	public static final int BOOKING = 7; // 预约
	public static final int INVALIDITY = 8; // 失效
	public static final int CANCEL_TEMP = 9; // 临时撤销交易
	public static final int REFUND = 10; // 退货
	public static final int FAIL = 11; // 失败
	public static final int CANCEL_FAIL = 12; // 撤销失败
}
