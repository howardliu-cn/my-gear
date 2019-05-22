package cn.howardliu.gear.spring.boot.autoconfigure.cache;

import java.util.Optional;

public interface CacheService {
    String NULL_VALUE = "@GEAR";

    /**
     * 以k-v方式将数据存储到缓存中
     *
     * @param k 键
     * @param v 值
     * @return 字符串
     */
    String set(final String k, final String v);


    /**
     * 以k-v方式将数据存储到缓存中
     *
     * @param k   键
     * @param v   值
     * @param <T> 对象
     * @return 对象
     */
    <T> T set(final String k, final T v);

    /**
     * 以k-v方式将数据存储到缓存中,过期时间为time,单位秒
     *
     * @param k    键
     * @param v    值
     * @param time 过期时间,单位秒
     * @return 对象
     */
    String setEx(final String k, final String v, final long time);

    /**
     * 以k-v方式将数据存储到缓存中,过期时间为time,单位秒
     *
     * @param k    键
     * @param v    值
     * @param time 过期时间,单位秒
     * @param <T>  对象
     * @return 对象
     */
    <T> T setEx(final String k, final T v, final long time);

    /**
     * 以k-v方式将数据存储到缓存中
     *
     * @param k 键
     * @param v 值
     * @return 如果缓存已经存在，则返回false
     */
    boolean setNX(final String k, final String v);

    /**
     * 将键为k的过期时间设置为expire秒
     *
     * @param k      键
     * @param expire 过期时间
     */
    void expire(final String k, final long expire);

    /**
     * 根据键值k获取存储字符串
     *
     * @param k 键值
     * @return 字符串
     */
    Optional<String> get(final String k);

    /**
     * 根据键值k获取存储对象
     *
     * @param k 键值
     * @return 存储对象
     */
    <T> Optional<T> get(final String k, Class<T> clazz);

    /**
     * 根据键值k删除存储对象
     *
     * @param k 键值
     * @return 影响行数
     */
    Long del(final String k);
}
