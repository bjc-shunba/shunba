package com.baidu.shunba.vo;

import com.baidu.shunba.bean.RequestMessage;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

@Data
public class MemberObject extends RequestMessage {
    public String memberId;
    public String phone;
    public String name;
    //base64
    public String photo;

    public String userCategory;
}
