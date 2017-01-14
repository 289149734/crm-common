/**
 * 
 */
package com.sjy.constant;

/**
 * 任务状态
 * 
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2017年1月14日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
public class JobStatus {

	public final static int INIT = 1; // 初始化
	public final static int WORKING = 2; // 工作中
	public final static int TIMEOUT = 4; // 超时
	public final static int FINISHED = 5; // 处理成功
	public final static int ERROR = 6; // 处理失败

}
