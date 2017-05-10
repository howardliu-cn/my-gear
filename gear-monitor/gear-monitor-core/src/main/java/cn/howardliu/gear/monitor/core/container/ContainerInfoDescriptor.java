package cn.howardliu.gear.monitor.core.container;

import java.util.List;

/**
 * <br>created at 17-5-10
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public interface ContainerInfoDescriptor {
    List<ConnectorInfo> getConnectionInfoList() throws Exception;

    ServerInfo getServerInfo() throws Exception;
}
