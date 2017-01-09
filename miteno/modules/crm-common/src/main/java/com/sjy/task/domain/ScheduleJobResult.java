/**
 * 
 */
package com.sjy.task.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

import com.sjy.util.UuidRootEntity;

/**
 * ScheduleJob执行结果
 * 
 * @copyright(c) Copyright SJY Corporation 2016.
 * @since 2016年12月20日
 * @author liyan
 * @e-mail 289149734@qq.com
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tbl_ScheduleJobResult")
@Setter
@Getter
public class ScheduleJobResult extends UuidRootEntity {

	@ManyToOne
	ScheduleJob job;

	// 创建时间
	Date recordDate;

	// 任务业务日期
	Date businessDate;

	// 执行结束时间
	Date finishDate;

	// 执行任务耗时，单位：毫秒
	long costTimes;

	// 任务执行结果
	@Column(nullable = false)
	int jobStatus;

	// 错误日志
	@Column(nullable = true, length = 5000)
	String errorMsg;
}
