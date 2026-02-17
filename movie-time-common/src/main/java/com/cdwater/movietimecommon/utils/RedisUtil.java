package com.cdwater.movietimecommon.utils;

import com.cdwater.movietimecommon.constants.RedisConstant;
import com.cdwater.movietimecommon.enums.ReturnEnum;
import com.cdwater.movietimecommon.exceptions.BusinessException;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * Redis工具
 */
@Component
public class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.refresh-token-ttl}")
    private long refreshTokenTtl;

    /**
     * 保存验证码
     *
     * @param code     保存的验证码
     * @param codeType 验证码类型
     * @param phone    对应的手机号
     */
    public void saveCode(String code, String codeType, String phone) {
        String key = RedisConstant.CODE + ":" + codeType + ":" + phone;
        redisTemplate.opsForValue().set(key, code, RedisConstant.CODE_TTL, TimeUnit.MINUTES);
    }

    /**
     * 验证验证码
     *
     * @param codeInput 输入的验证码
     * @param codeType  验证码类型
     * @param phone     对应的手机号
     * @return true:验证码正确 false:验证码错误
     */
    public boolean verifyCode(String codeInput, String codeType, String phone) {
        String key = RedisConstant.CODE + ":" + codeType + ":" + phone;
        String codeCache = (String) redisTemplate.opsForValue().get(key);
        //验证码已过期 | 错误
        if (codeCache == null || !StringUtils.equals(codeCache, codeInput)) {
            return false;
        }
        //删除Redis中的验证码，避免重复使用
        redisTemplate.delete(key);
        return true;
    }

    /**
     * 保存刷新token
     *
     * @param userId 持有用户id
     * @param token  刷新token
     */
    public void saveRefreshToken(Long userId, String token) {
        String key = RedisConstant.TOKEN_REFRESH + ":" + userId;
        redisTemplate.opsForValue().set(key, token, refreshTokenTtl, TimeUnit.MILLISECONDS);
    }

    /**
     * 获取刷新token
     *
     * @param userId 持有用户id
     * @return 刷新token
     */
    public String getRefreshToken(Long userId) {
        String key = RedisConstant.TOKEN_REFRESH + ":" + userId;
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 删除刷新token
     *
     * @param userId 持有用户id
     */
    public void removeRefreshToken(Long userId) {
        String key = RedisConstant.TOKEN_REFRESH + ":" + userId;
        redisTemplate.delete(key);
    }

    /**
     * 检查登录失败次数
     *
     * @param phone 对应的手机号
     */
    public void checkLoginFailTimes(String phone) {
        String key = RedisConstant.LOGIN_FAIL + ":" + phone;
        Integer failCount = (Integer) redisTemplate.opsForValue().get(key);
        if (failCount != null && failCount >= 5) {
            throw new BusinessException(ReturnEnum.LOGIN_TOO_FREQUENT);
        }
    }

    /**
     * 登录失败次数+1
     *
     * @param phone 对应的手机号
     */
    public void incrLoginFailTimes(String phone) {
        String key = RedisConstant.LOGIN_FAIL + ":" + phone;
        redisTemplate.opsForValue().increment(key);
        redisTemplate.expire(key, RedisConstant.LOGIN_FAIL_TTL, TimeUnit.MINUTES);
    }

    /**
     * 清除登录失败次数
     *
     * @param phone 对应的手机号
     */
    public void removeLoginFailTimes(String phone) {
        String key = RedisConstant.LOGIN_FAIL + ":" + phone;
        redisTemplate.delete(key);
    }

    /**
     * 将token加入黑名单
     *
     * @param jti      jwt唯一标识
     * @param ttl      过期时间
     * @param timeUnit 时间单位
     */
    public void joinTokenBlackList(String jti, long ttl, TimeUnit timeUnit) {
        String key = RedisConstant.TOKEN_BLACKLIST + ":" + jti;
        redisTemplate.opsForValue().set(key, "", ttl, timeUnit);
    }

    /**
     * 判断token是否在黑名单
     *
     * @param jti jwt唯一标识
     * @return true:在黑名单 false:不在黑名单
     */
    public boolean existTokenBlackList(String jti) {
        String key = RedisConstant.TOKEN_BLACKLIST + ":" + jti;
        return redisTemplate.hasKey(key);
    }


    public void addGeo(String key, Object member, double longitude, double latitude) {
        Point point = new Point(longitude, latitude);
        redisTemplate.opsForGeo().add(key, point, member);
    }

    public void removeGeo(String key, Object... members) {
        redisTemplate.opsForGeo().remove(key, members);
    }
}
