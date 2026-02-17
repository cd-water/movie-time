package com.cdwater.movietimecommon.utils;

import com.cdwater.movietimecommon.constants.RedisConstant;
import jakarta.annotation.Resource;
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

    public void joinTokenBlackList(String jti, long ttl, TimeUnit timeUnit) {
        String key = RedisConstant.TOKEN_BLACKLIST + ":" + jti;
        redisTemplate.opsForValue().set(key, "", ttl, timeUnit);
    }

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

    public void saveCode(String key,String code, long ttl, TimeUnit timeUnit){
        redisTemplate.opsForValue().set(key, code, ttl, timeUnit);
    }
}
