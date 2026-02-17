package com.cdwater.movietimecommon.constants;

/**
 * Redis常量
 */
public class RedisConstant {

    public static final String TOKEN_BLACKLIST = "token:blacklist";
    public static final String TOKEN_REFRESH = "token:refresh";

    public static final String CODE = "code";
    public static final Integer CODE_TTL = 1;//minute
    public static final String LOGIN = "login";
    public static final String FORGET_PWD = "forget-pwd";
    public static final String LOGIN_FAIL = "login:fail";
    public static final Integer LOGIN_FAIL_TTL = 2;//minute

    public static final String CINEMA_GEO = "cinema:geo";


}
