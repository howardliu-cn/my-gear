package cn.howardliu.gear.logback.appender.kafka.encode;

/**
 * <br>created at 16-12-21
 *
 * @author liuxh
 * @since 1.0.1
 */
public interface KafkaMessageEncoder<E> {
    String encode(E event);
}
