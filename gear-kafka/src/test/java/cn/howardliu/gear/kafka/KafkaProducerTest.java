package cn.howardliu.gear.kafka;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * <br>created at 16-8-12
 *
 * @author liuxh
 * @since 1.0.0
 */
@Ignore
public class KafkaProducerTest {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerTest.class);
    private KafkaProducer<String, String> kafkaProducer;

    @Before
    public void before() throws Exception {
        Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, "10.6.100.4:9092,10.6.100.5:9092,10.6.100.6:9092");
        properties.put(ACKS_CONFIG, "all");// 0, 1, all
        properties.put(BUFFER_MEMORY_CONFIG, "33554432");
        properties.put(COMPRESSION_TYPE_CONFIG, "none");// none, gzip, snappy
        properties.put(RETRIES_CONFIG, "0");
        properties.put(BATCH_SIZE_CONFIG, "16384");
        properties.put(CLIENT_ID_CONFIG, "job-client-id");
        properties.put(LINGER_MS_CONFIG, "0");
        properties.put(MAX_REQUEST_SIZE_CONFIG, "1048576");
        properties.put(RECEIVE_BUFFER_CONFIG, "32768");
        properties.put(SEND_BUFFER_CONFIG, "131072");
        properties.put(TIMEOUT_CONFIG, "30000");
        properties.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        kafkaProducer = new KafkaProducer<>(properties);
    }

    @After
    public void after() throws Exception {
        kafkaProducer.close();
    }

    @Test
    public void test() throws Exception {
        for (int i = 0; i < 1; i++) {
            ProducerRecord<String, String> topic = new ProducerRecord<>("kafka-appender-topic",
                    "job-key-" + i % 20,
                    "{id:" + i + "}");
            kafkaProducer.send(topic, new Callback() {
                @Override
                public void onCompletion(RecordMetadata metadata, Exception exception) {
                    if (exception == null) {
                        logger.info("topic={}, partition={}, offset={}",
                                metadata.topic(), metadata.partition(), metadata.offset());
                    } else {
                        logger.error("producer发送消息失败", exception);
                    }
                }
            });
        }
    }
}
