package com.baidu.shunba.vo;

import com.baidu.shunba.bean.RequestMessage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PushTicketRequest extends RequestMessage {
    @Getter
    @Setter
    private List<PushTicketBean> ticketAry;

    public PushTicketRequest(String transSn) {
        this.setTransCode("04");
        this.setTransSn(transSn);

        this.ticketAry = new ArrayList<>();
    }

    public void addPushTicketBean(PushTicketBean bean) {
        this.ticketAry.add(bean);
    }
}
