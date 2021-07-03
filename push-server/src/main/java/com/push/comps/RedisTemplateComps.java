package com.push.comps;

import com.push.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


/**
 * redis工具类
 */
@Component
public class RedisTemplateComps {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void set(String key, String value) {
        stringRedisTemplate.boundValueOps(key).set(value);
    }

    public String get(String key) {
        return stringRedisTemplate.boundValueOps(key).get();
    }

    public String getDeviceToken(String uid) {
        return stringRedisTemplate.boundValueOps(String.format(Constants.APNS_DEVICE_TOKEN, uid)).get();
    }

    public void openApns(String uid, String deviceToken) {
        stringRedisTemplate.boundValueOps(String.format(Constants.APNS_DEVICE_TOKEN, uid)).set(deviceToken);
    }

    public void closeApns(String uid) {
        stringRedisTemplate.delete(String.format(Constants.APNS_DEVICE_TOKEN, uid));
    }


}
