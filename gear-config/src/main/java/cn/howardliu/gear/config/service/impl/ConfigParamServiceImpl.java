package cn.howardliu.gear.config.service.impl;

import cn.howardliu.gear.config.mapper.ConfigParamMapper;
import cn.howardliu.gear.config.pojo.ConfigParam;
import cn.howardliu.gear.config.service.ConfigParamService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import static cn.howardliu.gear.config.ConfigConstants.GEAR_CONFIG_PARAM_INFO;

/**
 * <br>created at 16-5-9
 *
 * @author liuxh
 * @since 1.2.0
 */
@Service("configParamService")
public class ConfigParamServiceImpl implements ConfigParamService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigParamServiceImpl.class);
    @Autowired
    private ConfigParamMapper configParamMapper;

    @Override
    @Cacheable(value = GEAR_CONFIG_PARAM_INFO, key = "'['+#name+']'", condition = "#name != null", unless = "#result == null")
    public ConfigParam getConfigByName(String name) throws Exception {
        if (StringUtils.isBlank(name)) {
            logger.warn("参数名[name={}]为空，查询结果为空", name);
            return null;
        }
        return configParamMapper.getParam(name);
    }

    @Override
    @CachePut(value = GEAR_CONFIG_PARAM_INFO, key = "'['+#name+']'", condition = "#name != null")
    public ConfigParam setConfigParam(String name, String type, String value) throws Exception {
        if (StringUtils.isBlank(name) || value == null) {
            logger.error("参数[name={}, type={}, value={}]不符合要求", name, type, value);
            throw new IllegalArgumentException("输入参数有误，请检查！");
        }
        ConfigParam param = new ConfigParam();
        param.setName(name);
        param.setType(type);
        param.setValue(value);
        configParamMapper.setParam(param);
        return param;
    }
}
