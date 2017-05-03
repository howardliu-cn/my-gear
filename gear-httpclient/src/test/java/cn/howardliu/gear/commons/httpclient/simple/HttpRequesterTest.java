package cn.howardliu.gear.commons.httpclient.simple;

import cn.howardliu.gear.commons.NameValuePair;
import cn.howardliu.gear.httpclient.simple.SimpleHttpRequester;
import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertNotNull;

/**
 * <br>created at 16-8-5
 *
 * @author liuxh
 * @since 1.0.0
 */
public class HttpRequesterTest {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void test1() throws Exception {
        String result = SimpleHttpRequester.getHttpRequester()
                .get("http://10.6.4.74:15672/api/overview/", null, "admin", "12345");
        System.out.println(result);
        assertNotNull(result);
    }

    @Test
    public void testPost() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(1000, r -> {
            Thread t = new Thread(r);
            t.setDaemon(true);
            t.setName("post-request-thread-" + t.getId());
            return t;
        });
//        final String url = "http://10.6.3.214:8042/pcm-inner-sdc/pcmInnerSupplyInfo/getSupCodesFromPcmByShopCodeAndManager.htm";
        final String url = "http://10.6.3.118/pcm-inner-sdc/pcmInnerSupplyInfo/getSupCodesFromPcmByShopCodeAndManager.htm";
        final String requestBody = "{\"paraList\":[{\"shopCode\":\"D001\",\"managerList\":[{\"level\":\"1\",\"manager\":\"02\"},{\"level\":\"1\",\"manager\":\"04\"},{\"level\":\"1\",\"manager\":\"08\"},{\"level\":\"1\",\"manager\":\"10\"},{\"level\":\"1\",\"manager\":\"90\"},{\"level\":\"2\",\"manager\":\"0101\"},{\"level\":\"2\",\"manager\":\"0702\"},{\"level\":\"2\",\"manager\":\"0703\"},{\"level\":\"2\",\"manager\":\"0704\"},{\"level\":\"2\",\"manager\":\"0716\"},{\"level\":\"2\",\"manager\":\"0717\"}]}]}";
        int times = 200;
        final CountDownLatch countDownLatch = new CountDownLatch(times);
        for (int i = 0; i < times; i++) {
            final int finalI = i;
            executorService.submit(() -> {
                String result = null;
                try {
                    result = SimpleHttpRequester.getHttpRequester().post(url, requestBody);
                } catch (Exception e) {
                    logger.error("请求数据异常:{}", e.getMessage());
                }
                logger.warn(finalI + "");
                try {
                    JSON.parseObject(result);
                } catch (Exception e) {
                    logger.error("解析结果发生异常，异常数据{}", result);
                }
                countDownLatch.countDown();
            }, null);
        }
        countDownLatch.await();
    }

    @Test
    public void test2() throws Exception {
        for (int i = 0; i < 100; i++) {
            try {
                NameValuePair<String> pair = new NameValuePair<>(
                        "http://10.6.4.22:8042/pcm-inner-sdc/categoryPis/getIndustryCategorys.htm",
//                        "http://10.6.3.214:8942/pcm-inner-sdc/categoryPis/getIndustryCategorys.htm",
                        "{\"StoreCode\":\"21011\"}"
                );
//                NameValuePair<String> pair = new NameValuePair<>(
//                        "http://10.6.4.22:8042/pcm-inner-sdc/categoryPis/getIndustryCategorys.htm",
////                        "http://10.6.3.214:8042/pcm-inner-sdc/pcmInnerSupplyInfo/getSupCodesFromPcmByShopCodeAndManager.htm",
//                        "{\"paraList\":[{\"shopCode\":\"D001\",\"managerList\":[{\"level\":\"1\",\"manager\":\"02\"},{\"level\":\"1\",\"manager\":\"04\"},{\"level\":\"1\",\"manager\":\"08\"},{\"level\":\"1\",\"manager\":\"10\"},{\"level\":\"1\",\"manager\":\"90\"},{\"level\":\"2\",\"manager\":\"0101\"},{\"level\":\"2\",\"manager\":\"0702\"},{\"level\":\"2\",\"manager\":\"0703\"},{\"level\":\"2\",\"manager\":\"0704\"},{\"level\":\"2\",\"manager\":\"0716\"},{\"level\":\"2\",\"manager\":\"0717\"}]}]}"
//                );
                String result = SimpleHttpRequester.getHttpRequester().post(pair.getName(), pair.getValue());
                JSON.parseArray(result);
                System.out.println(i);
            } catch (Exception e) {
                logger.error("失败i={}", i, e);
            }
        }
    }

    @Test
    @Ignore
    public void test() throws Exception {
        final String postUrl = "/MQInput/itgService/jsonTest.do";
        int count = 10000;
        for (int i = 0; i < count; i++) {
            SimpleHttpRequester.getHttpRequester().get("http://10.6.3.95:8010" + postUrl);
            SimpleHttpRequester.getHttpRequester().get("http://10.6.3.96:8010" + postUrl);
            SimpleHttpRequester.getHttpRequester().get("http://10.6.3.95:8012" + postUrl);
            SimpleHttpRequester.getHttpRequester().get("http://10.6.3.96:8012" + postUrl);
            SimpleHttpRequester.getHttpRequester().get("http://10.6.3.95:8014" + postUrl);
            SimpleHttpRequester.getHttpRequester().get("http://10.6.3.96:8014" + postUrl);
            SimpleHttpRequester.getHttpRequester().get("http://10.6.3.95:8016" + postUrl);
            SimpleHttpRequester.getHttpRequester().get("http://10.6.3.96:8016" + postUrl);
            SimpleHttpRequester.getHttpRequester().get("http://10.6.3.95:8090" + postUrl);
            SimpleHttpRequester.getHttpRequester().get("http://10.6.3.96:8090" + postUrl);
            System.out.println(i);
        }
    }
}