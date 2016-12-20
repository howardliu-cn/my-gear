package cn.howardliu.gear.kafka;

import org.apache.kafka.clients.producer.Callback;
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
public class KafkaProducerWrapper implements Closeable {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerWrapper.class);
    private static KafkaProducer<String, String> kafkaProducer;

    public KafkaProducerWrapper(Properties config) {
        kafkaProducer = new KafkaProducerFactory<String, String>(config).build();
    }

    public KafkaProducerWrapper(String bootstrapServers) {
        kafkaProducer = new KafkaProducerFactory<String, String>(bootstrapServers).build();
    }

    public static boolean send(String topic, String key, String msg) {
        try {
            ProducerRecord<String, String> record;
            if (key == null) {
                record = new ProducerRecord<>(topic, msg);
            } else {
                record = new ProducerRecord<>(topic, key, msg);
            }
            kafkaProducer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        logger.debug("producer发送消息成功，topic={}, partition={}, offset={}",
                                metadata.topic(), metadata.partition(), metadata.offset());
                    } else {
                        logger.error("producer发送消息失败", exception);
                    }
                }
            });
            return true;
        } catch (Exception e) {
            logger.error("消息发送失败, topic={}, key={}, msg={}", topic, key, msg, e);
        }
        return false;
    }

    @Override
    public void close() throws IOException {
        kafkaProducer.close();
    }
}
