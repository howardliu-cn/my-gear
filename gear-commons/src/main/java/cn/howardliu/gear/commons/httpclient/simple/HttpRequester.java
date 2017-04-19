package cn.howardliu.gear.commons.httpclient.simple;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.*;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <br>created at 16-4-8
 *
 * @author liuxh
 * @since 1.0.0
 */
public abstract class HttpRequester {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String DEFAULT_CONTENT_TYPE = ContentType.APPLICATION_JSON.getMimeType();

    abstract CloseableHttpClient getHttpClient();

    CloseableHttpClient getHttpClient(HttpHost httpHost, String username, String password) {
        return this.getHttpClient();
    }

    /**
     * GET方式请求url，url中已经包含请求参数或不需要参数。
     *
     * @param url 请求地址
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String get(String url) throws URISyntaxException, IOException {
        return get(url, RequestConfig.DEFAULT);
    }

    public String get(String url, int timeout) throws URISyntaxException, IOException {
        return get(url, getRequestConfig(timeout));
    }

    public String get(String url, RequestConfig config) throws URISyntaxException, IOException {
        return get(url, new ArrayList<NameValuePair>(), config);
    }

    /**
     * GET方式请求url
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String get(String url, Map<String, String> params) throws URISyntaxException, IOException {
        return get(url, params, RequestConfig.DEFAULT);
    }

    public String get(String url, Map<String, String> params, int timeout) throws URISyntaxException, IOException {
        return get(url, params, getRequestConfig(timeout));
    }

    private RequestConfig getRequestConfig(int timeout) {
        return RequestConfig.custom().setConnectTimeout(timeout).setSocketTimeout(timeout).build();
    }

    public String get(String url, Map<String, String> params, RequestConfig config)
            throws URISyntaxException, IOException {
        List<NameValuePair> nameValuePairs = new ArrayList<>(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return get(url, nameValuePairs, config);
    }

    /**
     * GET方式请求url
     *
     * @param url   请求地址
     * @param param 请求参数
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String get(String url, NameValuePair param) throws URISyntaxException, IOException {
        return get(url, param, RequestConfig.DEFAULT);
    }

    public String get(String url, NameValuePair param, int timeout) throws URISyntaxException, IOException {
        return get(url, param, getRequestConfig(timeout));
    }

    public String get(String url, NameValuePair param, RequestConfig config) throws URISyntaxException, IOException {
        return get(url, Arrays.asList(param), config);
    }

    /**
     * GET方式请求url
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String get(String url, List<NameValuePair> params) throws URISyntaxException, IOException {
        return get(url, params, RequestConfig.DEFAULT);
    }

    public String get(String url, List<NameValuePair> params, int timeout) throws URISyntaxException, IOException {
        return get(url, params, getRequestConfig(timeout));
    }

    public String get(String url, List<NameValuePair> params, RequestConfig config)
            throws URISyntaxException, IOException {
        // 1. 验证输入url的有效性：url没有有效的host或url为相对路径，则url无效。
        URI uri = (new URIBuilder(url)).build();
        HttpHost httpHost = URIUtils.extractHost(uri);
        if (httpHost == null) {
            throw new IllegalArgumentException("缺少有效的HOST");
        }
        String respText;
        // 2. 创建HttpClient对象
        try (CloseableHttpClient client = getHttpClient()) {
            respText = get(client, httpHost, uri, params, config);
        }
        return respText;
    }

    /**
     * GET方式请求url
     *
     * @param url      请求地址
     * @param params   请求参数
     * @param username 权限校验用户名
     * @param password 权限校验密码
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String get(String url, List<NameValuePair> params, String username, String password)
            throws URISyntaxException, IOException {
        return get(url, params, username, password, RequestConfig.DEFAULT);
    }

    public String get(String url, List<NameValuePair> params, String username, String password, int timeout)
            throws URISyntaxException, IOException {
        return get(url, params, username, password, getRequestConfig(timeout));
    }

    public String get(String url, List<NameValuePair> params, String username, String password,
            RequestConfig config) throws URISyntaxException, IOException {
        // 1. 验证输入url的有效性：url没有有效的host或url为相对路径，则url无效。
        URI uri = (new URIBuilder(url)).build();
        HttpHost httpHost = URIUtils.extractHost(uri);
        if (httpHost == null) {
            throw new IllegalArgumentException("缺少有效的HOST");
        }
        String respText;
        // 2. 创建HttpClient对象
        try (CloseableHttpClient client = getHttpClient(httpHost, username, password)) {
            respText = this.get(client, httpHost, uri, params, config);
        }
        return respText;
    }

    private String get(CloseableHttpClient client, HttpHost httpHost, URI uri, List<NameValuePair> params,
            RequestConfig config) throws URISyntaxException, IOException {
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
        if (config != null) {
            httpGet.setConfig(config);
        }
        // 5. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
        return execute(client, httpHost, httpGet);
    }

    /**
     * POST方式请求url
     *
     * @param url 请求地址
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String post(String url) throws URISyntaxException, IOException {
        return get(url);
    }

    /**
     * POST方式请求url
     *
     * @param url         请求地址
     * @param postMessage 请求体，直接将参数写入POST请求的body中。
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String post(String url, String postMessage) throws URISyntaxException, IOException {
        HttpEntity httpEntity = null;
        if (StringUtils.isNotEmpty(postMessage)) {
            httpEntity = new StringEntity(postMessage, Consts.UTF_8);
        }
        return post(url, httpEntity);
    }

    /**
     * POST方式请求url
     *
     * @param url         请求地址
     * @param postMessage 请求体，直接将参数写入POST请求的body中。
     * @param timeout     超时时间，毫秒
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String post(String url, String postMessage, int timeout) throws URISyntaxException, IOException {
        HttpEntity httpEntity = null;
        if (StringUtils.isNotEmpty(postMessage)) {
            httpEntity = new StringEntity(postMessage, Consts.UTF_8);
        }
        RequestConfig config = RequestConfig.custom().setSocketTimeout(timeout).setConnectTimeout(timeout).build();
        return post(url, httpEntity, config);
    }

    /**
     * POST方式请求url
     *
     * @param url         请求地址
     * @param postMessage 请求体，直接将参数写入POST请求的body中。
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String post(String url, String postMessage, String contentType) throws URISyntaxException, IOException {
        HttpEntity httpEntity = null;
        if (StringUtils.isNotEmpty(postMessage)) {
            httpEntity = new StringEntity(postMessage, Consts.UTF_8);
        }
        return post(url, httpEntity, contentType);
    }

    /**
     * POST方式请求url
     *
     * @param url        请求地址
     * @param httpEntity 请求实体，包含http请求所需参数。
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String post(String url, HttpEntity httpEntity) throws URISyntaxException, IOException {
        return post(url, httpEntity, ContentType.APPLICATION_JSON.getMimeType());
    }

    /**
     * POST方式请求url
     *
     * @param url        请求地址
     * @param httpEntity 请求实体，包含http请求所需参数。
     * @param config     请求配置
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String post(String url, HttpEntity httpEntity, RequestConfig config) throws URISyntaxException, IOException {
        return post(url, httpEntity, ContentType.APPLICATION_JSON.getMimeType(), config);
    }

    /**
     * POST方式请求url
     *
     * @param url         请求地址
     * @param httpEntity  请求实体，包含http请求所需参数。
     * @param contentType 请求头
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String post(String url, HttpEntity httpEntity, String contentType) throws URISyntaxException, IOException {
        RequestConfig config = RequestConfig.custom().setSocketTimeout(30000).setConnectTimeout(30000).build();
        return post(url, httpEntity, contentType, config);
    }

    /**
     * POST方式请求url
     *
     * @param url         请求地址
     * @param httpEntity  请求实体，包含http请求所需参数。
     * @param contentType 请求头
     * @param config      请求配置
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String post(String url, HttpEntity httpEntity, String contentType, RequestConfig config)
            throws URISyntaxException, IOException {
        // 1. 验证输入url的有效性：url没有有效的host或url为相对路径，则url无效。
        URI uri = (new URIBuilder(url)).build();
        HttpHost httpHost = URIUtils.extractHost(uri);
        if (httpHost == null) {
            throw new IllegalArgumentException("缺少有效的HOST");
        }
        String respText;
        // 2. 创建HttpClient对象
        try (CloseableHttpClient client = getHttpClient()) {
            // 3. 创建请求方法的实例，并指定请求URL。如果需要发送GET请求，创建HttpGet对象；如果需要发送POST请求，创建HttpPost对象。
            HttpPost httpPost = new HttpPost(uri);
            if (logger.isDebugEnabled()) {
                logger.debug("executing request: ", httpPost.getRequestLine());
            }
            // 4. 如果需要发送请求参数，可调用HttpGet、HttpPost共同的setParams(HttpParams params)方法来添加请求参数；
            //    对于HttpPost对象而言，也可调用setEntity(HttpEntity entity)方法来设置请求参数。
            if (httpEntity != null) {
                httpPost.setEntity(httpEntity);
            }
            if (contentType != null) {
                httpPost.setHeader(HttpHeaders.CONTENT_TYPE, contentType);
            } else {
                // httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.TEXT_PLAIN.withCharset("UTF-8").getMimeType());
                // 1. 当前系统中多数调用使用http+json方式
                // 2. 有部分系统使用netty服务器，不接受非application/json以外的其他方式
                httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
            }
            if (config != null) {
                httpPost.setConfig(config);
            }
            Header[] headers = httpPost.getAllHeaders();
            for (Header h : headers) {
                logger.debug("{}={}", h.getName(), h.getValue());
            }
            // 5. 调用HttpClient对象的execute(HttpUriRequest request)发送请求，该方法返回一个HttpResponse。
            respText = execute(client, httpHost, httpPost);
        }
        return respText;
    }

    /**
     * 以form表单方式请求url
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String form(String url, Map<String, String> params) throws URISyntaxException, IOException {
        return this.form(url, params, DEFAULT_CONTENT_TYPE);
    }

    /**
     * 以form表单方式请求url
     *
     * @param url         请求地址
     * @param params      请求参数
     * @param contentType 请求数据类型
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String form(String url, Map<String, String> params, String contentType)
            throws URISyntaxException, IOException {
        List<NameValuePair> nameValuePairs = new ArrayList<>(params.size());
        for (Map.Entry<String, String> entry : params.entrySet()) {
            nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return this.form(url, nameValuePairs, contentType);
    }

    /**
     * 以form表单方式请求url
     *
     * @param url    请求地址
     * @param params 请求参数
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String form(String url, List<NameValuePair> params) throws URISyntaxException, IOException {
        return this.form(url, params, DEFAULT_CONTENT_TYPE);
    }

    /**
     * 以form方式请求url
     *
     * @param url         请求地址
     * @param params      请求参数
     * @param contentType 请求数据类型
     * @return url响应结果，java.lang.String类型。
     * @throws java.net.URISyntaxException 输入的url不合法
     * @throws java.io.IOException         其他IO错误
     */
    public String form(String url, List<NameValuePair> params, String contentType)
            throws URISyntaxException, IOException {
        HttpEntity httpEntity;
        if (params == null) {
            httpEntity = new StringEntity("", Consts.UTF_8);
        } else {
            httpEntity = new UrlEncodedFormEntity(params, Consts.UTF_8);
        }
        return this.post(url, httpEntity, contentType);
    }


