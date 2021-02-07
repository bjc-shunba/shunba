package com.baidu.shunba.controller;

import com.baidu.shunba.bean.QueryVo;
import com.baidu.shunba.bean.ResponseMessage;
import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.bean.message.SecondLineShiftRequestMessage;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.queryvo.SBShiftCustomBeanQueryVO;
import com.baidu.shunba.queryvo.SBShiftQueryVO;
import com.baidu.shunba.service.SBShiftCustomBeanService;
import com.baidu.shunba.service.SBShiftService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("shift")
public class ShiftController {
    @Autowired
    private SBShiftService sbShiftService;

    @Autowired
    private SBShiftCustomBeanService sbShiftCustomBeanService;

    @ApiOperation("查询所有班次")
    @RequestMapping(value = "listShift", method = RequestMethod.POST)
    public ResultVo listShift(@RequestBody QueryVo<SBShiftQueryVO> query) {
        return ResultVo.ok(sbShiftService.commonQuery(query));
    }

    @ApiOperation("查询所有班次")
    @RequestMapping(value = "customer-search", method = RequestMethod.POST)
    public ResultVo customerSearch(@RequestBody QueryVo<SBShiftCustomBeanQueryVO> query) {
        return ResultVo.ok(sbShiftCustomBeanService.commonQuery(query));
    }

    @ApiOperation("使用线路id查询班次")
    @RequestMapping(value = "searchShift", method = RequestMethod.GET)
    public ResultVo searchShift(@RequestParam(name = "lineId") String lineId) {
        if (StringUtils.isBlank(lineId)) {
            return ResultVo.error("参数不能为空");
        }

        return ResultVo.ok(sbShiftService.findAllByLineId(lineId));
    }

    @ApiOperation("顺巴推送班次信息")
    @RequestMapping(value = "receiveLineShift", method = RequestMethod.POST)
    public ResponseMessage pullShift(@RequestBody SecondLineShiftRequestMessage lineShiftRequestMessage) throws AppException {
        return sbShiftService.pullShift(lineShiftRequestMessage);
    }
}
