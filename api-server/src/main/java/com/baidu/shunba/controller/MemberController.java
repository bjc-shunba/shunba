package com.baidu.shunba.controller;

import com.baidu.shunba.bean.QueryVo;
import com.baidu.shunba.bean.ResultVo;
import com.baidu.shunba.client.SocketServerInnerApiClient;
import com.baidu.shunba.entity.SBMember;
import com.baidu.shunba.queryvo.SBMemberQueryVO;
import com.baidu.shunba.service.SBMemberService;
import com.baidu.shunba.utils.Base64Utils;
import com.baidu.shunba.utils.ObjectUtils;
import com.baidu.shunba.vo.MemberObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Date;
import java.util.Optional;

@Api
@RestController
@RequestMapping("member")
@Slf4j
public class MemberController {
    @Autowired
    private SBMemberService sbMemberService;

    @Autowired
    private SocketServerInnerApiClient socketServerInnerApiClient;

    @Value("${shunba.uploadFolder}")
    private String uploadFolder;

    @ApiOperation("是否人脸图片已经上传")
    @RequestMapping(value = "checkFaceExist", method = RequestMethod.GET)
    public ResultVo<?> checkFaceExist(@RequestParam String memberId) {
        if (StringUtils.isEmpty(memberId)) {
            return ResultVo.error("用户ID为空");
        }

        Optional<SBMember> optionalSBMember = sbMemberService.findById(memberId);
        if (!optionalSBMember.isPresent()) {
            return ResultVo.error("用户不存在");
        }

        SBMember member = optionalSBMember.get();

        if (ObjectUtils.getIntValue(member.getHasImage()) <= 0) {
            return ResultVo.error("人脸信息不存在");
        }

        return ResultVo.ok("存在人脸信息");
    }

    /**
     * 顺吧上传人脸信息
     *
     * @param memberObject
     * @return
     */
    @ApiOperation("上传用户图片和信息")
    @RequestMapping(value = "uploadMemberFace", method = RequestMethod.POST)
    public ResultVo<?> uploadMemberFace(@RequestBody MemberObject memberObject) {
        if (StringUtils.isEmpty(memberObject.memberId)) {
            return ResultVo.error("用户名不能为空");
        }

        int memberType;

        if ("D".equalsIgnoreCase(memberObject.getUserCategory())) {
            // 用户类型司机
            memberType = 2;
        } else {
            memberType = 1;
        }

        try {
            boolean hasImage = false;

            try {
                String fileFolder = uploadFolder + ("/userface/");

                File folder = new File(fileFolder);
                if (!folder.exists()) {
                    folder.mkdirs();
                }

                String tmpFile = fileFolder + "tmp_" + memberObject.memberId + ".jpg";
                String file;
                if (memberType == 1) {
                    file = fileFolder + memberObject.memberId + ".jpg";
                } else {
                    file = fileFolder + "s-" + memberObject.memberId + ".jpg";
                }
                File tmpPhoto = new File(tmpFile);
                String base64Photo = Base64Utils.formatBase64Photo(memberObject.photo);
                if (StringUtils.isBlank(base64Photo)) {
                    return ResultVo.error("发送的照片为空!");
                }
                boolean isSuccess = Base64Utils.decodeToFile(base64Photo, tmpPhoto);

                if (isSuccess) {
                    File photo = new File(file);
                    if (photo.exists()) {
                        if (photo.delete()) {
                            hasImage = tmpPhoto.renameTo(photo);
                        }
                    } else {
                        hasImage = tmpPhoto.renameTo(photo);
                    }
                }
            } catch (Exception ex) {
                log.error("uploadMemberFace::save_file", ex);
                return ResultVo.error("base64解码失败, 无法同步用户");
            }

            Optional<SBMember> optionalSBMember = sbMemberService.findByMemberId(memberObject.memberId);

            SBMember member = optionalSBMember.orElse(null);

            Date now = new Date();
            if (member == null) {
                member = new SBMember();
                member.setCreateDate(now);
                member.setMemberId(memberObject.memberId);
            }
            member.setUpdateDate(now);
            member.setMemberType(memberType);

            if (StringUtils.isNotEmpty(memberObject.name)) {
                member.setName(memberObject.name);
            }

            if (StringUtils.isNotEmpty(memberObject.phone)) {
                member.setPhone(memberObject.phone);
            }

            if (ObjectUtils.getIntValue(member.getHasImage()) == 0 && hasImage) {
                member.setHasImage(1);
            }

            sbMemberService.saveAndFlush(member);

            socketServerInnerApiClient.notifyNewFace(member);
            
            JsonObject memberObjectJson = new Gson().toJsonTree(memberObject).getAsJsonObject();
            memberObjectJson.remove("photo");
            log.info(" ======== 保存人脸照片成功: {}", memberObjectJson);
        } catch (Exception e) {
            log.debug(" ======== 保存人脸照片失败: {}, 失败原因: {}", memberObject, e);
            return ResultVo.error(e);
        }

        return ResultVo.ok("ok");
    }

    @ApiOperation("获取所有用户")
    @RequestMapping(value = "listMember", method = RequestMethod.POST)
    public ResultVo<?> listMember(@RequestBody QueryVo<SBMemberQueryVO> query) {
        return ResultVo.ok(sbMemberService.commonQuery(query));
    }
}
