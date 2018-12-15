package cn.howardliu.gear.springEx.redis;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * <br>created at 18-1-25
 *
 * @author liuxh
 * @since 0.1.0
 */
@Service("redisCacheService")
public class RedisCacheService implements CacheService {
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取输入字符串的utf-8编码格式的byte数组
     *
     * @return utf-8编码格式的byte数组,如果输入字符串为空,返回空数组
     */
    private static byte[] bytes(String s) {
        if (s == null) {
            return new byte[]{};
        }
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private static String str(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public String set(String k, String v) {
        byte[] key = bytes(k);
        byte[] value = bytes(v == null ? NULL_VALUE : v);
        return stringRedisTemplate.execute((RedisCallback<String>) redisConnection -> {
            redisConnection.set(key, value);
            return v;
        });
    }

    /**
     * 以k-v方式将数据存储到Redis中
     *
     * @param k   键
     * @param v   值对象,将对象转为json格式存储,如果v为空,则存储@IDO
     * @param <T> 值对象类
     * @return 值类
     */
    @Override
    public <T> T set(String k, T v) {
        byte[] key = bytes(k);
        byte[] value = v == null ? bytes(NULL_VALUE) : bytes(JSON.toJSONString(v));
        return stringRedisTemplate.execute((RedisCallback<T>) redisConnection -> {
            redisConnection.set(key, value);
            return v;
        });
    }

    @Override
    public String setEx(String k, String v, long time) {
        byte[] key = bytes(k);
        byte[] value = bytes(v == null ? NULL_VALUE : v);
        return stringRedisTemplate.execute((RedisCallback<String>) redisConnection -> {
            redisConnection.setEx(key, time, value);
            return v;
        });
    }

    /**
     * 以k-v方式将数据存储到Redis中,过期时间为time,单位秒
     *
     * @param k    键
     * @param v    值,如果v为空,则存储@IDO
     * @param time 过期时间,单位秒
     * @param <T>  对象
     * @return 对象
     */
    @Override
    public <T> T setEx(String k, T v, long time) {
        byte[] key = bytes(k);
        byte[] value = v == null ? bytes(NULL_VALUE) : bytes(JSON.toJSONString(v));
        return stringRedisTemplate.execute((RedisCallback<T>) redisConnection -> {
            redisConnection.setEx(key, time, value);
            return v;
        });
    }

    @Override
    public boolean setNX(String k, String v) {
        byte[] key = bytes(k);
        byte[] value = bytes(v == null ? NULL_VALUE : v);
        return stringRedisTemplate
                .execute((RedisCallback<Boolean>) redisConnection -> redisConnection.setNX(key, value));
    }

    @Override
    public void expire(String k, long expire) {
        if (StringUtils.isBlank(k)) {
            return;
        }
        byte[] key = bytes(k);
        stringRedisTemplate.execute((RedisCallback<Boolean>) connection -> {
            connection.expire(key, expire);
            return Boolean.TRUE;
        });
    }

    @Override
    public Optional<String> get(String k) {
        if (k == null) {
            return Optional.empty();
        }
        byte[] key = bytes(k);
        String value = stringRedisTemplate.execute((RedisCallback<String>) connection -> {
            byte[] bytes = connection.get(key);
            return (bytes == null) ? null : str(bytes);
        });
        if (NULL_VALUE.equals(value)) {
            value = null;
        }
        return Optional.ofNullable(value);
    }

    @Override
    public <T> Optional<T> get(String k, Class<T> clazz) {
        if (k == null) {
            return Optional.empty();
        }
        byte[] key = bytes(k);
        String value = stringRedisTemplate.execute((RedisCallback<String>) connection -> {
            byte[] bytes = connection.get(key);
            return (bytes == null) ? null : str(bytes);
        });
        if (NULL_VALUE.equals(value)) {
            value = null;
        }
        return value == null ? Optional.empty() : Optional.ofNullable(JSON.parseObject(value, clazz));
    }

    @Override
    public Long del(String k) {
        byte[] key = bytes(k);
        return stringRedisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.del(key));
    }
}
