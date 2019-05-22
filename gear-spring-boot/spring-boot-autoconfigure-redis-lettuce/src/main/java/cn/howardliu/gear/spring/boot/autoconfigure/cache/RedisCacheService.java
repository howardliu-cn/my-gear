package cn.howardliu.gear.spring.boot.autoconfigure.cache;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * <br>created at 18-1-25
 *
 * @author liuxh
 * @since 0.1.0
 */
public class RedisCacheService implements CacheService {
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheService.class);
    private StringRedisTemplate stringRedisTemplate;

    public RedisCacheService(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
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
        Boolean result = stringRedisTemplate.execute((RedisCallback<Boolean>) redisConnection -> redisConnection.setNX(key, value));
        return result == null ? false : result;
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
        String value = get(k).orElse(null);
        return value == null ? Optional.empty() : Optional.ofNullable(JSON.parseObject(value, clazz));
    }

    @Override
    public Long del(String k) {
        byte[] key = bytes(k);
        return stringRedisTemplate.execute((RedisCallback<Long>) redisConnection -> redisConnection.del(key));
    }

    private byte[] bytes(String s) {
        if (s == null) {
            return new byte[]{};
        }
        return s.getBytes(StandardCharsets.UTF_8);
    }

    private String str(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }
}
