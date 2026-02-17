package com.cdwater.movietimeuser.controller.app;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimeuser.model.dto.AppChangePwdDTO;
import com.cdwater.movietimeuser.model.dto.AppForgetPwdDTO;
import com.cdwater.movietimeuser.model.dto.AppLoginDTO;
import com.cdwater.movietimeuser.model.dto.SendCodeDTO;
import com.cdwater.movietimeuser.model.vo.AppLoginResponse;
import com.cdwater.movietimeuser.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController("appAuthController")
@RequestMapping("/app/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/send-code")
    public Result sendCode(@RequestBody @Valid SendCodeDTO sendCodeDTO) {
        authService.appSendCode(sendCodeDTO);
        return Result.success();
    }

    @PostMapping("/phone-login")
    public Result phoneLogin(@RequestBody @Valid AppLoginDTO appLoginDTO) {
        AppLoginResponse loginResponse = authService.appPhoneLogin(appLoginDTO);
        return Result.success(loginResponse);
    }

    @PostMapping("/pwd-login")
    public Result pwdLogin(@RequestBody @Valid AppLoginDTO appLoginDTO) {
        AppLoginResponse loginResponse = authService.appPwdLogin(appLoginDTO);
        return Result.success(loginResponse);
    }

    @PutMapping("/change-pwd")
    public Result changePwd(@RequestBody @Valid AppChangePwdDTO appChangePwdDTO) {
        authService.appChangePwd(appChangePwdDTO);
        return Result.success();
    }

    @PostMapping("/forget-pwd")
    public Result forgetPwd(@RequestBody @Valid AppForgetPwdDTO appForgetPwdDTO) {
        authService.appForgetPwd(appForgetPwdDTO);
        return Result.success();
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String authHeader) {
        authService.appLogout(authHeader);
        return Result.success();
    }

    @PostMapping("/refresh-token")
    public Result refreshToken(@RequestHeader("Authorization") String authHeader,
                               @RequestHeader("X-Refresh-Token") String refreshToken) {
        AppLoginResponse appLoginResponse = authService.appRefreshToken(authHeader, refreshToken);
        return Result.success(appLoginResponse);
    }
}
