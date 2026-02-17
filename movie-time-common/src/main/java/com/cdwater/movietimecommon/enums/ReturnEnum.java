package com.cdwater.movietimecommon.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回信息枚举
 */
@AllArgsConstructor
@Getter
public enum ReturnEnum {

    SUCCESS_REQUEST(200, "请求成功"),
    ILLEGAL_INPUT(400, "非法输入"),
    INVALID_TOKEN(401, "凭证无效或已过期"),
    FORBIDDEN_ACCESS(403, "无访问权限"),
    NOT_FOUND(404, "资源未找到"),
    SYSTEM_ERROR(500, "系统异常"),

    LOGIN_FAIL(666, "账号或密码错误，请重新输入"),
    OLD_PASSWORD_ERROR(666, "原密码错误，请重新输入"),
    ACCOUNT_EXIST(666, "账号已存在"),
    UPLOAD_FAIL(666, "文件上传失败"),
    SYSTEM_BUSY(666, "系统繁忙，请稍后再试"),
    CODE_ERROR(666, "验证码错误，请重新输入"),
    LOGIN_TOO_FREQUENT(666, "登录频繁，请稍后再试");

    //    SEND_CODE_FAIL(666, "发送验证码失败"),


//    SYSTEM_BUSY(666, "系统繁忙，请稍后再试"),
//    SEAT_LOCKED(666, "座位已锁定"),


    private final Integer code;
    private final String msg;
}