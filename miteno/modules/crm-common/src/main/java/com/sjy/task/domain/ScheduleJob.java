/**
 * 
 */
package com.sjy.task.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import org.quartz.Job;

import com.sjy.util.UuidRootEntity;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_ScheduleJob")
@Setter
@Getter
public class ScheduleJob extends UuidRootEntity {

	/** 任务名称 **/
	@Column(unique = true)
	private String jobName;

	/** 任务分组 **/
	private String jobGroup;

	/** 任务状态 0禁用 1启用 2删除 **/
	private Integer jobStatus;

	/** 任务运行时间表达式 **/
	private String cronExpression;

	/** 任务描述 **/
	private String jobDesc;

	/** 任务实现类 **/
	private String jobClass;

	public Job getJob() throws Exception {
		try {
			return (Job) Class.forName(jobClass).newInstance();
		} catch (Exception e) {
			return null;
		}
	}
}
