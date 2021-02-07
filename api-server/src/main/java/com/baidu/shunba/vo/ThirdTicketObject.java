package com.baidu.shunba.vo;

import java.util.List;

public class ThirdTicketObject {

	public String transSn;
	public String transCode;
	public String lineID;    //推送过来
	public String shiftID;   //主动抓取
	
	public int retCode;    //核票结果，主动拉取
	public String transitDate;    //核票结果，主动拉取
	
	public List<ThirdTicket> ticketAry;
}
