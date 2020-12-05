package org.springblade.common.entity;

import lombok.Data;

import java.util.Date;

@Data
public class DateVo {
	private Date startDate;
	private Date endDate;
	public DateVo(Date startDate, Date endDate) {
		this.startDate = startDate;
		this.endDate = endDate;
	}
}
