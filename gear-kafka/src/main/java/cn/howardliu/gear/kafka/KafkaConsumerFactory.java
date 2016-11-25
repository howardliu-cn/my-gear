package cn.howardliu.gear.kafka;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * <br/>created at 16-8-12
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

    public ConsumerConnector build() {
        return Consumer.createJavaConsumerConnector(new ConsumerConfig(config));
    }
}
