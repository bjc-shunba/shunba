package com.baidu.shunba.queryvo;

import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.filter.impl.DateFilter;
import com.baidu.shunba.filter.impl.IntegerFilter;
import com.baidu.shunba.filter.impl.StringFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class BDSerialNumberQueryVO extends QueryCriteria {
    private StringFilter serialNumber;

    private IntegerFilter isUsed;

    private IntegerFilter delFlag;

    private StringFilter deviceId;

    private StringFilter carNo;

    private DateFilter createDate;

    private DateFilter updateDate;
}
