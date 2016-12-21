package cn.howardliu.gear.config.service.impl;

import cn.howardliu.gear.config.pojo.ConfigParam;
import cn.howardliu.gear.config.service.ConfigParamService;
import cn.howardliu.gear.config.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <br/>created at 16-5-9
 *
 * @author liuxh
 * @since 1.0.1
 */
@Service("configService")
public class ConfigServiceImpl implements ConfigService {
    private static final Logger logger = LoggerFactory.getLogger(ConfigServiceImpl.class);
    @Autowired
    private ConfigParamService configParamService;

    public <T> T getValue(String fieldName, T defaultValue, Converter<T> converter) {
        try {
            ConfigParam configParam = configParamService.getConfigByName(fieldName);
            if (configParam == null) {
                setValue(fieldName, defaultValue);
            } else {
                return converter.convert(configParam.getValue());
            }
        } catch (Exception e) {
            logger.error("查询[fieldName={}]配置失败，将使用默认值{}", fieldName, defaultValue, e);
        }
        return defaultValue;
    }

    private boolean setValue(String fieldName, Object defaultValue) {
        try {
            configParamService.setConfigParam(fieldName, defaultValue.getClass().getName(), defaultValue.toString());
            logger.info("设置[fieldName={}, defaultValue={}]成功", fieldName, defaultValue);
            return true;
        } catch (Exception e) {
            logger.error("设置[fieldName={}, defaultValue={}]失败", fieldName, defaultValue, e);
        }
        return false;
    }
}
