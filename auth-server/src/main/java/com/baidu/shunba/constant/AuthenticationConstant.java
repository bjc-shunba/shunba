package com.baidu.shunba.constant;

/**
 * 认证授权常量
 */
public final class AuthenticationConstant {
    public static final String RESOURCE_ID = "shunba";

    public static final String CORS_CONFIGURATION_SOURCE_PATH = "/**";

    public static final String[] EXCLUDE_URLS = new String[]{"/", "/login", "/oauth/token", "/oauth/**", "/logout", "/login/**", "/actuator/**",
            "/swagger-ui.html", "/webjars/**", "/swagger-ui.html", "/swagger-resources", "/swagger-resources/**", "/v2/api-docs", "/csrf"};
}
