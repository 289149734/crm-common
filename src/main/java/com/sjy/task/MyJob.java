/**
 * 
 */
package com.sjy.task;

import org.quartz.Job;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2017年1月9日
 * @author liyan
 * @e-mail 289149734@qq.com
 *
 */
public interface MyJob extends Job {
	
	public void initTask();
	
}
