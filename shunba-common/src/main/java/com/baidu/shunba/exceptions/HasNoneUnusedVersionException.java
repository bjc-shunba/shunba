package com.baidu.shunba.exceptions;

import com.baidu.shunba.constant.ResultEnum;

public class HasNoneUnusedVersionException extends AppException {
    public HasNoneUnusedVersionException() {
        super(ResultEnum.HAS_NONE_UNUSED_VERSION_ERROR.getMsg(), ResultEnum.HAS_NONE_UNUSED_VERSION_ERROR);
    }
}
