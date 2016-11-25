package cn.howardliu.gear.es.storm;

import backtype.storm.tuple.ITuple;

/**
 * <br/>create at 16-3-8
 *
 * @author liuxh
 * @since 1.0.0
 */
public class DefaultEsTupleMapper implements EsTupleMapper {
    @Override
    public String getSource(ITuple tuple) {
        return tuple.getStringByField("source");
    }

    @Override
    public String getIndex(ITuple tuple) {
        return tuple.getStringByField("index");
    }

    @Override
    public String getType(ITuple tuple) {
        return tuple.getStringByField("type");
    }

    @Override
    public String getId(ITuple tuple) {
        return tuple.getStringByField("id");
    }
}
