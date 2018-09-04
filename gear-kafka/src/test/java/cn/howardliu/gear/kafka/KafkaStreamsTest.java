package cn.howardliu.gear.kafka;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.CountDownLatch;

/**
 * <br>created at 18-8-27
 *
 * @author liuxh
 * @since 1.0.0
 */
public class KafkaStreamsTest {
    private static final Logger logger = LoggerFactory.getLogger(KafkaStreamsTest.class);
    private Properties props;

    @Before
    public void before() {
        props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "jobKey");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "192.168.7.137:9092,192.168.7.138:9092,192.168.7.139:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
    }

    @Test
    public void test() {
        final StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> source = builder.stream("kafka-appender-topic");
        source.foreach((key, value) -> System.out.println("key: " + key + ", value: " + value));

        final Topology topology = builder.build();
        final KafkaStreams streams = new KafkaStreams(topology, props);
        final CountDownLatch latch = new CountDownLatch(1);

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                streams.close();
                latch.countDown();
            }
        });

        try {
            streams.start();
            latch.await();
        } catch (Throwable e) {
            System.exit(1);
        }
        System.exit(0);
    }
}
