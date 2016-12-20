package cn.howardliu.gear.toolkit.es;

import cn.howardliu.gear.es.ElasticSearchClientFactory;
import cn.howardliu.gear.es.EsConfig;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.sort.SortParseElement;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * <br>created at 16-11-30
 *
 * @author liuxh
 * @since 1.0.1
 */
public class EsDump {
    public static void main(String[] args) throws Exception {
        Integer size = Integer.valueOf(args[0]);
        int duration = Integer.valueOf(args[1]);
        String sourceEsNodes = "10.6.3.24:9300,10.6.3.25:9300,10.6.3.26:9300,10.6.3.27:9300,10.6.3.28:9300";
        String sourceEsClusterName = "wfj-es";
        String destEsNodes = "10.6.100.29:9300,10.6.100.30:9300,10.6.100.31:9300,10.6.100.32:9300,10.6.100.33:9300";
        String destEsClusterName = "wfj-es";
        Map<String, List<String>> source = new HashMap<>();
        source.put("message", Arrays.asList("tx-info", "message-log", "tx-info-log"));
        Client sourceEsClient = new ElasticSearchClientFactory(new EsConfig(sourceEsClusterName, sourceEsNodes))
                .construct();
        Client destEsClient = new ElasticSearchClientFactory(new EsConfig(destEsClusterName, destEsNodes)).construct();

        for (Map.Entry<String, List<String>> entry : source.entrySet()) {
            String indexName = entry.getKey();
            List<String> typeNames = entry.getValue();
            for (String typeName : typeNames) {
                dump(sourceEsClient, indexName, typeName, destEsClient, indexName, typeName, size, duration);
            }
        }
    }

    private static void dump(Client sourceEsClient, String sourceIndexName, String sourceTypeName,
            Client destEsClient, String destIndexName, String destTypeName, int size, int duration) throws Exception {
        long total = sourceEsClient
                .prepareSearch(sourceIndexName)
                .setTypes(sourceTypeName)
                .setSize(0)
                .get()
                .getHits()
                .getTotalHits();
        SearchResponse scrollResp = sourceEsClient
                .prepareSearch(sourceIndexName)
                .setTypes(sourceTypeName)
                .addSort(SortParseElement.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(duration, TimeUnit.MINUTES))
                .setSize(size)
                .get();
        long count = 0;
        while (true) {
            SearchHit[] hits = scrollResp.getHits().getHits();
            if (hits.length == 0) {
                break;
            }
            BulkRequestBuilder destBulkRequestBuilder = destEsClient.prepareBulk();
            for (SearchHit hit : hits) {
                destBulkRequestBuilder.add(
                        destEsClient
                                .prepareIndex()
                                .setIndex(destIndexName)
                                .setType(destTypeName)
                                .setId(hit.getId())
                                .setSource(hit.getSource())
                );
            }
            BulkResponse bulkResponse = destBulkRequestBuilder.setRefresh(true).get();
            if (bulkResponse.hasFailures()) {
                System.out.println("批量迁移数据出现错误： {}" + bulkResponse.buildFailureMessage());
            } else {
                count += hits.length;
            }
            System.out.println("总数：" + total + "; 已迁移：" + count);
            scrollResp = sourceEsClient
                    .prepareSearchScroll(scrollResp.getScrollId())
                    .setScroll(new TimeValue(duration, TimeUnit.MINUTES))
                    .get();
        }
    }
}
