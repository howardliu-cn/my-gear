package cn.howardliu.gear.httpclient.simple;

import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * <br>created at 15-10-13
 *
 * @author liuxh
 * @since 1.0.0
 */
public class PoolingHttpRequester extends HttpRequester {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final Object lock = new Object();
    private static PoolingHttpRequester poolingHttpRequester;
    private static final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
    private static CloseableHttpClient client = null;

    static {
        cm.setMaxTotal(256);
        cm.setDefaultMaxPerRoute(16);
        client = HttpClients.custom().setConnectionManager(cm).build();
    }

    private PoolingHttpRequester() {
    }

    public static PoolingHttpRequester getHttpRequester() {
        if (poolingHttpRequester == null) {
            synchronized (lock) {
                if (poolingHttpRequester == null) {
                    poolingHttpRequester = new PoolingHttpRequester();
                }
            }
        }
        return poolingHttpRequester;
    }

    @Override
    CloseableHttpClient getHttpClient() {
        return client;
    }

    public PoolingHttpRequester setConnPoolMaxTotal(final int max) {
        cm.setMaxTotal(max);
        return this;
    }

    public PoolingHttpRequester setConnPoolMaxPerRoute(final int max) {
        cm.setDefaultMaxPerRoute(max);
        return this;
    }

    public PoolingHttpRequester setConnPooMaxPerRoute(final HttpRoute route, final int max) {
        cm.setMaxPerRoute(route, max);
        return this;
    }

    /**
     * GET方式请求url
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return url响应结果，java.lang.String类型。
     * @throws URISyntaxException 输入的url不合法
     * @throws IOException
     */
    public String get(String url, List<NameValuePair> params) throws URISyntaxException, IOException {
        // 1. 验证输入url的有效性：url没有有效的host或url为相对路径，则url无效。
        URI uri = (new URIBuilder(url)).build();
        HttpHost httpHost = URIUtils.extractHost(uri);
        if (httpHost == null) {
            throw new IllegalArgumentException("缺少有效的HOST");
        }
        String respText;
        // 2. 创建HttpClient对象
        CloseableHttpClient client = getHttpClient();
        // 3. 创建请求方法的实例，并指定请求URL。如果需要发送GET请求，创建HttpGet对象；如果需要发送POST请求，创建HttpPost对象。
        HttpGet httpGet = new HttpGet(uri);
        if (logger.isDebugEnabled()) {
            logger.debug("executing request: ", httpGet.getRequestLine());
        }
        // 4. 如果需要发送请求参数，可调用HttpGet、HttpPost共同的setParams(HttpParams params)方法来添加请求参数；
        //    对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
        if (params != null && params.size() > 0) {
            httpGet.setURI(new URI(httpGet.getURI().toString()
                    + "?" + EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8))));
        }
        // 5. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
        respText = execute(client, httpHost, httpGet);
        return respText;
    }
}
