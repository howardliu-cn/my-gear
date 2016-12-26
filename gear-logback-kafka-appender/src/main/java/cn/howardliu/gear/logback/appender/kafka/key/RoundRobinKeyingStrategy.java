package cn.howardliu.gear.logback.appender.kafka.key;

/**
 * <br>created at 16-12-21
 *
 * @author liuxh
 * @since 1.0.1
 */
public class RoundRobinKeyingStrategy<K, E> implements KeyingStrategy<K, E> {
    @Override
    public K key(E e) {
        return null;
    }
}
