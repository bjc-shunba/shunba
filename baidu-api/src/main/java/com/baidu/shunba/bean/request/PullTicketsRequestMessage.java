package com.baidu.shunba.bean.request;

import com.baidu.shunba.bean.RequestMessage;
import lombok.Data;

/**
 * 拉取核票结果
 */
@Data
public class PullTicketsRequestMessage extends RequestMessage {
    private String shiftNo;
}
