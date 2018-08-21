package cn.howardliu.gear.cloud.tester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * <br>created at 18-8-17
 *
 * @author liuxh
 * @since 1.0.0
 */
@SpringBootApplication
public class GearTesterApplication {
    public static void main(String[] args) {
        SpringApplication.run(GearTesterApplication.class, args);
    }
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @PostConstruct
    public void getEsData() {
        Map mapping = elasticsearchTemplate.getMapping("filebeat-nginx-6.3.2-2018.08.17", "doc");
        for (Object o : mapping.keySet()) {
            System.out.println(o);
            System.out.println(mapping.get(o));
        }
    }
}
