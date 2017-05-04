package cn.howardliu.gear.monitor.os;

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
public class NetworkInfo {
    private static final Logger logger = LoggerFactory.getLogger(NetworkInfo.class);

    public static Set<NetworkInterfaceInfo> getNetworkInfo() {
        try {
            return getNetWorkInfo(NetworkInterface.getNetworkInterfaces());
        } catch (SocketException e) {
            logger.error("获取本机网卡设备信息失败", e);
        }
        return new HashSet<>();
    }

    public static NetworkInterfaceInfo getNetWorkInfo(String name) {
        try {
            return getNetWorkInfo(NetworkInterface.getByName(name));
        } catch (SocketException e) {
            logger.error("获取本机网卡设备信息失败", e);
        }
        return null;
    }

    private static Set<NetworkInterfaceInfo> getNetWorkInfo(@NotNull Enumeration<NetworkInterface> interfaces) {
        Set<NetworkInterfaceInfo> networkInterfaceInfoSet = new HashSet<>();
        try {
            while (interfaces.hasMoreElements()) {
                NetworkInterfaceInfo networkInterfaceInfo = getNetWorkInfo(interfaces.nextElement());
                if (networkInterfaceInfo != null) {
                    networkInterfaceInfoSet.add(networkInterfaceInfo);
                }
            }
        } catch (SocketException e) {
            logger.error("获取网络设备信息失败", e);
        }
        return networkInterfaceInfoSet;
    }

    private static NetworkInterfaceInfo getNetWorkInfo(@NotNull NetworkInterface networkInterface)
            throws SocketException {
        if (networkInterface == null
                || networkInterface.isLoopback()
//                || networkInterface.isVirtual()
                || !networkInterface.isUp()) {
            return null;
        }
        return new NetworkInterfaceInfo.Builder()
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
        InetAddress address;
        while (addresses.hasMoreElements()) {
            address = addresses.nextElement();
            if (!address.isSiteLocalAddress()) {
                continue;
            }
            hostAddresses.add(address.getHostAddress());
        }
        return hostAddresses;
    }
}
