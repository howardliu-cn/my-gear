package cn.howardliu.gear.es.storm.bolt;

import cn.howardliu.gear.es.EsConfig;
import cn.howardliu.gear.es.storm.EsSource;
import org.apache.storm.topology.FailedException;
import org.apache.storm.tuple.Tuple;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <br>created at 16-4-7
 *
 * @author liuxh
 * @since 1.0.0
 */
public class EsBulkIndexBolt extends AbstractEsBolt {
    private static final Logger logger = LoggerFactory.getLogger(EsBulkIndexBolt.class);

    public EsBulkIndexBolt(EsConfig esConfig) {
        super(esConfig);
    }

    @Override
    public void execute(Tuple tuple) {
        try {
            //noinspection unchecked
            List<EsSource> sources = (List<EsSource>) tuple.getValueByField("sources");

            BulkRequestBuilder bulkRequest = client.prepareBulk();
            for (EsSource source : sources) {
                bulkRequest.add(
                        client
                                .prepareIndex(source.getIndexName(), source.getTypeName(), source.getId())
                                .setSource(source.getSource())
                );
            }
            BulkResponse bulkResponse = bulkRequest.execute().actionGet();
            if (bulkResponse.hasFailures()) {
                String msg = "failed processing bulk index requests " + bulkResponse.buildFailureMessage();
                logger.warn(msg);
                collector.reportError(new FailedException(msg));
            }

            collector.ack(tuple);
        } catch (Exception e) {
            collector.reportError(e);
            collector.fail(tuple);
        }
    }
}
