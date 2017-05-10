package cn.howardliu.gear.monitor.core.os;

import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

/**
 * <br>created at 17-5-4
 *
 * @author liuxh
 * @version 1.0.0
 * @since 1.0.0
 */
public class NetworkInfoTest {
    @Test
    public void testGetNetworkInfo() throws Exception {
        Set<NetworkInterfaceInfo> networkInterfaceInfo = NetworkInfo.getNetworkInfo();
        System.out.println(networkInterfaceInfo);
        assertNotNull(networkInterfaceInfo);
    }

    @Test
    public void testGetNetWorkInfo() throws Exception {
        System.out.println(NetworkInfo.getNetWorkInfo("wlp7s0"));
        System.out.println(NetworkInfo.getNetWorkInfo("enp8s0"));
    }
}