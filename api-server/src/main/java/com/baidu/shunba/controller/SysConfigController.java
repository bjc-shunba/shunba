package com.baidu.shunba.controller;

import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.client.SocketServerInnerApiClient;
import com.baidu.shunba.entity.SysConfig;
import com.baidu.shunba.service.SysConfigService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("sys-config")
@Slf4j
public class SysConfigController {
    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private SocketServerInnerApiClient socketServerInnerApiClient;

    @ApiOperation("根据配置项键名称, 查询配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "配置项键名称, 即jian字段的值")
    })
    @RequestMapping(path = "find/by-key", method = {RequestMethod.GET})
    public ResultVo<SysConfig> findByKey(@RequestParam String key) {
        return ResultVo.ok(sysConfigService.findByKey(key));
    }

    @ApiOperation("查询所有配置")
    @RequestMapping(path = "find-all", method = {RequestMethod.GET})
    public ResultVo<List<SysConfig>> getAllSysConfig() {
        return ResultVo.ok(sysConfigService.findAll());
    }

    @ApiOperation("根据配置项键名称, 批量查询配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "配置项键名称, 即jian字段的值")
    })
    @RequestMapping(path = "find-all/by-key", method = {RequestMethod.POST})
    public ResultVo<List<SysConfig>> findByKey(@RequestBody List<String> key) {
        if (null == key || key.isEmpty()) {
            return ResultVo.error("参数错误, 请传入至少一个key!");
        }

        return ResultVo.ok(sysConfigService.findAllByKey(key));
    }

    @ApiOperation("更新配置")
    @RequestMapping(path = "update", method = {RequestMethod.POST})
    public ResultVo<Boolean> update(@RequestBody SysConfig sysConfig) {
        try {
            SysConfig save = sysConfigService.save(sysConfig);

            socketServerInnerApiClient.sendInitAll();

            return ResultVo.ok(save != null);
        } catch (Exception e) {
            log.error("{}", e);

            return ResultVo.ok(false);
        }
    }

    @ApiOperation("批量更新配置")
    @RequestMapping(path = "update-all", method = {RequestMethod.POST})
    public ResultVo<Boolean> update(@RequestBody List<SysConfig> sysConfig) {
        try {
            List<SysConfig> sysConfigs = sysConfigService.saveAll(sysConfig);

            socketServerInnerApiClient.sendInitAll();

            return ResultVo.ok(sysConfigs.size() == sysConfig.size());
        } catch (Exception e) {
            log.error("{}", e);

            return ResultVo.ok(false);
        }
    }

    @ApiOperation("删除配置项")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "key", value = "配置项键名称, 即jian字段的值")
    })
    @RequestMapping(path = "delete-by-key", method = {RequestMethod.DELETE})
    public ResultVo<Boolean> delete(@RequestParam String key) {
        try {
            sysConfigService.deleteByKey(key);

            return ResultVo.ok(true);
        } catch (Exception e) {
            log.error("{}", e);

            return ResultVo.ok(false);
        }
    }
}
