package com.baidu.shunba.queryvo;

import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.filter.impl.DateFilter;
import com.baidu.shunba.filter.impl.DoubleFilter;
import com.baidu.shunba.filter.impl.IntegerFilter;
import com.baidu.shunba.filter.impl.StringFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SBTicketQueryVO extends QueryCriteria {
    private StringFilter ticketNo;

    private StringFilter lineId;

    private StringFilter shiftId;

    private StringFilter shiftNo;

    private StringFilter memberId;

    private StringFilter faceId;

    private StringFilter userPhone;

    private StringFilter deviceId;

    private IntegerFilter seq;

    private IntegerFilter delFlag;

    private IntegerFilter isCheck;

    private DoubleFilter temperature;

    private IntegerFilter checkType;

    private DateFilter checkTime;
}
