package com.baidu.shunba.queryvo;

import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.filter.impl.DateFilter;
import com.baidu.shunba.filter.impl.IntegerFilter;
import com.baidu.shunba.filter.impl.StringFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SBShiftCustomBeanQueryVO extends QueryCriteria {
    private StringFilter shiftNo;

    private IntegerFilter seq;

    private StringFilter name;

    private StringFilter lineId;

    private StringFilter carNo;

    private StringFilter driverId;

    private StringFilter driverName;

    private StringFilter driverPhone;

    private DateFilter startTimeDate;

    private DateFilter endTimeDate;
}
