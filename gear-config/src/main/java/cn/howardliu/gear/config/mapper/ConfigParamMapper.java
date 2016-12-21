package cn.howardliu.gear.config.mapper;

import cn.howardliu.gear.config.pojo.ConfigParam;
import org.apache.ibatis.annotations.Param;

/**
 * <br/>created at 16-5-9
 *
 * @author liuxh
 * @since 1.0.1
 */
public interface ConfigParamMapper {
    ConfigParam getParam(@Param("name") String name);

    void setParam(ConfigParam configParam);
}
