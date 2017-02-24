package cn.howardliu.gear.storm.spout.scheme;

import backtype.storm.spout.Scheme;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * <br/>create at 16-7-12
 *
 * @author liuxh
 * @since 1.0.2
 */
public class MessageScheme implements Scheme {
    private static final Logger logger = LoggerFactory.getLogger(MessageScheme.class);
    private static long count = 1;
    private static final long step = 10;

    @Override
    public List<Object> deserialize(byte[] ser) {
        try {
            String msg = new String(ser, "UTF-8");
            logger.trace("get one message is {}", msg);
            if (logger.isInfoEnabled()) {
                if (count % step == 0) {
                    logger.info("{}", count);
                }
                count++;
            }
            return new Values(msg);
        } catch (UnsupportedEncodingException ignored) {
            return null;
        }
    }

    @Override
    public Fields getOutputFields() {
        return new Fields("msg");
    }
}
