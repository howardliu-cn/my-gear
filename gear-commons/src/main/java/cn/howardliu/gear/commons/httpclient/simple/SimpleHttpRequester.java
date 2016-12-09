package cn.howardliu.gear.commons.httpclient.simple;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <br>created at 16-4-8
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SimpleHttpRequester extends HttpRequester {
    private static final Logger logger = LoggerFactory.getLogger(SimpleHttpRequester.class);

    private SimpleHttpRequester() {
    }

    public static SimpleHttpRequester getHttpRequester() {
        return new SimpleHttpRequester();
    }

    @Override
    CloseableHttpClient getHttpClient() {
        return HttpClients.createDefault();
    }

    @Override
    CloseableHttpClient getHttpClient(HttpHost httpHost, String username, String password) {
        if (username == null || password == null) {
            return getHttpClient();
        }
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope(httpHost.getHostName(), httpHost.getPort()),
                new UsernamePasswordCredentials(username, password));
        return HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build();
    }
}
