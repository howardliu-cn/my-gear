package cn.howardliu.gear.kafka;

import org.apache.commons.lang3.Validate;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * <br>created at 16-8-12
 *
 * @author liuxh
 * @since 1.0.0
 */
public class KafkaConsumerFactory {
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerFactory.class);
    private Properties config;

    public KafkaConsumerFactory(Properties config) {
        this.config = Validate.notNull(config, "the configuration object mustn't null");
        logger.debug("the configuration of consumer is {}", this.config);
    }

    public KafkaConsumer<String, String> build() {
        return new KafkaConsumer<>(config);
    }
}
