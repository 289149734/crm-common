/**
 * 
 */
package com.sjy.task.constant;

/**
 * ScheduleJob任务执行结果
 * 
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
public class ScheduleJobStatus {

	public final static int INIT = 1; // 初始化
	public final static int PROCESSING = 2; // 执行中
	public final static int TIMEOUT = 3; // 执行超时
	public final static int SUCCESS = 4; // 执行成功
	public final static int FAILURE = 5; // 执行失败

}
