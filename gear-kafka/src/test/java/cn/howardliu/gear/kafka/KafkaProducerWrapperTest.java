package cn.howardliu.gear.kafka;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * <br>created at 16-12-20
 *
 * @author liuxh
 * @since 1.0.1
 */
@Ignore
public class KafkaProducerWrapperTest {
    private KafkaProducerWrapper<String, String> wrapper;

    @Before
    public void before() {
        Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, "192.168.7.137:9092,192.168.7.138:9092,192.168.7.139:9092");
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
        properties.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        wrapper = new KafkaProducerWrapper<>(properties);
    }

    @Test
    public void testSend() {
        for (int i = 0; i < 10; i++) {
            System.out.println(wrapper.send("kafka-appender-topic", null, "测试"));
        }
    }
    @After
    public void after() throws Exception {
        wrapper.close();
    }
}