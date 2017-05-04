package cn.howardliu.gear.monitor.os;

import java.util.HashSet;
import java.util.Set;

/**
 * <br>created at 16-12-9
 *
 * @author liuxh
 * @since 1.0.1
 */
public class NetworkInterfaceInfo {
    private String name;
    private String displayName;
    private String mac;
    private Set<String> hostAddresses = new HashSet<>();
    private Set<NetworkInterfaceInfo> children = new HashSet<>();

    public NetworkInterfaceInfo() {
    }

    private NetworkInterfaceInfo(Builder builder) {
        this.name = builder.name;
        this.displayName = builder.displayName;
        this.mac = builder.mac;
        this.hostAddresses.addAll(builder.hostAddresses);
        this.children.addAll(builder.children);
    }

    public String getName() {
        return name;
    }

    public NetworkInterfaceInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public NetworkInterfaceInfo setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getMac() {
        return mac;
    }

    public NetworkInterfaceInfo setMac(String mac) {
        this.mac = mac;
        return this;
    }

    public Set<String> getHostAddresses() {
        return hostAddresses;
    }

    public NetworkInterfaceInfo setHostAddresses(Set<String> hostAddresses) {
        this.hostAddresses = hostAddresses;
        return this;
    }

    public Set<NetworkInterfaceInfo> getChildren() {
        return children;
    }

    public NetworkInterfaceInfo setChildren(Set<NetworkInterfaceInfo> children) {
        this.children = children;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        NetworkInterfaceInfo that = (NetworkInterfaceInfo) o;
        return displayName.equals(that.displayName) && mac.equals(that.mac) && name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + displayName.hashCode();
        result = 31 * result + mac.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "NetWorkInfo{" +
                "name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", mac='" + mac + '\'' +
                ", hostAddresses=" + hostAddresses +
                ", children=" + children +
                '}';
    }

    static class Builder {
        private final Set<String> hostAddresses = new HashSet<>();
        private final Set<NetworkInterfaceInfo> children = new HashSet<>();
        private String name;
        private String displayName;
        private String mac;

        Builder name(String name) {
            this.name = name;
            return this;
        }

        Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        Builder mac(String mac) {
            this.mac = mac;
            return this;
        }

        Builder hostAddresses(Set<String> hostAddresses) {
            this.hostAddresses.addAll(hostAddresses);
            return this;
        }

        Builder children(Set<NetworkInterfaceInfo> children) {
            this.children.addAll(children);
            return this;
        }

        NetworkInterfaceInfo build() {
            return new NetworkInterfaceInfo(this);
        }
    }
}
