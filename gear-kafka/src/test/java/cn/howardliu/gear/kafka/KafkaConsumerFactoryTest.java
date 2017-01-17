package cn.howardliu.gear.kafka;

import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.message.MessageAndMetadata;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * <br>created at 17-1-17
 *
 * @author liuxh
 * @since 1.0.1
 */
public class KafkaConsumerFactoryTest {
    private KafkaConsumerFactory factory;
    private KafkaProducerWrapper<String, String> wrapper;

    @Before
    public void createKafkaConsumerFactory() {
        Properties properties = new Properties();
        properties.put("zookeeper.connect",
                "10.6.100.18:2181,10.6.100.19:2181,10.6.100.20:2181,10.6.100.21:2181,10.6.100.22:2181/kafka");
        properties.put("fetch.message.max.bytes", "1048576");
        properties.put("group.id", "kafka-consumer");
        properties.put("auto.commit.enable", "false");
        properties.put("auto.offset.reset", "largest");
        factory = new KafkaConsumerFactory(properties);
    }

    @Before
    public void createKafkaProducerWrapper() throws Exception {
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
        wrapper = new KafkaProducerWrapper<>(properties);
    }

    @Test
    public void test() throws Exception {
        final ConsumerConnector consumer = factory.build();

        ExecutorService executorService = Executors.newFixedThreadPool(4, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r);
                t.setName("sale-info-kafka-consumer-" + t.getId());
                t.setDaemon(true);
                return t;
            }
        });

        Map<String, Integer> topicCountMap = new HashMap<>();
        topicCountMap.put("sale-info", 4);
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
        List<KafkaStream<byte[], byte[]>> kafkaStreams = messageStreams.get("sale-info");
        for (final KafkaStream<byte[], byte[]> kafkaStream : kafkaStreams) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    for (MessageAndMetadata<byte[], byte[]> messageAndMetadata : kafkaStream) {
                        try {
                            String key = new String(messageAndMetadata.key(), "UTF-8");
                            String value = new String(messageAndMetadata.message(), "UTF-8");
                            System.out.println("key=" + key + ", value=" + value);
                            wrapper.send("sale-info", key, value);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            consumer.commitOffsets(true);
                        }
                    }
                }
            }, null);
        }

        TimeUnit.MINUTES.sleep(1);
        consumer.shutdown();
    }
}