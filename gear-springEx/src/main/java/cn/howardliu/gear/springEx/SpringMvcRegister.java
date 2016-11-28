package cn.howardliu.gear.springEx;

import cn.howardliu.gear.zk.discovery.ServiceDescription;
import cn.howardliu.gear.zk.discovery.ServiceRegisterWrapper;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

import java.io.IOException;
import java.util.Collection;

/**
 * <br>created at 16-5-6
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SpringMvcRegister implements ISpringMvcRegister {
    private static final Logger logger = LoggerFactory.getLogger(SpringMvcRegister.class);
    private static ServiceRegisterWrapper serviceRegisterWrapper;
    private SpringMvcServiceScanner springMvcServiceScanner;
    private String preServiceName = "";

    public SpringMvcRegister(CuratorFramework client, String basePath, String address, Integer port) throws Exception {
        serviceRegisterWrapper = new ServiceRegisterWrapper(client, basePath, address, port);
    }

    @Override
    public synchronized void regist() throws Exception {
        logger.info("注册开始。。。");
        Collection<ServiceDescription> serviceList = springMvcServiceScanner.serviceList();
        for (ServiceDescription description : serviceList) {
            String trueServiceName = preServiceName + "-" + description.getServiceName();
            trueServiceName = trueServiceName.replaceAll("--+", "-").replaceAll("-$", "").replaceAll("^-", "");
            ServiceDescription trueService = new ServiceDescription(trueServiceName, description.getUri(),
                    description.getFullName());
            serviceRegisterWrapper.regist(trueService);
        }
        logger.info("注册结束");
    }

    @Override
    public synchronized void refresh() throws Exception {
        // TODO 待增加描述信息
        logger.info("刷新开始。。。");
        serviceRegisterWrapper.refresh("update at " + System.currentTimeMillis());
        logger.info("刷新结束");
    }

    @Override
    public void close() throws IOException {
        serviceRegisterWrapper.close();
    }

    @Required
    public void setSpringMvcServiceScanner(SpringMvcServiceScanner springMvcServiceScanner) {
        this.springMvcServiceScanner = springMvcServiceScanner;
    }

    public void setPreServiceName(String preServiceName) {
        if (preServiceName == null) {
            logger.warn("服务名前缀不能为null，将使用默认的空字符串");
            this.preServiceName = "";
        } else {
            this.preServiceName = preServiceName.trim();
        }
    }
}
