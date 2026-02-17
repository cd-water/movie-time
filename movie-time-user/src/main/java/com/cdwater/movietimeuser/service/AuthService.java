package com.cdwater.movietimeuser.service;

import com.cdwater.movietimeuser.model.dto.*;
import com.cdwater.movietimeuser.model.vo.AdminLoginResponse;
import com.cdwater.movietimeuser.model.vo.AppLoginResponse;

public interface AuthService {

    /**
     * Admin端
     */
    AdminLoginResponse adminLogin(AdminLoginDTO adminLoginDTO);

    void adminLogout(String header);

    void adminChangePwd(AdminChangePwdDTO adminChangePwdDTO, String header);

    /**
     * App端
     */
    void appSendCode(SendCodeDTO sendCodeDTO);

    AppLoginResponse appPhoneLogin(AppLoginDTO appLoginDTO);

    AppLoginResponse appPwdLogin(AppLoginDTO appLoginDTO);

    void appChangePwd(AppChangePwdDTO appChangePwdDTO);

    void appForgetPwd(AppForgetPwdDTO appForgetPwdDTO);

    void appLogout(String authHeader);

    AppLoginResponse appRefreshToken(String authHeader, String refreshToken);
}
