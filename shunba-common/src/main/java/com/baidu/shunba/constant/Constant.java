package com.baidu.shunba.constant;

public class Constant {
    /**
     * 0/没有/空
     **/
    public static final int ZERO = 0;
    /**
     * 1
     **/
    public static final int ONE = 1;
    /**
     * 2
     **/
    public static final int TWO = 2;
    /**
     * 2
     **/
    public static final int THREE = 3;
    /**
     * 4
     **/
    public static final int FOUR = 4;

    /**
     * -1
     **/
    public static final int NEGATIVE_ONE = -1;
    /**
     * 300
     **/
    public static final int THREE_HUNDREDS = 300;
    /**
     * 23
     **/
    public static final int TWENTYTHREE = 23;
    /**
     * 59
     **/
    public static final int FIFTY_NINE = 59;
    /**
     * 空格
     **/
    public static final String BLANK = " ";
    /**
     * 授权码未激活
     **/
    public static final String NOT_ACTIVE = "0";
    public static final int ROLE_ID_SCHOOL_ADMIN = 2;
    public static final int INSERT_STUDENT = 4;

    public static final int NUM_THIRTY = 30;
    public static final int NUM_SIXTY = 60;
    public static final Long LONG_SIXTY = 60L;
    public static final int NUM_ONE = 1;
    public static final int NUM_SIX = 6;
    public static final int NUM_SIX_TEEN = 16;
    public static final int NUM_TEN = 10;
    public static final int NUM_FORTY_FIVE = 45;
    public static final int NUM_FORTY_EIGHT = 48;
    public static final int NUM_FORTY = 40;
    public static final int NUM_HUNDRED_EIGHTY = 180;
    public static final int NUM_TWO_HUNDRED_FIFTY_SIX = 256;
    public static final int NUM_TWO_HUNDRED = 200;
    public static final int NUM_SIXTY_NINE = 100;
    public static final int NUM_TWENTY = 20;
    public static final String BASE_NUM_LETTER = "123456789abcdefghijklmnopqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ";
    public static final String FONT_COLOUR = "微软雅黑";
    public static final String AUTH_ORIZATION = "Authorization";
    public static final String SECUTITY_URL = "http://localhost:10020/oauth/token";
    public static final String SECUTITY_RELEASE_URL_2 = "/login/**";
    public static final String SECUTITY_RELEASE_URL_3 = "/random/**";
    public static final String CORS_CONFIGURATION_SOURCE_PATH = "/**";
    public static final String SECUTITY_RELEASE_URL_TEST = "test";
    public static final String BD_CALLBACK_BOS_URL = "/actuator/health";
    public static final String BD_CALLBACK_VOD_URL = "/file/vodCallback";
    public static final String WS_URL = "/wsuser/**";

    public static final String REDIS_VERIFY_KEY = "_verify";
    public static final String IMG_BASE64 = "data:image/jpg;base64,";
    public static final String IMG_TYPE_JPG = "jpg";

    public static final String GRANT_TYPE_VALUE = "password";
    public static final String GRANT_TYPE_KEY = "grant_type";
    public static final String CLIENT_ID_VALUE = "client_2";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_SECRET_VALUE = "123456";
    public static final String FINAL_SECRET = "$2a$10$eAnZGcYm7YIsdvjzzOHUe.u6F/t2gMm5sb0WsJm4juwVqM8N/e73G";
    public static final String CLIENT_SECRET_KEY = "client_secret";

    public static final String USER_NAME = "username";
    public static final String BCRYPT_TYPE = "{bcrypt}";
    public static final String SECURITY_CLIENT_ID_1 = "yzxy";
    public static final String SECURITY_CLIENT_CREDENTIALS = "client_credentials";
    public static final String SECURITY_REFRESH_TOKEN = "refresh_token";
    public static final String SECURITY_OAUTH2 = "oauth2";
    public static final String SECURITY_SELECT = "select";
    public static final String SEGMENTATION_CHARACTER_1 = "}";

    /**
     * 机构参数长度限制
     **/
    public static final int SCHOOL_CODE_LENGTH = 128;



    public static final String TOKEN_PREFIX = "tokens:*";
    public static final int ONE_THOUSAND = 1000;
    public static final String ASTERISK = "*";
    public static final String PRE_USER_CHANNEL = "/users";
    public static final String TEACHERS_CHANNEL = "/teachers";
    public static final String USERPPREFIX = "/users/";
    public static final String TOKEN = "token";
    public static final String UA = "User-Agent";
    public static final String COMPUTER = "Computer";
    public static final String IPAD_UA = "Mac OS X (iPad)";
    public static final String ANDROID_UA = "Android Mobile";
    public static final String IPHONE_UA = "Mac OS X (iPhone)";
    public static final String LOG_DATE_FORMAT = "yyyyMM";
    public static final String UNKNOWN = "unKnown";
    public static final String SUPER_ADMIN = "超级管理员";
    public static final String LOGIN_LOG = "loginlog";
    public static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final String LOGPUT_PATH = "/fourcelogout";
    public static final String USER_CACHE = "userCache";
    public static final String AES = "AES";

    public static final String XR_IP = "X-Real-IP";
    public static final String X_F_FOR = "X-Forwarded-For";
    public static final String PC_IP = "Proxy-Client-IP";
    public static final String WLC_IP = "WL-Proxy-Client-IP";
    public static final String HTTP_IP = "HTTP_CLIENT_IP";
    public static final String HTTP_FOR = "HTTP_X_FORWARDED_FOR";

    public static final String CODE_EMPTY = "验证码不能为空";
    public static final String CODE_OVER = "验证码已经过期";
    public static final String CODE_ERROR = "验证码错误";
    public static final String LOGIN_ERROR = "用户名或密码错误";
    public static final String ACCOUNT_LOCKED = "该帐号已被锁定";
    public static final String ACCOUNT_EXPIRED = "该帐号已过期";
    public static final String TIPS_BEFORE = "您账号已于（";
    public static final String TIPS_AFTER = "）时间在另一地点登录，您当前已被迫下线！";
    public static final String GET = "get";
    public static final String LOGIN = "login";
    public static final String PC = "pc";
    public static final String AES_ECB = "AES/ECB/NoPadding";
    public static final String EMPTY_CHAR = "";
    public static final String EQ = "=";
    public static final String POST = "post";
    public static final String METHOD = "method";
    public static final String UTF_8 = "UTF-8";
    public static final String AND = "&";
    public static final String SIGNATURE = "&signature=";
    public static final String ANTIURL = "https://nc.baidu.com/pm/check?";
    public static final String APPKEY = "appkey";
    public static final String DATA = "data";
    public static final String TIMESTAMP = "timestamp";
    public static final String MD5 = "MD5";
    public static final Integer HEXFF = 0xFF;
    public static final Integer HEXF = 0xF;
}