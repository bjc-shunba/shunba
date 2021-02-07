package com.baidu.shunba.client;

import com.baidu.shunba.vo.MemberObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "api-server")
@RequestMapping("member")
public interface MemberApiClient {
    @ApiOperation("上传用户图片和信息")
    @RequestMapping(value = "uploadMemberFace", method = RequestMethod.POST)
    String uploadMemberFace(@RequestBody MemberObject memberObject);
}
