package com.baidu.shunba.api.ticket.response;

import com.baidu.shunba.api.BaseBean;
import com.baidu.shunba.api.ticket.bean.SecondTicket;
import com.baidu.shunba.common.utils.DateUtils;

import java.util.Date;
import java.util.List;

public class SecondTicketObject extends BaseBean {
	
	public int retCode;    //票务订单，主动拉取
	private String startDate;    //票务订单，主动拉取
	public int buyNumber;    //票务订单，主动拉取

	public Date getStartDate() {
		try {
			return DateUtils.getDateFromStringWithFormat(startDate, "yyyy-MM-dd");
		} catch (Exception e) {
			return null;
		}
	}
	
	public List<SecondTicket> ticketAry;
}
