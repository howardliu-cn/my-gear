package cn.howardliu.gear.es;

import org.apache.commons.lang3.Validate;
import org.elasticsearch.common.settings.Settings;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

/**
 * <br/>create at 16-5-17
 *
 * @author liuxh
 * @since 1.0.0
 */
public class EsConfig implements Serializable {
    private final String clusterName;
    private final String[] nodes;
    private final Map<String, String> additionalConfiguration;

    /**
     * EsConfig Constructor to be used in EsIndexBolt, EsPercolateBolt and EsStateFactory
     *
     * @param clusterName Elasticsearch cluster name
     * @param nodeList    Elasticsearch address list in host:port pattern connected with comma
     * @throws IllegalArgumentException if nodes are empty
     * @throws NullPointerException     on any of the fields being null
     */
    public EsConfig(String clusterName, String nodeList) {
        this(clusterName, nodeList.split(TransportAddresses.ADDRESS_DELIMETER));
    }

    /**
     * EsConfig Constructor to be used in EsIndexBolt, EsPercolateBolt and EsStateFactory
     *
     * @param clusterName Elasticsearch cluster name
     * @param nodes       Elasticsearch addresses in host:port pattern string array
     * @throws IllegalArgumentException if nodes are empty
     * @throws NullPointerException     on any of the fields being null
     */
    public EsConfig(String clusterName, String[] nodes) {
        this(clusterName, nodes, Collections.<String, String>emptyMap());
    }

    /**
     * EsConfig Constructor to be used in EsIndexBolt, EsPercolateBolt and EsStateFactory
     *
     * @param clusterName             Elasticsearch cluster name
     * @param nodes                   Elasticsearch addresses in host:port pattern string array
     * @param additionalConfiguration Additional Elasticsearch configuration
     * @throws IllegalArgumentException if nodes are empty
     * @throws NullPointerException     on any of the fields being null
     */
    public EsConfig(String clusterName, String[] nodes, Map<String, String> additionalConfiguration) {
        this.clusterName = Validate.notNull(clusterName, "Elasticsearch cluster name cannot be null!");
        this.nodes = Validate.notEmpty(nodes, "Nodes cannot be empty");
        this.additionalConfiguration = Validate
                .notNull(additionalConfiguration, "Additional Elasticsearch configuration cannot be null!");
    }

    public TransportAddresses getTransportAddresses() {
        return new TransportAddresses(nodes);
    }

    public Settings getSettings() {
        return Settings.settingsBuilder()
                .put("cluster.name", clusterName)
                .put(additionalConfiguration)
                .build();
    }
}
