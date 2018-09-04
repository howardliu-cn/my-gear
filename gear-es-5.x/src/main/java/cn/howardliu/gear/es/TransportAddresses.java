package cn.howardliu.gear.es;

import org.apache.commons.lang3.Validate;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * <br>created at 16-5-17
 *
 * @author liuxh
 * @since 1.0.0
 */
public class TransportAddresses implements Iterable<InetSocketTransportAddress> {
    private static final Logger logger = LoggerFactory.getLogger(TransportAddresses.class);
    public static final String ADDRESS_DELIMETER = ",";
    public static final String HOST_PORT_DELIMETER = ":";
    private final String[] nodes;

    public TransportAddresses(String nodes) {
        this(Validate.notBlank(nodes, "Elasticsearch hosts cannot be null").trim().split(ADDRESS_DELIMETER));
    }

    public TransportAddresses(String[] nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("Elasticsearch hosts cannot be null");
        }
        if (nodes.length == 0) {
            throw new IllegalArgumentException("At least one Elasticsearch host must be specified");
        }
        this.nodes = nodes;
    }

    @Override
    public Iterator<InetSocketTransportAddress> iterator() {
        List<InetSocketTransportAddress> result = new LinkedList<>();
        for (String node : nodes) {
            try {
                InetSocketTransportAddress transportAddress = transformToInetAddress(node);
                result.add(transportAddress);
            } catch (UnknownHostException e) {
                logger.error("Incorrect Elasticsearch hostname {}", node, e);
            }
        }
        return result.iterator();
    }

    private InetSocketTransportAddress transformToInetAddress(String node) throws UnknownHostException {
        String[] hostAndPort = node.split(HOST_PORT_DELIMETER);
        if (hostAndPort.length != 2) {
            throw new IllegalArgumentException(
                    "Incorrect Elasticsearch node format, should follow {host}" + HOST_PORT_DELIMETER + "{port} pattern");
        }
        String hostname = hostname(hostAndPort[0]);
        return new InetSocketTransportAddress(InetAddress.getByName(hostname), port(hostAndPort[1]));
    }

    private String hostname(String input) {
        return input.trim();
    }

    private int port(String input) {
        return Integer.parseInt(input.trim());
    }
}
