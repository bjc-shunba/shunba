package com.baidu.shunba.constant;

import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * @ClassName BcryptUtil
 * @Deacription Bcrypt加密方法
 * @Author zsh
 * @Date 2019/12/08  10:38:32
 * @Version 1.0
 **/
public class BcryptUtil {

    public static String setBcrypt(String passWord) {
        return BCrypt.hashpw(passWord, BCrypt.gensalt());
    }

}