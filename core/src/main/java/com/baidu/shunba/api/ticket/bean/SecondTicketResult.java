package com.baidu.shunba.api.ticket.bean;

import com.baidu.shunba.common.utils.DateUtils;

import java.util.Date;

public class SecondTicketResult {

	public String ticketNo;
	public boolean checkResult;

	public double temperature;
	public int checkType;
	private String timestamp;

	public String remark;

	public Date getTimestamp() {
		try {
			return DateUtils.getDateFromStringWithFormat(timestamp, "yyyy-MM-dd HH:mm:ss");
		} catch (Exception e) {
			return null;
		}
	}

	public void setTimestamp(Date date) {
		timestamp = DateUtils.getDateStringWithFormat(date, "yyyy-MM-dd HH:mm:ss");
	}
}
