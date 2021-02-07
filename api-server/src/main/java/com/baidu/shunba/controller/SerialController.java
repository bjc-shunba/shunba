package com.baidu.shunba.controller;

import com.baidu.shunba.bean.QueryVo;
import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.client.SocketServerInnerApiClient;
import com.baidu.shunba.constant.ResultEnum;
import com.baidu.shunba.entity.BDSerialNumber;
import com.baidu.shunba.entity.SBDevice;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.filter.impl.IntegerFilter;
import com.baidu.shunba.filter.impl.StringFilter;
import com.baidu.shunba.queryvo.BDSerialNumberQueryVO;
import com.baidu.shunba.service.BDSerialNumberService;
import com.baidu.shunba.service.SBDeviceService;
import com.baidu.shunba.utils.FileUtils;
import com.baidu.shunba.utils.ObjectUtils;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

@ApiOperation("车载pad配置序列号配置")
@RestController
@RequestMapping("serial")
@Slf4j
public class SerialController {
    public static final String InputSplit = ",|;|，|；";
    public static final String ImportSplit = ",|;";

    @Autowired
    private BDSerialNumberService bdSerialNumberService;

    @Autowired
    private SBDeviceService sbDeviceService;

    @Autowired
    private SocketServerInnerApiClient socketServerInnerApiClient;

    @ApiOperation("查询车载PAD序列号配置列表")
    @RequestMapping(value = "listSerial", method = RequestMethod.POST)
    public ResultVo listSerial(@RequestBody QueryVo<BDSerialNumberQueryVO> query) {
        return ResultVo.ok(bdSerialNumberService.commonQuery(query));
    }

    @ApiOperation("更新序列号")
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ResultVo<BDSerialNumber> update(@RequestBody BDSerialNumber serialNumber) throws AppException {
        log.info(" ********** 开始更新序列号: {}", serialNumber);

        // 修改场景下, 修改完成完成后, 需要分别给被修改的所有设备发送初始化信号
        // 新关联的设备
        SBDevice device = null;
        if (StringUtils.isNotBlank(serialNumber.getCarNo())) {
            device = sbDeviceService.findByCarNo(serialNumber.getCarNo());
        } else if (StringUtils.isNotBlank(serialNumber.getDeviceId())) {
            device = sbDeviceService.findByDeviceId(serialNumber.getDeviceId()).orElse(null);
        }

        BDSerialNumber saved = bdSerialNumberService.saveAndFlush(serialNumber);

        // 保存成功后, 发送初始化信号
        if (null != device) {
            socketServerInnerApiClient.sendInit(device.getDeviceId());
        }

        log.info(" ********** 更新序列号: {} 成功", serialNumber);

        return ResultVo.ok(saved);
    }

