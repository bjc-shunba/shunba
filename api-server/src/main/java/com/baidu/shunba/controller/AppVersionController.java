package com.baidu.shunba.controller;

import com.baidu.shunba.bean.QueryVo;
import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.entity.SBAppVersion;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.queryvo.SBAppVersionQueryVO;
import com.baidu.shunba.service.SBAppVersionService;
import com.baidu.shunba.utils.Base64Utils;
import com.baidu.shunba.utils.FileUtils;
import com.baidu.shunba.utils.MD5Utils;
import com.baidu.shunba.vo.AppVersionUploadParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.List;

@Api("PAD APP版本管理")
@RestController
@RequestMapping("appVersion")
@Slf4j
public class AppVersionController {
    @Autowired
    private SBAppVersionService sbAppVersionService;

    @Value("${shunba.uploadFolder}")
    private String uploadFolder;

    @ApiOperation("获取版本号")
    @RequestMapping(value = "getLatestVersion", method = RequestMethod.GET)
    public ResultVo<SBAppVersion> getLatestVersion() {
        return ResultVo.ok(sbAppVersionService.getLatestVersion());
    }

    @ApiOperation("获取所有版本")
    @RequestMapping(value = "listVersions", method = RequestMethod.POST)
    public ResultVo<?> listVersions(@RequestBody QueryVo<SBAppVersionQueryVO> queryVO) {
        return ResultVo.ok(sbAppVersionService.commonQuery(queryVO));
    }

    @ApiOperation("新增或修改版本文件")
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public ResultVo<SBAppVersion> uploadNewVersion(@RequestParam("file") MultipartFile file, @ModelAttribute AppVersionUploadParam param) {
        String filePath = uploadFolder + "/app/";

        File path = new File(filePath);

        if (!path.exists() || !path.isDirectory()) {
            path.mkdirs();
        }

        String fileName = filePath + param.getVersion() + ".apk";

        File apkFile = new File(fileName);

        if (apkFile.exists()) {
            if (!apkFile.delete()) {
                return ResultVo.error("文件上传失败, 无法删除版本旧文件");
            }
        }

        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            file.transferTo(apkFile);
        } catch (IOException e) {
            log.error("apk文件保存错误: ", e);

            return ResultVo.error("文件上传失败, 文件保存错误");
        } finally {
            FileUtils.close(inputStream);
        }

        String fileMD5;
        try {
            fileMD5 = MD5Utils.getFileMD5(apkFile);
        } catch (Exception e) {
            log.error("apk文件MD5生成失败: ", e);

            return ResultVo.error("apk文件MD5生成失败");
        }

        // 调用gc, 释放文件, 防止不能删除
        System.gc();
        SBAppVersion version = new SBAppVersion();

        version.setMd5(fileMD5);
        version.setMemo(param.getMemo());
        version.setVersion(param.getVersion());

        return ResultVo.ok(sbAppVersionService.saveOrUpdate(version));
    }


    @ApiOperation("升级最新版本号. 从所有未使用的版本中, 找出第一个上传的版本将其下发. 如果不存在未下发的版本, 则返回错误")
    @RequestMapping(value = "use-newest", method = {RequestMethod.GET, RequestMethod.POST})
    public ResultVo<Boolean> useNewVersion() throws AppException {
        sbAppVersionService.useNewVersion();

        return ResultVo.ok(true);
    }
}
