package com.baidu.shunba.controller;

import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.constant.ResultEnum;
import com.baidu.shunba.entity.SbDriverDispatchRecord;
import com.baidu.shunba.service.SbDriverDispatchRecordService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("dispatch")
public class SbDriverDispatchRecordController {
    @Autowired
    private SbDriverDispatchRecordService sbDriverDispatchRecordService;

    @ApiOperation("")
    @RequestMapping(value = "searchShift", method = RequestMethod.GET)
    public ResultVo searchShift(@RequestParam(name = "shiftNo", required = true) String shiftNo, @RequestParam(name = "deviceId", required = false) String deviceId) {
        if (StringUtils.isBlank(shiftNo)) {
            return ResultVo.error("shiftNo不能为空");
        }

        SbDriverDispatchRecord sbDriverDispatchRecord = sbDriverDispatchRecordService.findSbDriverDispatchRecord(shiftNo);

        if (null == sbDriverDispatchRecord) {
            // 收发车记录不存在, 默认返回1表示收车状态
            return ResultVo.ok(ResultEnum.SUCCESS, 1);
        }

        return ResultVo.ok(ResultEnum.SUCCESS, sbDriverDispatchRecord.getOperate());
    }
}
