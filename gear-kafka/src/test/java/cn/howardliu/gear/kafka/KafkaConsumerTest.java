package cn.howardliu.gear.kafka;

import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * <br/>created at 16-8-12
 *
 * @author liuxh
 * @since 1.0.0
 */
public class KafkaConsumerTest {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerTest.class);
    private Properties properties;

    @Before
    public void before() throws Exception {
        properties = new Properties();
        properties.put("zookeeper.connect", "10.6.2.56:2181,10.6.2.57:2181,10.6.2.58:2181/kafka");
        properties.put("fetch.message.max.bytes", "1048576");
        properties.put("group.id", "jobKey");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "largest");// smallest, largest
    }

    @Test
    public void test1() throws Exception {
        consumer();
    }

    @Test
    public void test2() throws Exception {
        consumer();
    }

    public void consumer() throws Exception {
        final ConsumerConnector connector = new KafkaConsumerFactory(properties).build();
        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put("mq-job-topic-dev", 2);
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = connector.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> kafkaStreams = messageStreams.get("mq-job-topic-dev");
        ExecutorService executorService = Executors.newFixedThreadPool(2, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setDaemon(true);
                return t;
            }
        });
        for (final KafkaStream<byte[], byte[]> kafkaStream : kafkaStreams) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (MessageAndMetadata<byte[], byte[]> messageAndMetadata : kafkaStream) {
                        try {
                            String key = new String(messageAndMetadata.key(), "UTF-8");
                            if (key.startsWith("job-key")) {
                                String message = new String(messageAndMetadata.message(), "UTF-8");
                                logger.info("message={}, key={}", message, key);
                            } else {
                                logger.info("key={}", key);
                            }
                            Thread.sleep(1000);
                            connector.commitOffsets(true);
                        } catch (Exception e) {
                            logger.error("", e);
                        }
                    }
                }
            }, null);
        }
        Thread.sleep(10000);
    }
}