    /**
     * 通过client，以httpRequest为请求参数，调用httpHost，并解析响应，返回{@code java.lang.String} 类型的响应内容。
     *
     * @param client      HTTP请求客户端
     * @param httpHost    包含HTTP连接所需参数（远端Host名、端口、scheme）的对象
     * @param httpRequest 包含从客户端到服务端的请求信息
     * @return url响应结果
     * @throws java.io.IOException         其他IO错误
     * @throws java.net.URISyntaxException 如果返回为301或302跳转，则会调用GET请求，如果返回的url不正确，会抛出该异常。
     */
    protected String execute(CloseableHttpClient client, HttpHost httpHost, HttpRequest httpRequest)
            throws IOException, URISyntaxException {
        String respText = null;
        try (CloseableHttpResponse response = client.execute(httpHost, httpRequest)) {
            // 调用HttpResponse的getAllHeaders()、getHeaders(String name)等方法可获取服务器的响应头；
            Header[] headers = response.getAllHeaders();
            if (logger.isDebugEnabled()) {
                for (Header header : headers) {
                    logger.debug("the headers of this message ：{} = {}", header.getName(), header.getValue());
                }
            }
            StatusLine statusLine = response.getStatusLine();
            if (logger.isDebugEnabled()) {
                logger.debug("the status line of this response is {}", statusLine.getStatusCode());
            }
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                // 调用HttpResponse的getEntity()方法可获取HttpEntity对象，该对象包装了服务器的响应内容。
                // EntityUtils.toString获取response返回的content，首先获取响应头的字符集，如果响应头中无定义，使用UTF-8字符集。
                respText = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
                logger.debug("the message entity of this response:\n\r{}", respText);
            } else if (statusLine.getStatusCode() == HttpStatus.SC_MOVED_PERMANENTLY
                    || statusLine.getStatusCode() == HttpStatus.SC_MOVED_TEMPORARILY) {
                return get(response.getLastHeader(HttpHeaders.LOCATION).getValue());
            }
        }
        return respText;
    }
}
