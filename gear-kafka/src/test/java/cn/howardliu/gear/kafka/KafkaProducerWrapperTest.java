package cn.howardliu.gear.kafka;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * <br>created at 16-12-20
 *
 * @author liuxh
 * @since 1.0.1
 */
public class KafkaProducerWrapperTest {
    private KafkaProducerWrapper wrapper;

    @Before
    public void before() throws Exception {
        Properties properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, "10.6.2.56:9092,10.6.2.57:9092,10.6.2.58:9092");
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
        wrapper = new KafkaProducerWrapper(properties);
    }

    @Test
    public void testSend() throws Exception {
        for (int i = 0; i < 100; i++) {
            System.out.println(
                    KafkaProducerWrapper.send("mq-job-topic-dev",
                            "job-key-" + i % 20,
                            "{id:" + i + "}")
            );
        }
    }
    @After
    public void after() throws Exception {
        wrapper.close();
    }
}