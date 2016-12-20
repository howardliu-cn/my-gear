package cn.howardliu.gear.commons.server;

import java.util.HashSet;
import java.util.Set;

/**
 * <br>created at 16-12-9
 *
 * @author liuxh
 * @since 1.0.1
 */
public class NetWorkInterfaceInfo {
    private String name;
    private String displayName;
    private String mac;
    private Set<String> hostAddresses = new HashSet<>();
    private Set<NetWorkInterfaceInfo> children = new HashSet<>();

    public NetWorkInterfaceInfo(){
    }

    public String getName() {
        return name;
    }

    public NetWorkInterfaceInfo setName(String name) {
        this.name = name;
        return this;
    }

    public String getDisplayName() {
        return displayName;
    }

    public NetWorkInterfaceInfo setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public String getMac() {
        return mac;
    }

    public NetWorkInterfaceInfo setMac(String mac) {
        this.mac = mac;
        return this;
    }

    public Set<String> getHostAddresses() {
        return hostAddresses;
    }

    public NetWorkInterfaceInfo setHostAddresses(Set<String> hostAddresses) {
        this.hostAddresses = hostAddresses;
        return this;
    }

    public Set<NetWorkInterfaceInfo> getChildren() {
        return children;
    }

    public NetWorkInterfaceInfo setChildren(Set<NetWorkInterfaceInfo> children) {
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
        NetWorkInterfaceInfo that = (NetWorkInterfaceInfo) o;
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

    public static class Builder {
        private String name;
        private String displayName;
        private String mac;
        private final Set<String> hostAddresses = new HashSet<>();
        private final Set<NetWorkInterfaceInfo> children = new HashSet<>();

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder mac(String mac) {
            this.mac = mac;
            return this;
        }

        public Builder hostAddresses(Set<String> hostAddresses) {
            this.hostAddresses.addAll(hostAddresses);
            return this;
        }

        public Builder children(Set<NetWorkInterfaceInfo> children) {
            this.children.addAll(children);
            return this;
        }

        public NetWorkInterfaceInfo build() {
            return new NetWorkInterfaceInfo(this);
        }
    }

    private NetWorkInterfaceInfo(Builder builder) {
        this.name = builder.name;
        this.displayName = builder.displayName;
        this.mac = builder.mac;
        this.hostAddresses.addAll(builder.hostAddresses);
        this.children.addAll(builder.children);
    }
}