    @ApiOperation("删除序列号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "配置项id")
    })
    @RequestMapping(value = "delete-by-id", method = RequestMethod.DELETE)
    public ResultVo<String> update(@RequestParam String id) throws AppException {
        Optional<BDSerialNumber> byId = bdSerialNumberService.findById(id);

        if (!byId.isPresent()) {
            return ResultVo.error("指定序列号不存在!");
        }

        BDSerialNumber bdSerialNumber = byId.get();

        if (ObjectUtils.sameWidth(bdSerialNumber.getDelFlag(), 1)) {
            return ResultVo.ok("ok");
        }

        bdSerialNumberService.delete(bdSerialNumber);

        return ResultVo.ok("ok");
    }

    @ApiOperation("")
    @RequestMapping(value = "searchValidSerial", method = RequestMethod.GET)
    public ResultVo searchValidSerial(@RequestParam(name = "serialNumber") String serialNumber) {
        if (StringUtils.isBlank(serialNumber)) {
            return ResultVo.error("参数不合法");
        }

        BDSerialNumberQueryVO vo = new BDSerialNumberQueryVO();

        StringFilter stringFilter = new StringFilter();
        stringFilter.setLike(serialNumber);
        vo.setSerialNumber(stringFilter);

        stringFilter = new StringFilter();
        stringFilter.setSpecification(false);
        vo.setSerialNumber(stringFilter);

        IntegerFilter integerFilter = new IntegerFilter();
        integerFilter.setNotEquals(1);
        vo.setDelFlag(integerFilter);
        vo.setIsUsed(integerFilter);

        return ResultVo.ok(bdSerialNumberService.findAll(vo));
    }

    @ApiOperation("")
    @RequestMapping(value = "importSerial", method = RequestMethod.GET)
    public ResultVo importSerial(HttpServletRequest request) {
        String responseUrl = "redirect:/web/serial/listSerial";

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();

        Exception lastError = null;
        int success = 0;
        int fail = 0;

        try {
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                MultipartFile file = entity.getValue();

                String extend = FileUtils.getExtend(file.getOriginalFilename()).toLowerCase();
                if (!extend.equals("csv") && !extend.equals("txt")) {
                    return ResultVo.error("仅支持csv和txt文件");
                }

                InputStreamReader reader = new InputStreamReader(file.getInputStream());
                BufferedReader br = new BufferedReader(reader);
                try {
                    int lineCount = 0;
                    int serialIndex = -1;
                    boolean alongLine = false;

                    while (true) {
                        String line = br.readLine();
                        if (line == null) {
                            break;
                        }
                        String[] contents = line.split(ImportSplit);

                        //first line - begin
                        if (lineCount == 0) {
                            if (line.indexOf("序列号") < 0) {
                                if (contents.length > 1) {
                                    for (int i = 0; i < contents.length; i++) {
                                        String c = contents[i];
                                        if (c.indexOf('-') > 0) {
                                            serialIndex = i;
                                            try {
                                                handlerSerail(c);
                                                success++;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                fail++;
                                                lastError = e;
                                            }
                                            break;
                                        }
                                    }
                                } else {
                                    if (line.indexOf('-') > 0) {
                                        alongLine = true;
                                        serialIndex = 0;
                                        break;
                                    }
                                }
                            } else {
                                if (contents.length > 0) {
                                    for (int i = 0; i < contents.length; i++) {
                                        String c = contents[i];
                                        if (c.indexOf("序列号") >= 0) {
                                            serialIndex = i;
                                            break;
                                        }
                                    }
                                }
                            }

                            lineCount++;
                            continue;
                        }
                        //first line - end

                        if (serialIndex < 0) {
                            return ResultVo.error("未找到序列号对应的位置，请确保文件首行标题包含“序列号”数据列！");
                        }

                        if (alongLine) {
                            try {
                                handlerSerail(line);
                                success++;
                            } catch (Exception e) {
                                e.printStackTrace();
                                fail++;
                                lastError = e;
                            }
                        } else {
                            String serial = contents[serialIndex];
                            try {
                                handlerSerail(serial);
                                success++;
                            } catch (Exception e) {
                                e.printStackTrace();
                                fail++;
                                lastError = e;
                            }
                        }

                        lineCount++;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (success == 0) {
                        return ResultVo.error("输入文件读取错误：" + e);
                    }
                    lastError = e;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            if (success == 0) {
                return ResultVo.error("输入文件读取错误：" + e);
            }
            lastError = e;
        }

        StringBuilder result = new StringBuilder();
        result.append(String.format("成功导入%d条，失败%d条。", success, fail));
        if (lastError != null) {
            result.append("错误：" + lastError);
        }

        return ResultVo.ok(result);
    }

    private void handlerSerail(String serial) throws AppException {
        serial = serial.replaceAll("\"", "");
        BDSerialNumber bdSerial = new BDSerialNumber();
        bdSerial.setSerialNumber(serial);
        bdSerial.setIsUsed(0);

        bdSerialNumberService.saveAndFlush(bdSerial);
    }

    @ApiOperation("")
    @RequestMapping(value = "inputSerial", method = RequestMethod.GET)
    public ResultVo inputSerial(@RequestParam String inputSerials) {
        if (StringUtils.isBlank(inputSerials)) {
            return ResultVo.error("输入新序列号为空");
        }

        String[] serials = inputSerials.split(InputSplit);
        if (serials.length <= 0) {
            return ResultVo.error("输入新序列号为空");
        }

        int success = 0;
        int fail = 0;
        Exception lastError = null;
        for (int i = 0; i < serials.length; i++) {

            String serial = serials[i];
            try {
                handlerSerail(serial);
                success++;
            } catch (Exception e) {
                e.printStackTrace();
                fail++;
                lastError = e;
            }
        }

        StringBuilder result = new StringBuilder();
        result.append(String.format("成功导入%d条，失败%d条。", success, fail));
        if (lastError != null) {
            result.append("错误：" + lastError);
        }

        return ResultVo.ok(result);
    }
}
