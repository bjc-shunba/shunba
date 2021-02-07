package com.baidu.shunba.bean.message;

import lombok.Data;

@Data
public class SecondTicket {
    public String ticketNo;

    public String lineId;

    public String shiftNo;

    public String memberId;

    public Integer operate;
}
