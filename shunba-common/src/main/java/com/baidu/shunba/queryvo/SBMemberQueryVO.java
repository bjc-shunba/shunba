package com.baidu.shunba.queryvo;

import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.filter.impl.DateFilter;
import com.baidu.shunba.filter.impl.IntegerFilter;
import com.baidu.shunba.filter.impl.StringFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SBMemberQueryVO extends QueryCriteria {
    private StringFilter name;

    private StringFilter phone;

    private IntegerFilter hasImage;

    private DateFilter createDate;

    private DateFilter updateDate;

    private StringFilter memberId;

    private IntegerFilter memberType;
}
