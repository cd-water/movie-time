package com.cdwater.movietimeuser.service.impl;

import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.enums.ReturnEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.AdminContext;
import com.cdwater.movietimecommon.utils.JwtUtil;
import com.cdwater.movietimecommon.utils.RedisUtil;
import com.cdwater.movietimeuser.mapper.AdminMapper;
import com.cdwater.movietimeuser.model.dto.*;
import com.cdwater.movietimeuser.model.entity.Admin;
import com.cdwater.movietimeuser.model.vo.AdminLoginResponse;
import com.cdwater.movietimeuser.model.vo.AppLoginResponse;
import com.cdwater.movietimeuser.service.AuthService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtUtil jwtUtil;

    @Value("${jwt.admin-token-ttl}")
    private long adminTokenTtl;

    @Resource
    private RedisUtil redisUtil;

    @Override
    public AdminLoginResponse adminLogin(AdminLoginDTO adminLoginDTO) {
        Admin admin = adminMapper.selectByUsername(adminLoginDTO.getUsername());
        if (admin == null || !passwordEncoder.matches(adminLoginDTO.getPassword(), admin.getPassword())) {
            //用户名或密码错误
            throw new BusinessException(ReturnEnum.LOGIN_FAIL);
        }
        //生成token
        Map<String, Object> claims = new HashMap<>();
        claims.put("top", admin.getTop());
        String token = jwtUtil.generateToken(admin.getId(), claims, adminTokenTtl);
        //封装返回对象
        return AdminLoginResponse.builder()
                .id(admin.getId())
                .username(admin.getUsername())
                .top(admin.getTop())
                .token(token)
                .build();
    }

    @Override
    public void adminLogout(String header) {
        //解析token
        String token = header.substring(7);
        //获取剩余过期时间
        long remainingTime = jwtUtil.getTokenRemainingTime(token);
        if (remainingTime <= 0) {
            return;
        }
        //token加入Redis黑名单
        String jti = jwtUtil.getJti(token);
        redisUtil.joinTokenBlackList(jti, remainingTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public void adminChangePwd(AdminChangePwdDTO adminChangePwdDTO, String header) {
        //校验原密码
        Long id = AdminContext.getId();
        Admin admin = adminMapper.selectById(id);
        if (admin == null || !passwordEncoder.matches(adminChangePwdDTO.getOldPassword(), admin.getPassword())) {
            //原密码错误
            throw new BusinessException(ReturnEnum.OLD_PASSWORD_ERROR);
        }
        //新密码，BCrypt加密
        String newPassword = passwordEncoder.encode(adminChangePwdDTO.getNewPassword());
        adminMapper.updatePwdById(id, newPassword);
        //重新登录
        adminLogout(header);
    }

    @Override
    public void appSendCode(SendCodeDTO sendCodeDTO) {
        //生成验证码
        String code = RandomStringUtils.randomNumeric(6);
        //发送验证码 TODO后续开启短信服务

        //验证码存入Redis
        String codeType = sendCodeDTO.getCodeType();
        String phone = sendCodeDTO.getPhone();
        String key = RedisConstant.CODE + ":" + codeType + ":" + phone;
        redisUtil.saveCode(key, code, RedisConstant.CODE_TTL, TimeUnit.MINUTES);
    }

    @Override
    public AppLoginResponse appPhoneLogin(AppLoginDTO appLoginDTO) {
        return null;
    }

    @Override
    public AppLoginResponse appPwdLogin(AppLoginDTO appLoginDTO) {
        return null;
    }

    @Override
    public void appChangePwd(AppChangePwdDTO appChangePwdDTO) {

    }

    @Override
    public void appForgetPwd(AppForgetPwdDTO appForgetPwdDTO) {

    }

    @Override
    public void appLogout(String authHeader) {

    }

    @Override
    public AppLoginResponse appRefreshToken(String authHeader, String refreshToken) {
        return null;
    }
}
