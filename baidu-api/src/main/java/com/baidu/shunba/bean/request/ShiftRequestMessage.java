package com.baidu.shunba.bean.request;

import com.baidu.shunba.bean.RequestMessage;
import com.baidu.shunba.bean.SecondLineShift;
import lombok.Data;

import java.util.List;

@Data
public class ShiftRequestMessage extends RequestMessage {
    private String startDate;

    List<SecondLineShift> lineAry;
}
