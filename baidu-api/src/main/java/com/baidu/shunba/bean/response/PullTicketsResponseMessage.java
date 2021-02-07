package com.baidu.shunba.bean.response;

import com.baidu.shunba.bean.PullTicketsResponseBean;
import com.baidu.shunba.bean.RetCode;
import com.google.gson.Gson;
import lombok.Data;

import java.util.List;

@Data
public class PullTicketsResponseMessage {
    /**
     * 交易流水号，全局唯一，长度10~30
     */
    private String transSn;

    /**
     * 返回码
     */
    private RetCode retCode;

    private List<PullTicketsResponseBean> ticketAry;

    /**
     * 转换结果为json
     *
     * @return json string
     */
    public String asJsonString() {
        return new Gson().toJson(this);
    }

    public PullTicketsResponseMessage(String transSn, RetCode retCode, List<PullTicketsResponseBean> ticketAry) {
        this.transSn = transSn;
        this.retCode = retCode;
        this.ticketAry = ticketAry;
    }
}
