package com.baidu.shunba.api.line.response;

import com.baidu.shunba.api.BaseBean;

/**
 * 司机收发车
 */
public class SecondDispatchObject extends BaseBean {
    /**
     * 班次编号
     */
    public String shiftNo;

    /**
     * 操作（0-发车 1-收车）
     */
    public int operate;

    /**
     * 收发车时间
     */
    public String timestamp;
}
