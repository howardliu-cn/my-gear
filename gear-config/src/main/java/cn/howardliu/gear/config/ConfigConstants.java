package cn.howardliu.gear.config;

/**
 * <br>created at 16-12-20
 *
 * @author liuxh
 * @since 1.0.1
 */
public class ConfigConstants {
    public static final String GEAR_CONFIG_PARAM_INFO = "gear-config-param-info";

    public static final String SQL_CREATE_CONFIG_PARAM_TABLE_MYSQL = "CREATE TABLE `config_param` (\n" +
            "  `sid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增主键',\n" +
            "  `name` varchar(128) NOT NULL COMMENT '配置字段名',\n" +
            "  `type` varchar(256) NOT NULL,\n" +
            "  `value` varchar(256) NOT NULL,\n" +
            "  PRIMARY KEY (`sid`),\n" +
            "  UNIQUE KEY `name` (`name`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8";
}
