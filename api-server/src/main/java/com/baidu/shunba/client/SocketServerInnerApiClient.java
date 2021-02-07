package com.baidu.shunba.client;

import com.baidu.shunba.annotation.ApiModelDetails;
import com.baidu.shunba.entity.SBDevice;
import com.baidu.shunba.entity.SBMember;
import com.baidu.shunba.entity.SbDriverDispatchRecord;
import com.baidu.shunba.vo.PushTicketsBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(value = "socket-server")
@RequestMapping("/api/inner")
public interface SocketServerInnerApiClient {
    /**
     * 推送收发车操作
     *
     * @param record
     * @return
     */
    @RequestMapping(value = "/sendDispatch", method = {RequestMethod.POST})
    @ResponseBody
    @ApiModelDetails(function = "推送收发车到设备")
    String sendDispatch(@RequestBody SbDriverDispatchRecord record);

    /**
     * 推送人脸数据
     *
     * @param member
     * @return
     */
    @RequestMapping(value = "/nofityNewFace", method = {RequestMethod.POST})
    @ResponseBody
    @ApiModelDetails(function = "推送人脸数据")
    String notifyNewFace(@RequestBody SBMember member);

    /**
     * 授权信息推送
     *
     * @param device
     * @return
     */
    @RequestMapping(value = "/pushAuth", method = {RequestMethod.POST})
    @ResponseBody
    @ApiModelDetails(function = "授权信息推送")
    String pushAuth(@RequestBody SBDevice device);

    /**
     * 向指定设备发送初始化信号
     *
     * @param deviceId
     * @return
     */
    @RequestMapping(value = "/init", method = {RequestMethod.GET})
    @ResponseBody
    @ApiModelDetails(function = "发送初始化信号到所有设备")
    String sendInit(@RequestParam(name = "deviceId") String deviceId);

    /**
     * 发送初始化信号到所有设备
     *
     * @return
     */
    @RequestMapping(value = "/init-all", method = {RequestMethod.GET})
    @ResponseBody
    @ApiModelDetails(function = "发送初始化信号到所有设备")
    String sendInitAll();

    /**
     * 推送票据订单到所有设备
     *
     * @param tickets
     * @return
     */
    @RequestMapping(value = "/nofityNewTickets", method = {RequestMethod.GET})
    @ResponseBody
    @ApiModelDetails(function = "推送票据订单到所有设备")
    String nofityNewTickets(@RequestBody PushTicketsBean tickets);
}
