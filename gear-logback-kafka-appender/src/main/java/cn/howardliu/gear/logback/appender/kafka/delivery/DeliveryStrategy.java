package cn.howardliu.gear.logback.appender.kafka.delivery;

import cn.howardliu.gear.kafka.KafkaProducerWrapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * <br>created at 16-12-22
 *
 * @author liuxh
 * @since 1.0.1
 */
public interface DeliveryStrategy<K, V, E> {
    boolean send(KafkaProducerWrapper<K, V> wrapper, String topic, K key, V value, E event, Callback<E> callback);

    public static interface Callback<E> {
        void execute(E evt, Throwable throwable);
    }
}
