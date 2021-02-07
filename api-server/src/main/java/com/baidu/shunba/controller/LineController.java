package com.baidu.shunba.controller;

import com.baidu.shunba.bean.QueryVo;
import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.entity.SBLine;
import com.baidu.shunba.filter.impl.StringFilter;
import com.baidu.shunba.queryvo.SBLineQueryVO;
import com.baidu.shunba.service.SBLineService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("line")
public class LineController {
    @Autowired
    private SBLineService sbLineService;

    @ApiOperation("")
    @RequestMapping(value = "listLine", method = RequestMethod.POST)
    public ResultVo<?> listLine(@RequestBody QueryVo<SBLineQueryVO> query) {
        return ResultVo.ok(sbLineService.commonQuery(query));
    }

    @ApiOperation("")
    @RequestMapping(value = "searchLine", method = RequestMethod.GET)
    public ResultVo<List<SBLine>> searchLine(@RequestParam(name = "q") String lineId) {
        if (StringUtils.isBlank(lineId)) {
            return ResultVo.error("参数不正确");
        }

        SBLineQueryVO vo = new SBLineQueryVO();

        StringFilter filter = new StringFilter();
        filter.setEquals(lineId);
        vo.setLineId(filter);

        return ResultVo.ok(sbLineService.findAll(vo));
    }
}
