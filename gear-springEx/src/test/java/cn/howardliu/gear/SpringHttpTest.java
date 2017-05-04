package cn.howardliu.gear;

import org.apache.commons.io.Charsets;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * <br>created at 16-10-10
 *
 * @author liuxh
 * @since 1.0.0
 */
@Ignore
public class SpringHttpTest {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Test
    public void test() throws Exception {
        String url = "http://10.6.4.22:8042/pcm-inner-sdc/pcmInnerSupplyInfo/getSupCodesFromPcmByShopCodeAndManager.htm";
//        String url = "http://10.6.3.214:8042/pcm-inner-sdc/pcmInnerSupplyInfo/getSupCodesFromPcmByShopCodeAndManager.htm";
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        ClientHttpRequest client = factory.createRequest(new URI(url), HttpMethod.POST);
        client.getHeaders().set(HttpHeaders.CONTENT_TYPE, "application/json");
        String params = "{\"paraList\":[{\"shopCode\":\"D001\",\"managerList\":[{\"level\":\"1\",\"manager\":\"02\"},{\"level\":\"1\",\"manager\":\"04\"},{\"level\":\"1\",\"manager\":\"08\"},{\"level\":\"1\",\"manager\":\"10\"},{\"level\":\"1\",\"manager\":\"90\"},{\"level\":\"2\",\"manager\":\"0101\"},{\"level\":\"2\",\"manager\":\"0702\"},{\"level\":\"2\",\"manager\":\"0703\"},{\"level\":\"2\",\"manager\":\"0704\"},{\"level\":\"2\",\"manager\":\"0716\"},{\"level\":\"2\",\"manager\":\"0717\"}]}]}";
        client.getBody().write(params.getBytes(Charsets.UTF_8));
        ClientHttpResponse res = client.execute();
        BufferedReader br = new BufferedReader(new InputStreamReader(res.getBody()));
        StringBuilder result = new StringBuilder();
        String str;
        while ((str = br.readLine()) != null) {
            result.append(str).append("\n");
        }
        System.out.println(result.toString());
    }
}
