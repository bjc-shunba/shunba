package com.baidu.shunba.bean.message;

import com.baidu.shunba.bean.RequestMessage;
import com.baidu.shunba.utils.DateUtils;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SecondLineShiftRequestMessage extends RequestMessage {
    private String startDate;

    private List<SecondLineShift> lineAry;

    public Date getStartDate() {
        return DateUtils.getDateFromStringWithFormat(this.startDate, "yyyy-MM-dd");
    }
}
