package cn.howardliu.gear.es;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;

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

    public Client construct() {
        Settings settings = esConfig.getSettings();
        TransportClient transportClient = TransportClient.builder().settings(settings).build();
        addTransportAddresses(transportClient);
        return transportClient;
    }

    private void addTransportAddresses(TransportClient transportClient) {
        Iterable<InetSocketTransportAddress> transportAddresses = esConfig.getTransportAddresses();
        for (InetSocketTransportAddress transportAddress : transportAddresses) {
            transportClient.addTransportAddress(transportAddress);
        }
    }
}
