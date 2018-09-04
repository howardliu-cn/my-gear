package cn.howardliu.gear.kafka;

import org.apache.commons.lang3.Validate;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * <br>created at 16-12-20
 *
 * @author liuxh
 * @since 1.0.1
 */
public class KafkaProducerFactory<K, V> {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerFactory.class);
    private Properties config = new Properties();

    {
        config.put(BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        config.put(ACKS_CONFIG, "all");// 0, 1, all
        config.put(BUFFER_MEMORY_CONFIG, "33554432");
        config.put(COMPRESSION_TYPE_CONFIG, "none");// none, gzip, snappy
        config.put(RETRIES_CONFIG, "0");
        config.put(BATCH_SIZE_CONFIG, "16384");
        config.put(CLIENT_ID_CONFIG, "default-client-id");
        config.put(LINGER_MS_CONFIG, "0");
        config.put(MAX_REQUEST_SIZE_CONFIG, "1048576");
        config.put(RECEIVE_BUFFER_CONFIG, "32768");
        config.put(SEND_BUFFER_CONFIG, "131072");
        config.put(TRANSACTION_TIMEOUT_CONFIG, "30000");
        config.put(KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        config.put(VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
    }

    public KafkaProducerFactory(Properties config) {
        this.config.putAll(Validate.notNull(config, "the configuration object mustn't null"));
        logger.debug("the configuration of consumer is {}", this.config);
    }

    public KafkaProducerFactory(String bootstrapServers) {
        config.setProperty(BOOTSTRAP_SERVERS_CONFIG,
                Validate.notEmpty(bootstrapServers, "the bootstrap servers mustn't null"));
    }

    public KafkaProducer<K, V> build() {
        return new KafkaProducer<>(this.config);
    }
}
