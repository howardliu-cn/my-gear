package cn.howardliu.gear.config.service;


import cn.howardliu.gear.config.pojo.ConfigParam;

/**
 * <br>created at 16-5-9
 *
 * @author liuxh
 * @since 1.0.1
 */
public interface ConfigParamService {
    ConfigParam getConfigByName(String name) throws Exception;

    ConfigParam setConfigParam(String name, String type, String value) throws Exception;
}
