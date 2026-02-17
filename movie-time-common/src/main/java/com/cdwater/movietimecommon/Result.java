package com.cdwater.movietimecommon;

import com.cdwater.movietimecommon.enums.ReturnEnum;
import lombok.Data;

/**
 * 统一响应结果
 */
@Data
public class Result {

    private Integer code;
    private String msg;
    private Object data;

    public static Result success() {
        Result result = new Result();
        result.setCode(ReturnEnum.SUCCESS_REQUEST.getCode());
        result.setMsg(ReturnEnum.SUCCESS_REQUEST.getMsg());
        return result;
    }

    public static Result success(Object data) {
        Result result = success();
        result.setData(data);
        return result;
    }

    public static Result fail() {
        Result result = new Result();
        result.setCode(ReturnEnum.SYSTEM_ERROR.getCode());
        result.setMsg(ReturnEnum.SYSTEM_ERROR.getMsg());
        return result;
    }

    public static Result fail(Integer code, String msg) {
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
