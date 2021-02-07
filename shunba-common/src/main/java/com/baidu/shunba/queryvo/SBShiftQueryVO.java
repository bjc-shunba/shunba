package com.baidu.shunba.queryvo;

import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.filter.impl.IntegerFilter;
import com.baidu.shunba.filter.impl.StringFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SBShiftQueryVO extends QueryCriteria {
    private StringFilter shiftId;

    private IntegerFilter seq;

    private StringFilter name;

    private StringFilter lineId;

    private StringFilter schedule;

    private StringFilter carNumber;

    private StringFilter driverId;

    private StringFilter driverName;

    private StringFilter driverPhone;

    private StringFilter startTime;

    private StringFilter endTime;
}
