/**
 * 
 */
package com.sjy.task.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.Data;

import org.quartz.Job;

/**
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@Entity
@Table(name = "tbl_ScheduleJob")
@Data
public class ScheduleJob {
	/** 任务id **/
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ScheduleJobSeq")
	@SequenceGenerator(name = "ScheduleJobSeq", sequenceName = "seq_ScheduleJob")
	@Column(name = "id", unique = true, nullable = false)
	private Long id;

	@Version
	Integer version = -1;

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
		return (Job) Class.forName(jobClass).newInstance();
	}
}
