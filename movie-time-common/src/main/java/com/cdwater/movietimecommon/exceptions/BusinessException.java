package com.cdwater.movietimecommon.exceptions;

import com.cdwater.movietimecommon.enums.ReturnEnum;
import lombok.Getter;

/**
 * 业务异常
 */
@Getter
public class BusinessException extends RuntimeException {

    private final Integer code;
    private final String msg;

    public BusinessException(ReturnEnum returnEnum) {
        this.code = returnEnum.getCode();
        this.msg = returnEnum.getMsg();
    }

    @Override
    public String toString() {
        return "BusinessException{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
