package cn.howardliu.gear.commons.httpclient.simple;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * <br>created at 16-8-4
 *
 * @author liuxh
 * @since 1.0.0
 */
public class ClientAuthentication {
    private static final Logger logger = LoggerFactory.getLogger(ClientAuthentication.class);

    @Test
    public void test() throws Exception{
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(new AuthScope("10.6.4.74", 15672),
                new UsernamePasswordCredentials("admin", "12345"));
        try (CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credentialsProvider).build()) {
            HttpGet httpget = new HttpGet("http://10.6.4.74:15672/api/overview/");
            System.out.println("Executing request " + httpget.getRequestLine());
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                System.out.println(EntityUtils.toString(response.getEntity()));
            }
        }
    }

    @Test
    public void test1() throws Exception{
        URI uri = (new URIBuilder("http://10.6.4.74:15672/api/overview/")).build();
        HttpHost httpHost = URIUtils.extractHost(uri);
        Assert.assertEquals("10.6.4.74", httpHost.getHostName());
        Assert.assertEquals(15672, httpHost.getPort());
    }
}
