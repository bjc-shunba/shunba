package com.baidu.shunba.queryvo;

import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.filter.impl.StringFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SBLineQueryVO extends QueryCriteria {
    private StringFilter lineId;

    private StringFilter name;
}
