package cn.howardliu.gear.logback.appender.kafka;

import cn.howardliu.gear.kafka.KafkaProducerWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <br>created at 16-12-20
 *
 * @author liuxh
 * @since 1.0.1
 */
public class KafkaProducerWrapperTest2 {
    private KafkaProducerWrapper<String, String> wrapper;

    @Before
    public void before() throws Exception {
        wrapper = new KafkaProducerWrapper<>("10.6.100.4:9092,10.6.100.5:9092,10.6.100.6:9092");
    }

    @Test
    public void testSend() throws Exception {
        wrapper.send("kafka-appender-topic", null, "this is a test, the timestamp is " + System.currentTimeMillis());
    }

    @After
    public void after() throws Exception {
        wrapper.close();
    }
}