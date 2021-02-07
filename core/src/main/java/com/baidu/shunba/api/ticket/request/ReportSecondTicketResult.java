package com.baidu.shunba.api.ticket.request;

import com.baidu.shunba.api.BaseBean;
import com.baidu.shunba.api.ticket.bean.SecondTicketResult;

import java.util.ArrayList;
import java.util.List;

public class ReportSecondTicketResult extends BaseBean {

	public final List<SecondTicketResult> ticketAry = new ArrayList<>();

	public ReportSecondTicketResult() {
		transCode = "04";
	}
}
