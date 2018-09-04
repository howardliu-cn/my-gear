package cn.howardliu.gear.es;

import org.elasticsearch.client.RestClient;

import java.io.Serializable;

/**
 * <br>created at 16-5-17
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ElasticSearchClientFactory implements Serializable {
    private final EsConfig esConfig;

    public ElasticSearchClientFactory(EsConfig esConfig) {
        this.esConfig = esConfig;
    }

    public RestClient construct() {
        return RestClient.builder(this.esConfig.getHttpHosts().httpHostArray()).build();
    }
}
