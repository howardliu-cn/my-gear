package cn.howardliu.gear.logback.appender.kafka;

import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.spi.AppenderAttachable;
import cn.howardliu.gear.logback.appender.kafka.delivery.AsynchronousDeliveryStrategy;
import cn.howardliu.gear.logback.appender.kafka.delivery.DeliveryStrategy;
import cn.howardliu.gear.logback.appender.kafka.encode.KafkaMessageEncoder;
import cn.howardliu.gear.logback.appender.kafka.key.KeyingStrategy;
import cn.howardliu.gear.logback.appender.kafka.key.RoundRobinKeyingStrategy;

import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

/**
 * <br>created at 16-12-21
 *
 * @author liuxh
 * @since 1.0.1
 */
public abstract class KafkaAppenderConfig<E> extends UnsynchronizedAppenderBase<E> implements AppenderAttachable<E> {
    protected String topic = null;
    protected KafkaMessageEncoder<E> encoder = null;
    protected KeyingStrategy<String, ? super E> keyingStrategy = null;
    protected DeliveryStrategy<String, String, E> deliveryStrategy;
    protected Properties kafkaProducerConfig = new Properties();
    public static final Set<String> KNOWN_PRODUCER_CONFIG_KEYS = new HashSet<>();

    static {
        KNOWN_PRODUCER_CONFIG_KEYS.add(BOOTSTRAP_SERVERS_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(METADATA_FETCH_TIMEOUT_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(METADATA_MAX_AGE_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(BATCH_SIZE_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(BUFFER_MEMORY_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(ACKS_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(TIMEOUT_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(LINGER_MS_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(CLIENT_ID_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(SEND_BUFFER_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(RECEIVE_BUFFER_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(MAX_REQUEST_SIZE_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(RECONNECT_BACKOFF_MS_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(BLOCK_ON_BUFFER_FULL_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(RETRIES_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(RETRY_BACKOFF_MS_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(COMPRESSION_TYPE_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(METRICS_SAMPLE_WINDOW_MS_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(METRICS_NUM_SAMPLES_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(METRIC_REPORTER_CLASSES_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION);
        KNOWN_PRODUCER_CONFIG_KEYS.add(KEY_SERIALIZER_CLASS_CONFIG);
        KNOWN_PRODUCER_CONFIG_KEYS.add(VALUE_SERIALIZER_CLASS_CONFIG);
    }

    public void addKafkaProducerConfig(String kv) {
        String[] split = kv.split("=", 2);
        if (split.length == 2) {
            addKafkaProducerConfigValue(split[0], split[1]);
        }
    }

    public void addKafkaProducerConfigValue(String key, Object value) {
        if (!KNOWN_PRODUCER_CONFIG_KEYS.contains(key)) {
            addWarn("The key \"" + key + "\" is not a known kafka producer config key.");
        }
        this.kafkaProducerConfig.put(key, value);
    }


    protected boolean checkPrerequisites() {
        boolean errorFree = true;
        if (kafkaProducerConfig.get(BOOTSTRAP_SERVERS_CONFIG) == null) {
            addError("No \"" + BOOTSTRAP_SERVERS_CONFIG + "\" set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }
        if (topic == null) {
            addError("No topic set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }
        if (encoder == null) {
            addError("No encoder set for the appender named [\"" + name + "\"].");
            errorFree = false;
        }
        if (keyingStrategy == null) {
            addInfo("No partitionStrategy set for the appender named [\"" + name + "\"]. Using default RoundRobin strategy.");
            keyingStrategy = new RoundRobinKeyingStrategy<>();
        }
        if (deliveryStrategy == null) {
            addInfo("No sendStrategy set for the appender named [\"" + name + "\"]. Using default asynchronous strategy.");
            deliveryStrategy = new AsynchronousDeliveryStrategy<>();
        }
        return errorFree;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public KafkaMessageEncoder<E> getEncoder() {
        return encoder;
    }

    public void setEncoder(KafkaMessageEncoder<E> encoder) {
        this.encoder = encoder;
    }

    public KeyingStrategy<String, ? super E> getKeyingStrategy() {
        return keyingStrategy;
    }

    public void setKeyingStrategy(KeyingStrategy<String, ? super E> keyingStrategy) {
        this.keyingStrategy = keyingStrategy;
    }

    public DeliveryStrategy<String, String, E> getDeliveryStrategy() {
        return deliveryStrategy;
    }

    public void setDeliveryStrategy(DeliveryStrategy<String, String, E> deliveryStrategy) {
        this.deliveryStrategy = deliveryStrategy;
    }

    public Properties getKafkaProducerConfig() {
        return kafkaProducerConfig;
    }

    public void setKafkaProducerConfig(Properties kafkaProducerConfig) {
        this.kafkaProducerConfig = kafkaProducerConfig;
    }
}
