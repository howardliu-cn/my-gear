package cn.howardliu.gear.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Properties;

/**
 * <br>created at 16-12-20
 *
 * @author liuxh
 * @since 1.0.1
 */
public class KafkaProducerWrapper<K, V> implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerWrapper.class);
    private KafkaProducer<K, V> kafkaProducer;

    public KafkaProducerWrapper(Properties config) {
        kafkaProducer = new KafkaProducerFactory<K, V>(config).build();
    }

    public KafkaProducerWrapper(String bootstrapServers) {
        kafkaProducer = new KafkaProducerFactory<K, V>(bootstrapServers).build();
    }

    public boolean send(String topic, K key, V msg) {
        return send(topic, key, msg, new Callback<K, V>() {
            @Override
            public void execute(K key, V value, RecordMetadata metadata, Exception exception) {
                if (exception == null) {
                    logger.debug("producer发送消息成功，topic={}, partition={}, offset={}",
                            metadata.topic(), metadata.partition(), metadata.offset());
                } else {
                    logger.error("producer发送消息失败", exception);
                }
            }
        });
    }

    public boolean send(final String topic, final K key, final V value, final Callback<K, V> callback) {
        try {
            ProducerRecord<K, V> record;
            if (key == null) {
                record = new ProducerRecord<>(topic, value);
            } else {
                record = new ProducerRecord<>(topic, key, value);
            }
            kafkaProducer.send(record, new org.apache.kafka.clients.producer.Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    callback.execute(key, value, metadata, exception);
                }
            });
            return true;
        } catch (Exception e) {
            logger.error("消息发送失败, topic={}, key={}, value={}", topic, key, value, e);
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        kafkaProducer.close();
    }

    public static interface Callback<K, V> {
        void execute(K key, V value, RecordMetadata metadata, Exception exception);
    }
}
