package cn.howardliu.gear.toolkit.server;

import cn.howardliu.gear.commons.server.ServerInfo;
import org.apache.commons.lang3.StringUtils;

/**
 * <br>created at 16-12-9
 *
 * @author liuxh
 * @since 1.0.1
 */
public class ServerInfoMain {
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println(ServerInfo.getNetworkInfo());
        } else {
            for (String arg : args) {
                if (StringUtils.isNotBlank(arg)) {
                    System.out.println(ServerInfo.getNetWorkInfo(arg.trim()));
                }
            }
        }
    }
}
