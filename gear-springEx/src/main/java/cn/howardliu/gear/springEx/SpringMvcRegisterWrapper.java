package cn.howardliu.gear.springEx;

import cn.howardliu.gear.monitor.tomcat.TomcatInfoUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PreDestroy;
import java.io.IOException;

/**
 * <br>created at 16-7-30
 *
 * @author liuxh
 * @since 1.0.0
 */
public class SpringMvcRegisterWrapper implements ISpringMvcRegister {
    public static final String DEFAULT_IP_ENV_NAME = "LOCAL_IP_ADDRESS";
    private static final Logger logger = LoggerFactory.getLogger(SpringMvcRegisterWrapper.class);
    private String ipEnvName = DEFAULT_IP_ENV_NAME;

    private SpringMvcRegister springMvcRegister;
    private SpringMvcServiceScanner springMvcServiceScanner;
    private CuratorFramework client;
    private String basePath;
    private String preServiceName = "";
    private String localIp = null;
    private Integer localPort = null;

    @Override
    public void regist() throws Exception {
        if (this.springMvcRegister != null) {
            this.springMvcRegister.close();
        }
        this.springMvcRegister = new SpringMvcRegister(this.client, this.basePath, this.getLocalIp(), this.getPort());
        this.springMvcRegister.setPreServiceName(this.preServiceName);
        this.springMvcRegister.setSpringMvcServiceScanner(this.springMvcServiceScanner);
        this.springMvcRegister.regist();
    }

    @Override
    @Scheduled(initialDelay = 10000, fixedDelay = 30000)
    public void refresh() throws Exception {
        if (springMvcRegister != null) {
            this.springMvcRegister.refresh();
        }
    }

    @Override
    @PreDestroy
    public void close() throws IOException {
        this.springMvcRegister.close();
    }

    public String getLocalIp() {
        if (localIp != null) {
            return localIp;
        }
        String ipEnvName = Validate.notNull(this.getIpEnvName(), "本地IP地址环境变量[默认为" + DEFAULT_IP_ENV_NAME + "]不能为空，请检查！");
        localIp = System.getenv(ipEnvName);
        if (StringUtils.isBlank(localIp)) {
            localIp = System.getProperty(ipEnvName);
        }
        return Validate.notNull(localIp, "本地IP地址不能为空，请检查！");
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public Integer getPort() throws Exception {
        if (localPort != null) {
            return localPort;
        }
        // TODO 需要根据容器进行判断
        localPort = TomcatInfoUtils.getPort();
        return Validate.notNull(localPort, "服务端口不能为空，请检查！");
    }

    public String getIpEnvName() {
        return ipEnvName;
    }

    public void setIpEnvName(String ipEnvName) {
        this.ipEnvName = ipEnvName;
    }

    @Required
    public void setSpringMvcServiceScanner(SpringMvcServiceScanner springMvcServiceScanner) {
        this.springMvcServiceScanner = springMvcServiceScanner;
    }

    @Required
    public void setClient(CuratorFramework client) {
        this.client = client;
    }

    @Required
    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setPreServiceName(String preServiceName) {
        this.preServiceName = preServiceName;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }
}
