/**
 * 
 */
package com.sjy.task.domain;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.sjy.constant.JobStatus;
import com.sjy.util.UuidRootEntity;

/**
 * 子任务
 * 
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_ScheduleSubJob")
@Setter
@Getter
public class ScheduleSubJobResult extends UuidRootEntity {

	/** 主任务 **/
	@ManyToOne
	private ScheduleJobResult scheduleJobResult;

	/** 是否完成 **/
	private Integer jobStatus = JobStatus.INIT;

	// 创建时间
	Date recordDate;

	// 任务开始日期
	Date startDate;

	// 执行结束时间
	Date finishDate;

	// 执行任务耗时，单位：毫秒
	long costTimes;

	// 错误日志
	@Lob
	@Basic(fetch = FetchType.LAZY)
	String errorMsg;

	@Column(length = 16)
	String taskName;

	// 业务数据
	@Lob
	String businessData;
}
