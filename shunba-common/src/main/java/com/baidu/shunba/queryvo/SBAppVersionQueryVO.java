package com.baidu.shunba.queryvo;

import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.filter.impl.DateFilter;
import com.baidu.shunba.filter.impl.StringFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SBAppVersionQueryVO extends QueryCriteria {
    private StringFilter version;

    private DateFilter createDate;

    private DateFilter updateDate;
}
