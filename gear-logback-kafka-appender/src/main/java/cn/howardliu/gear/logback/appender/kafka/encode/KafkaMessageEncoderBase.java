package cn.howardliu.gear.logback.appender.kafka.encode;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

/**
 * <br>created at 16-12-21
 *
 * @author liuxh
 * @since 1.0.1
 */
public abstract class KafkaMessageEncoderBase<E> extends ContextAwareBase implements KafkaMessageEncoder<E>, LifeCycle {
    private boolean started = false;

    @Override
    public void start() {
        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }
}
