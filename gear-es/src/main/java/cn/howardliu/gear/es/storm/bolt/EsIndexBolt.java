package cn.howardliu.gear.es.storm.bolt;

import cn.howardliu.gear.es.EsConfig;
import cn.howardliu.gear.es.storm.EsTupleMapper;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.tuple.Tuple;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Basic bolt for storing tuple to ES document.
 * <br>created at 16-3-8
 *
 * @author liuxh
 * @since 1.0.0
 */
public class EsIndexBolt extends AbstractEsBolt {
    private static final Logger logger = LoggerFactory.getLogger(EsIndexBolt.class);
    private final EsTupleMapper tupleMapper;
    private final boolean replace;

    /**
     * EsIndexBolt constructor
     *
     * @param esConfig    Elasticsearch configuration containing node addresses and cluster name {@link cn.howardliu.gear.es.EsConfig}
     * @param tupleMapper Tuple to ES document mapper {@link EsTupleMapper}
     */
    public EsIndexBolt(EsConfig esConfig, EsTupleMapper tupleMapper) {
        this(esConfig, tupleMapper, true);
    }

    /**
     * EsIndexBolt constructor
     *
     * @param esConfig    Elasticsearch configuration containing node addresses and cluster name {@link cn.howardliu.gear.es.EsConfig}
     * @param tupleMapper Tuple to ES document mapper {@link EsTupleMapper}
     * @param replace     if true, replace ES document if id exists
     */
    public EsIndexBolt(EsConfig esConfig, EsTupleMapper tupleMapper, boolean replace) {
        super(esConfig);
        this.tupleMapper = tupleMapper;
        this.replace = replace;
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

            if (replace || !exists(index, type, id)) {
                client.prepareIndex(index, type, id).setSource(source).execute().actionGet();
            }
            collector.ack(tuple);
        } catch (Exception e) {
            logger.error("将数据写入es失败", e);
            collector.reportError(e);
            collector.fail(tuple);
        }
    }

    private boolean exists(String index, String type, String id) {
        try {
            return client.prepareSearch(index)
                    .setTypes(type)
                    .setQuery(QueryBuilders.termsQuery("_id", id))
                    .setSize(0)
                    .get()
                    .getHits().getTotalHits() > 0;
        } catch (Exception e) {
            logger.error("elasticsearch query exception, index={}, type={}, _id={}", index, type, id, e);
        }
        return false;
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }
}
