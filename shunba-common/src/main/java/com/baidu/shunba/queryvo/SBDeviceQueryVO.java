package com.baidu.shunba.queryvo;

import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.filter.impl.IntegerFilter;
import com.baidu.shunba.filter.impl.StringFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SBDeviceQueryVO extends QueryCriteria {
    private StringFilter deviceId;

    private StringFilter lineId;

    private StringFilter shiftNo;

    private StringFilter serialNumberId;

    private IntegerFilter delFlag;

    private IntegerFilter isOnline;

    private StringFilter appVersion;

    private StringFilter carNo;
}
