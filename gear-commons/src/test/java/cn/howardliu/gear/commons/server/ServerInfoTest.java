package cn.howardliu.gear.commons.server;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * <br>created at 16-12-9
 *
 * @author liuxh
 * @since 1.0.1
 */
public class ServerInfoTest {
    @Test
    public void testGetNetworkInfo() throws Exception {
        Set<NetWorkInterfaceInfo> networkInterfaceInfo = ServerInfo.getNetworkInfo();
        System.out.println(networkInterfaceInfo);
        assertNotNull(networkInterfaceInfo);
    }

    @Test
    public void testGetNetWorkInfo() throws Exception {
        System.out.println(ServerInfo.getNetWorkInfo("wlp7s0"));
        System.out.println(ServerInfo.getNetWorkInfo("enp8s0"));
    }
}