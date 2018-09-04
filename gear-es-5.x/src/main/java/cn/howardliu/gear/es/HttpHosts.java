package cn.howardliu.gear.es;

import org.apache.commons.lang3.Validate;
import org.apache.http.HttpHost;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <br>created at 18-8-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public class HttpHosts implements Iterable<HttpHost> {
    private static final String ADDRESS_DELIMETER = ",";
    private final List<HttpHost> result;

    public HttpHosts(String nodes) {
        this(Validate.notBlank(nodes, "Elasticsearch hosts cannot be null").trim().split(ADDRESS_DELIMETER));
    }

    public HttpHosts(String[] nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("Elasticsearch hosts cannot be null");
        }
        if (nodes.length == 0) {
            throw new IllegalArgumentException("At least one Elasticsearch host must be specified");
        }
        result = new LinkedList<>();
        for (String node : nodes) {
            HttpHost host = HttpHost.create(node);
            result.add(host);
        }
    }

    @Override
    public Iterator<HttpHost> iterator() {
        return result.iterator();
    }

    public List<HttpHost> httpHosts() {
        return result;
    }

    public HttpHost[] httpHostArray() {
        HttpHost[] hosts = new HttpHost[result.size()];
        for (int i = 0; i < result.size(); i++) {
            hosts[i] = result.get(i);
        }
        return hosts;
    }
}
