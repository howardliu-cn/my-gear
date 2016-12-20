package cn.howardliu.gear.commons.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * <br>created at 16-12-9
 *
 * @author liuxh
 * @since 1.0.1
 */
public class ServerInfo {
    private static final Logger logger = LoggerFactory.getLogger(ServerInfo.class);

    public static Set<NetWorkInterfaceInfo> getNetworkInfo() {
        try {
            return getNetWorkInfo(NetworkInterface.getNetworkInterfaces());
        } catch (SocketException e) {
            logger.error("获取本机网卡设备信息失败", e);
        }
        return new HashSet<>();
    }

    public static NetWorkInterfaceInfo getNetWorkInfo(String name) {
        try {
            return getNetWorkInfo(NetworkInterface.getByName(name));
        } catch (SocketException e) {
            logger.error("获取本机网卡设备信息失败", e);
        }
        return null;
    }

    public static Set<NetWorkInterfaceInfo> getNetWorkInfo(@NotNull Enumeration<NetworkInterface> interfaces) {
        Set<NetWorkInterfaceInfo> netWorkInterfaceInfoSet = new HashSet<>();
        try {
            while (interfaces.hasMoreElements()) {
                NetWorkInterfaceInfo netWorkInterfaceInfo = getNetWorkInfo(interfaces.nextElement());
                if (netWorkInterfaceInfo != null) {
                    netWorkInterfaceInfoSet.add(netWorkInterfaceInfo);
                }
            }
        } catch (SocketException e) {
            logger.error("获取网络设备信息失败", e);
        }
        return netWorkInterfaceInfoSet;
    }

    private static NetWorkInterfaceInfo getNetWorkInfo(@NotNull NetworkInterface networkInterface)
            throws SocketException {
        if (networkInterface == null
                || networkInterface.isLoopback()
//                || networkInterface.isVirtual()
                || !networkInterface.isUp()) {
            return null;
        }
        return new NetWorkInterfaceInfo.Builder()
                .name(getName(networkInterface))
                .displayName(getDisplayName(networkInterface))
                .mac(getMac(networkInterface))
                .hostAddresses(getAddresses(networkInterface))
                .children(getNetWorkInfo(networkInterface.getSubInterfaces()))
                .build();
    }

    private static String getName(@NotNull NetworkInterface networkInterface) {
        return networkInterface.getName();
    }

    private static String getDisplayName(@NotNull NetworkInterface networkInterface) {
        return networkInterface.getDisplayName();
    }

    private static String getMac(@NotNull NetworkInterface networkInterface) throws SocketException {
        byte[] mac = networkInterface.getHardwareAddress();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append(":");
            }
            int temp = mac[i] & 0xff;
            String str = Integer.toHexString(temp);
            if (str.length() == 1) {
                sb.append("0").append(str);
            } else {
                sb.append(str);
            }
        }
        return sb.toString();
    }

    private static Set<String> getAddresses(@NotNull NetworkInterface networkInterface) {
        Set<String> hostAddresses = new HashSet<>();
        Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
        InetAddress ip;
        while (addresses.hasMoreElements()) {
            ip = addresses.nextElement();
            if (!ip.isSiteLocalAddress()) {
                continue;
            }
            hostAddresses.add(ip.getHostAddress());
        }
        return hostAddresses;
    }
}
