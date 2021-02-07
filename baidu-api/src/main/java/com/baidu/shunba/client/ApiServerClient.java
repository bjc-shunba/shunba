package com.baidu.shunba.client;

import com.baidu.shunba.bean.message.SecondDispatchRequestMessage;
import com.baidu.shunba.bean.message.TicketRequestMessage;
import com.baidu.shunba.bean.request.ShiftRequestMessage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(value = "api-server")
public interface ApiServerClient {
    /**
     * 顺吧推送班次
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "/shift/receiveLineShift", method = {RequestMethod.POST})
    @ResponseBody
    String receiveLineShift(@RequestBody ShiftRequestMessage message);

    /**
     * 顺吧推送收发车
     *
     * @param message
     * @return
     */
    @RequestMapping(value = "/device/receiveDispatch", method = {RequestMethod.POST})
    @ResponseBody
    String receiveDispatch(@RequestBody SecondDispatchRequestMessage message);

    /**
     * 顺吧推送票据
     *
     * @param ticketObject
     * @return
     */
    @RequestMapping(value = "/ticket/receiveTickets", method = {RequestMethod.POST})
    @ResponseBody
    String receiveTickets(@RequestBody TicketRequestMessage ticketObject);
}
