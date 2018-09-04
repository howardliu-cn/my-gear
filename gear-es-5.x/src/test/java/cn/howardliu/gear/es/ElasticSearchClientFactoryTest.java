package cn.howardliu.gear.es;

import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * <br>created at 18-8-29
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ElasticSearchClientFactoryTest {

    @Test
    public void construct() throws IOException {
        ElasticSearchClientFactory factory = new ElasticSearchClientFactory(new EsConfig("fusion-docker-es", "192.168.7.137:9092,192.168.7.138:9092,192.168.7.139:9092"));
        RestClient client = factory.construct();
        Assert.assertTrue(new RestHighLevelClient(client).ping());
        client.close();
    }
}