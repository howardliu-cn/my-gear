package cn.howardliu.gear.es.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import cn.howardliu.gear.es.EsConfig;
import cn.howardliu.gear.es.storm.EsTupleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Basic bolt for storing tuple to ES document.
 * <br/>create at 16-3-8
 *
 * @author liuxh
 * @since 1.0.0
 */
public class EsIndexBolt extends AbstractEsBolt {
    private static final Logger logger = LoggerFactory.getLogger(EsIndexBolt.class);
    private final EsTupleMapper tupleMapper;

    /**
     * EsIndexBolt constructor
     *
     * @param esConfig    Elasticsearch configuration containing node addresses and cluster name {@link cn.howardliu.gear.es.EsConfig}
     * @param tupleMapper Tuple to ES document mapper {@link EsTupleMapper}
     */
    public EsIndexBolt(EsConfig esConfig, EsTupleMapper tupleMapper) {
        super(esConfig);
        this.tupleMapper = checkNotNull(tupleMapper);
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        super.prepare(map, topologyContext, outputCollector);
    }

    /**
     * {@inheritDoc}
     * Tuple should have relevant fields (source, index, type, id) for tupleMapper to extract ES document.
     */
    @Override
    public void execute(Tuple tuple) {
        try {
            String source = tupleMapper.getSource(tuple);
            String index = tupleMapper.getIndex(tuple);
            String type = tupleMapper.getType(tuple);
            String id = tupleMapper.getId(tuple);

            client.prepareIndex(index, type, id).setSource(source).execute().actionGet();
            collector.ack(tuple);
        } catch (Exception e) {
            logger.error("将数据写入es失败", e);
            collector.reportError(e);
            collector.fail(tuple);
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }
}
