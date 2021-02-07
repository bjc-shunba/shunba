package com.baidu.shunba.api.line.response;

import com.baidu.shunba.api.BaseBean;
import com.baidu.shunba.api.line.bean.SecondLineShift;
import com.baidu.shunba.common.utils.DateUtils;

import java.util.Date;
import java.util.List;

public class SecondLineShiftObject extends BaseBean {
	
	private String startDate;

	public Date getStartDate() {
		return DateUtils.getDateFromStringWithFormat(startDate, "yyyy-MM-dd");
	}
	
	public List<SecondLineShift> lineAry;
}
