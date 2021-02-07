package com.baidu.shunba.api.ticket.response;

import com.baidu.shunba.api.BaseBean;
import com.baidu.shunba.api.ticket.bean.SecondTicketResult;

import java.util.ArrayList;
import java.util.List;

public class SecondTicketResultObject extends BaseBean {

	public int retCode;    //票务订单，主动拉取

	public final List<SecondTicketResult> ticketAry = new ArrayList<>();
}
