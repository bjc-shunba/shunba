package com.baidu.shunba.vo;

import com.baidu.shunba.utils.DateUtils;

import java.util.Date;

public class RequestPullThirdTicketResult {

	public String transSn;
	public String transCode;
	private String transitDate;
	public String shiftID;
	
	public void setTransitDate(Date date) {
		transitDate = DateUtils.getDateStringWithFormat(date, "yyyy-MM-dd");
	}
}
