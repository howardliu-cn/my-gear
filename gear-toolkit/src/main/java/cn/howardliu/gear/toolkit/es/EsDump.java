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

import java.util.concurrent.TimeUnit;

/**
 * <br>created at 16-11-30
 *
 * @author liuxh
 * @since 1.0.1
 */
public class EsDump {
    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 8) {
            throw new RuntimeException(
                    "输入参数错误，请检查！\n" +
                            "0. 源集群名称\n" +
                            "1. 源集群地址\n" +
                            "2. 源索引名称\n" +
                            "3. 源类型名称\n" +
                            "4. 目标集群名称\n" +
                            "5. 目标集群地址\n" +
                            "6. 目标索引名称\n" +
                            "7. 目标类型名称\n" +
                            "8. 分页数量（可选，默认1000）\n" +
                            "9. 滚动读取有效时间（可选，默认3，单位：分钟）\n"
            );
        }
        Integer size = 1000;
        Integer duration = 3;
        if (args.length >= 8) {
            size = Integer.valueOf(args[8]);
        }
        if (args.length >= 9) {
            duration = Integer.valueOf(args[9]);
        }
        dump(
                new ElasticSearchClientFactory(new EsConfig(args[0], args[1])).construct(),
                args[2],
                args[3],
                new ElasticSearchClientFactory(new EsConfig(args[4], args[5])).construct(),
                args[6],
                args[7],
                size,
                duration
        );
    }

    private static void dump(Client sourceEsClient, String sourceIndexName, String sourceTypeName,
            Client destEsClient, String destIndexName, String destTypeName, int size, int duration) throws Exception {
        long time = -System.currentTimeMillis();
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
            long usedSeconds = (System.currentTimeMillis() + time) / 1000;
            System.out.println("总数：" + total + "; 已迁移：" + count + "; 已使用：" + usedSeconds);
            scrollResp = sourceEsClient
                    .prepareSearchScroll(scrollResp.getScrollId())
                    .setScroll(new TimeValue(duration, TimeUnit.MINUTES))
                    .get();
        }
    }
}
