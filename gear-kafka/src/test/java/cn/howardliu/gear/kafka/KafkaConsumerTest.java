package cn.howardliu.gear.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;

/**
 * <br>created at 16-8-12
 *
 * @author liuxh
 * @since 1.0.0
 */
@Ignore
public class KafkaConsumerTest {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerTest.class);
    private Properties properties;
    private volatile boolean running = true;

    @Before
    public void before() throws Exception {
        properties = new Properties();
        properties.put(BOOTSTRAP_SERVERS_CONFIG, "192.168.7.137:9092,192.168.7.138:9092,192.168.7.139:9092");
        properties.put(FETCH_MAX_BYTES_CONFIG, "1048576");
        properties.put(GROUP_ID_CONFIG, "jobKey");
        properties.put(ENABLE_AUTO_COMMIT_CONFIG, "false");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");// latest, earliest, none
    }

    @Test
    public void test1() {
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Arrays.asList("kafka-appender-topic"));
        while (running) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }

        Runtime.getRuntime().addShutdownHook(new Thread("streams-shutdown-hook") {
            @Override
            public void run() {
                running = false;
            }
        });
    }


    public void consumer() throws Exception {


//        final ConsumerConnector connector = new KafkaConsumerFactory(properties).build();
//        Map<String, Integer> topicCountMap = new HashMap<>();
//        topicCountMap.put("mq-job-topic-dev", 2);
//        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = connector.createMessageStreams(topicCountMap);
//        List<KafkaStream<byte[], byte[]>> kafkaStreams = messageStreams.get("mq-job-topic-dev");
//        ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactory() {
//            @Override
//            public Thread newThread(Runnable r) {
//                Thread t = new Thread(r);
//                t.setDaemon(true);
//                return t;
//            }
//        });
//        for (final KafkaStream<byte[], byte[]> kafkaStream : kafkaStreams) {
//            executorService.submit(new Runnable() {
//                @Override
//                public void run() {
//                    for (MessageAndMetadata<byte[], byte[]> messageAndMetadata : kafkaStream) {
//                        try {
//                            String key = new String(messageAndMetadata.key(), "UTF-8");
//                            if (key.startsWith("job-key")) {
//                                String message = new String(messageAndMetadata.message(), "UTF-8");
//                                logger.info("message={}, key={}", message, key);
//                            } else {
//                                logger.info("key={}", key);
//                            }
//                            Thread.sleep(1000);
//                            connector.commitOffsets(true);
//                        } catch (Exception e) {
//                            logger.error("", e);
//                        }
//                    }
//                }
//            }, null);
//        }
        Thread.sleep(10000);
    }
}
