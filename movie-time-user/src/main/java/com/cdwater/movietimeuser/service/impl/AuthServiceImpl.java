package com.cdwater.movietimeuser.service.impl;

import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.constants.TextConstant;
import com.cdwater.movietimecommon.enums.ReturnEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import com.cdwater.movietimecommon.utils.AdminContext;
import com.cdwater.movietimecommon.utils.JwtUtil;
import com.cdwater.movietimecommon.utils.RedisUtil;
import com.cdwater.movietimecommon.utils.UserContext;
import com.cdwater.movietimeuser.mapper.AdminMapper;
import com.cdwater.movietimeuser.mapper.UserMapper;
import com.cdwater.movietimeuser.model.dto.*;
import com.cdwater.movietimeuser.model.entity.Admin;
import com.cdwater.movietimeuser.model.entity.User;
import com.cdwater.movietimeuser.model.vo.AdminLoginResponse;
import com.cdwater.movietimeuser.model.vo.AppLoginResponse;
import com.cdwater.movietimeuser.service.AuthService;
import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AuthServiceImpl implements AuthService {

    @Resource
    private AdminMapper adminMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    private JwtUtil jwtUtil;

    @Value("${jwt.admin-token-ttl}")
    private long adminTokenTtl;

    @Value("${jwt.access-token-ttl}")
    private long accessTokenTtl;

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
        //获取token
        String token = header.substring(7);
        //获取剩余过期时间
        Claims claims = jwtUtil.parseToken(token);
        long remainingTime = claims.getExpiration().getTime() - System.currentTimeMillis();
        if (remainingTime <= 0) {
            return;
        }
        //token加入Redis黑名单
        String jti = claims.getId();
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
        redisUtil.saveCode(code, sendCodeDTO.getCodeType(), sendCodeDTO.getPhone());
    }

    @Override
    public AppLoginResponse appPhoneLogin(AppLoginDTO appLoginDTO) {
        String phone = appLoginDTO.getPhone();
        String code = appLoginDTO.getCode();
        //Redis校验验证码
        if (!redisUtil.verifyCode(code, RedisConstant.LOGIN, phone)) {
            throw new BusinessException(ReturnEnum.CODE_ERROR);
        }
        //MySQL查询用户
        User user = userMapper.selectByPhone(phone);
        if (user == null) {
            //用户不存在，静默注册
            user = User.builder()
                    .phone(phone)
                    .nickname(TextConstant.DEFAULT_NICKNAME_PREFIX + RandomStringUtils.randomAlphabetic(6))
                    .build();
            userMapper.insert(user);
        }
        //生成双Token
        AppLoginResponse appLoginResponse = generateTwoToken(user.getId(), phone);
        //返回双Token以及基本用户信息
        appLoginResponse.setNickname(user.getNickname());
        appLoginResponse.setAvatar(user.getAvatar());
        return appLoginResponse;
    }

    @Override
    public AppLoginResponse appPwdLogin(AppLoginDTO appLoginDTO) {
        String phone = appLoginDTO.getPhone();
        String password = appLoginDTO.getPassword();
        //检查Redis登录失败次数，防暴力破解
        redisUtil.checkLoginFailTimes(phone);
        //MySQL查询用户，验证登录
        User user = userMapper.selectByPhone(phone);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            //用户不存在或密码错误
            redisUtil.incrLoginFailTimes(phone);
            throw new BusinessException(ReturnEnum.LOGIN_FAIL);
        }
        //登录成功，清除失败次数
        redisUtil.removeLoginFailTimes(phone);
        //生成双Token
        AppLoginResponse appLoginResponse = generateTwoToken(user.getId(), phone);
        //返回双Token以及基本用户信息
        appLoginResponse.setNickname(user.getNickname());
        appLoginResponse.setAvatar(user.getAvatar());
        return appLoginResponse;
    }

    @Override
    public void appChangePwd(AppChangePwdDTO appChangePwdDTO) {
        //新密码，BCrypt加密
        String newPassword = passwordEncoder.encode(appChangePwdDTO.getNewPassword());
        //更新用户密码
        String phone = UserContext.getPhone();
        userMapper.updatePwdByPhone(phone, newPassword);
    }

    @Override
    public void appForgetPwd(AppForgetPwdDTO appForgetPwdDTO) {
        String phone = appForgetPwdDTO.getPhone();
        String code = appForgetPwdDTO.getCode();
        //Redis校验验证码
        if (!redisUtil.verifyCode(code, RedisConstant.LOGIN, phone)) {
            throw new BusinessException(ReturnEnum.CODE_ERROR);
        }
        //新密码，BCrypt加密
        String newPassword = passwordEncoder.encode(appForgetPwdDTO.getNewPassword());
        //更新用户密码
        userMapper.updatePwdByPhone(phone, newPassword);
    }

    @Override
    public void appLogout(String authHeader) {
        //获取accessToken
        String accessToken = authHeader.substring(7);
        //获取剩余过期时间
        Claims claims = jwtUtil.parseToken(accessToken);
        long remainingTime = claims.getExpiration().getTime() - System.currentTimeMillis();
        if (remainingTime <= 0) {
            return;
        }
        //将accessToken加入Redis黑名单
        String jti = claims.getId();
        redisUtil.joinTokenBlackList(jti, remainingTime, TimeUnit.MILLISECONDS);
        //清除Redis中的refreshToken
        Long userId = UserContext.getId();
        redisUtil.removeRefreshToken(userId);
    }

    @Override
    public AppLoginResponse appRefreshToken(String authHeader, String refreshToken) {
        //请求头为空 | 格式错误
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new BusinessException(ReturnEnum.INVALID_TOKEN);
        }
        //获取accessToken
        String accessToken = authHeader.substring(7);
        //解析accessToken
        Claims claims = jwtUtil.parseTokenAllowExpired(accessToken);
        Long userId = Long.valueOf(claims.getSubject());
        String phone = claims.get("phone", String.class);
        //获取Redis中的refreshToken
        String refreshTokenCache = redisUtil.getRefreshToken(userId);
        //refreshToken不存在 | 错误
        if (refreshTokenCache == null || !StringUtils.equals(refreshTokenCache, refreshToken)) {
            throw new BusinessException(ReturnEnum.INVALID_TOKEN);
        }
        //生成新的双Token并返回
        return generateTwoToken(userId, phone);
    }

    private AppLoginResponse generateTwoToken(Long userId, String phone) {
        //生成accessToken
        Map<String, Object> claims = new HashMap<>();
        claims.put("phone", phone);
        String accessToken = jwtUtil.generateToken(userId, claims, accessTokenTtl);
        //生成refreshToken并存入Redis
        String refreshToken = UUID.randomUUID().toString();
        redisUtil.saveRefreshToken(userId, refreshToken);
        //返回双Token
        return AppLoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
