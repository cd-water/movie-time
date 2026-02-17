package com.cdwater.movietimeuser.controller.admin;

import com.cdwater.movietimecommon.Result;
import com.cdwater.movietimeuser.model.dto.AdminChangePwdDTO;
import com.cdwater.movietimeuser.model.dto.AdminLoginDTO;
import com.cdwater.movietimeuser.model.vo.AdminLoginResponse;
import com.cdwater.movietimeuser.service.AuthService;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController("adminAuthController")
@RequestMapping("/admin/auth")
public class AuthController {

    @Resource
    private AuthService authService;

    @PostMapping("/login")
    public Result login(@Valid @RequestBody AdminLoginDTO adminLoginDTO) {
        AdminLoginResponse response = authService.adminLogin(adminLoginDTO);
        return Result.success(response);
    }

    @PostMapping("/logout")
    public Result logout(@RequestHeader("Authorization") String header) {
        authService.adminLogout(header);
        return Result.success();
    }

    @PostMapping("/change-pwd")
    public Result changePwd(@Valid @RequestBody AdminChangePwdDTO adminChangePwdDTO,
                            @RequestHeader("Authorization") String header) {
        authService.adminChangePwd(adminChangePwdDTO,header);
        return Result.success();
    }
}
