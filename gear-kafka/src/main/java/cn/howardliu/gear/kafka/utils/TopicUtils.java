package cn.howardliu.gear.kafka.utils;

import kafka.common.Topic;

/**
 * <br>created at 16-7-12
 *
 * @author liuxh
 * @since 1.0.2
 */
public class TopicUtils {
    public static String validate(String topic) {
        Topic.validate(topic);
        return topic;
    }
}
