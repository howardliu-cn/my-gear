package cn.howardliu.gear.logback.appender.kafka.key;

/**
 * <br>created at 16-12-21
 *
 * @author liuxh
 * @since 1.0.1
 */
public interface KeyingStrategy<K, E> {
    K key(E e);
}
