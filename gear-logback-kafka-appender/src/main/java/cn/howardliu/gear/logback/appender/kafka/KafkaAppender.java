package cn.howardliu.gear.logback.appender.kafka;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.AppenderAttachableImpl;
import cn.howardliu.gear.kafka.KafkaProducerWrapper;
import cn.howardliu.gear.logback.appender.kafka.delivery.DeliveryStrategy;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * <br>created at 16-12-22
 *
 * @author liuxh
 * @since 1.0.1
 */
public class KafkaAppender<E> extends KafkaAppenderConfig<E> {
    private static final String KAFKA_LOGGER_PREFIX = "org.apache.kafka.clients";
    private KafkaProducerWrapper<String, String> wrapper = null;
    private final AppenderAttachableImpl<E> aai = new AppenderAttachableImpl<>();
    private final ConcurrentLinkedQueue<E> queue = new ConcurrentLinkedQueue<>();
    private final DeliveryStrategy.Callback<E> callback = new DeliveryStrategy.Callback<E>() {
        @Override
        public void execute(E event, Throwable throwable) {
            aai.appendLoopOnAppenders(event);
        }
    };

    public KafkaAppender() {
        addKafkaProducerConfigValue(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        addKafkaProducerConfigValue(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    }

    @Override
    public void doAppend(E event) {
        ensureDeferredAppends();
        if (event instanceof ILoggingEvent && ((ILoggingEvent) event).getLoggerName().startsWith(KAFKA_LOGGER_PREFIX)) {
            // TODO kafka-client log polled other appender
            // deferAppend(event);
            aai.appendLoopOnAppenders(event);
        } else {
            super.doAppend(event);
        }
    }

    private void ensureDeferredAppends() {
        E event;
        while ((event = queue.poll()) != null) {
            super.doAppend(event);
        }
    }

    private void deferAppend(E event) {
        queue.add(event);
    }

    @Override
    public void start() {
        if (!checkPrerequisites()) {
            return;
        }
        wrapper = new KafkaProducerWrapper<>(kafkaProducerConfig);
        super.start();
    }

    @Override
    public void stop() {
        super.stop();
        if (wrapper != null) {
            try {
                wrapper.close();
            } catch (Exception e) {
                this.addWarn("Failed to shut down kafka producer: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void addAppender(Appender<E> newAppender) {
        aai.addAppender(newAppender);
    }

    @Override
    public Iterator<Appender<E>> iteratorForAppenders() {
        return aai.iteratorForAppenders();
    }

    @Override
    public Appender<E> getAppender(String name) {
        return aai.getAppender(name);
    }

    @Override
    public boolean isAttached(Appender<E> appender) {
        return aai.isAttached(appender);
    }

    @Override
    public void detachAndStopAllAppenders() {
        aai.detachAndStopAllAppenders();
    }

    @Override
    public boolean detachAppender(Appender<E> appender) {
        return aai.detachAppender(appender);
    }

    @Override
    public boolean detachAppender(String name) {
        return aai.detachAppender(name);
    }

    @Override
    protected void append(E e) {
        deliveryStrategy.send(wrapper, topic, keyingStrategy.key(e), encoder.encode(e), e, callback);
    }
}
