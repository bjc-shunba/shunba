package com.baidu.shunba.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface ApiModelDetails {
    /**
     * 功能说明
     *
     * @return
     */
    String function();
}
