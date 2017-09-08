package cn.howardliu.gear.es.storm.bolt;

import cn.howardliu.gear.es.ElasticSearchClientFactory;
import cn.howardliu.gear.es.EsConfig;
import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichBolt;
import org.apache.storm.tuple.Tuple;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * <br>created at 16-3-8
 *
 * @author liuxh
 * @since 1.0.0
 */
public abstract class AbstractEsBolt extends BaseRichBolt {
    private static final Logger logger = LoggerFactory.getLogger(AbstractEsBolt.class);

    protected transient static Client client;

    protected OutputCollector collector;
    protected EsConfig esConfig;

    public AbstractEsBolt(EsConfig esConfig) {
        checkNotNull(esConfig);
        this.esConfig = esConfig;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        try {
            this.collector = outputCollector;
            synchronized (AbstractEsBolt.class) {
                if (client == null) {
                    client = new ElasticSearchClientFactory(esConfig).construct();
                }
            }
        } catch (Exception e) {
            logger.warn("unable to initialize EsBolt ", e);
        }
    }

    @Override
    public abstract void execute(Tuple tuple);

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
    }

    static Client getClient() {
        return AbstractEsBolt.client;
    }

    static void replaceClient(Client client) {
        AbstractEsBolt.client = client;
    }
}
