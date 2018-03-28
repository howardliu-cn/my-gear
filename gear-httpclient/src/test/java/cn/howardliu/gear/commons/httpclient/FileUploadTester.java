package cn.howardliu.gear.commons.httpclient;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * <br>created at 17-11-15
 *
 * @author liuxh
 * @since 1.0.0
 */
public class FileUploadTester {
    private static final Logger logger = LoggerFactory.getLogger(FileUploadTester.class);

    @Test
    @Ignore
    public void test() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8080/spring-mvc-jsonp/file/upload");
        httpPost.setEntity(
                MultipartEntityBuilder.create()
                        .addPart("username", new StringBody("a", ContentType.TEXT_PLAIN))
                        .addPart("nickname", new StringBody("b", ContentType.TEXT_PLAIN))
                        .addPart("files", new FileBody(new File("/home/liuxh/Pictures/favicon.png")))
                        .addPart("files", new FileBody(new File("/home/liuxh/Pictures/Coder.png")))
                        .build()
        );
        CloseableHttpResponse response = httpClient.execute(httpPost);
        System.out.println(EntityUtils.toString(response.getEntity()));
        response.close();
        httpClient.close();
    }
}
