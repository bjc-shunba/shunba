package com.baidu.shunba.bean;

import lombok.Data;

/**
 * 查询对象
 *
 * @param <T>
 */
@Data
public class QueryVo<T extends QueryCriteria> {
    private T query;

    private PageVo page;
}
