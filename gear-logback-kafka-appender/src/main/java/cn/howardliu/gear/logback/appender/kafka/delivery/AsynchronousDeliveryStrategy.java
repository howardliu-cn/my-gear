package cn.howardliu.gear.logback.appender.kafka.delivery;

import cn.howardliu.gear.kafka.KafkaProducerWrapper;
import org.apache.kafka.clients.producer.BufferExhaustedException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

/**
 * <br>created at 16-12-22
 *
 * @author liuxh
 * @since 1.0.1
 */
public class AsynchronousDeliveryStrategy<K, V, E> implements DeliveryStrategy<K, V, E> {
    @Override
    public boolean send(KafkaProducerWrapper<K, V> wrapper, String topic, K key, V value, final E event,
            final Callback<E> callback) {
        try {
            wrapper.send(topic, key, value, new KafkaProducerWrapper.Callback<K, V>() {
                @Override
                public void execute(K key, V value, RecordMetadata metadata, Exception exception) {
                    if (exception != null) {
                        callback.execute(event, exception);
                    }
                }
            });
            return true;
        } catch (BufferExhaustedException e) {
            callback.execute(event, e);
            return false;
        }
    }
}
