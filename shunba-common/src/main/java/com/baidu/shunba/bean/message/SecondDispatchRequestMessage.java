package com.baidu.shunba.bean.message;

import com.baidu.shunba.bean.RequestMessage;
import lombok.Data;

@Data
public class SecondDispatchRequestMessage extends RequestMessage {
    /**
     * 班次编号
     */
    private String shiftNo;

    /**
     * 操作（0-发车 1-收车）
     */
    private int operate;

    /**
     * 收发车时间
     */
    private String timestamp;
}
