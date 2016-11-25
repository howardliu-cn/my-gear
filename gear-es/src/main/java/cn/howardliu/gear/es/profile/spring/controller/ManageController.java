package cn.howardliu.gear.es.profile.spring.controller;

import cn.howardliu.gear.es.profile.spring.service.IndexManageService;
import cn.howardliu.gear.springEx.ServiceRegister;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * <br/>created at 16-9-28
 *
 * @author liuxh
 * @since 1.0.0
 */
@RestController("indexManageController")
@RequestMapping("manage")
public class ManageController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private IndexManageService indexManageService;

    @RequestMapping("createIndex")
    @ServiceRegister("create-index")
    public JSONObject createIndex(@RequestParam String profile, String index) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtils.isBlank(profile)) {
                throw new IllegalArgumentException("环境配置名不能为空");
            }
            result.put("acknowledged", indexManageService.createIndex(profile, index));
            result.put("success", true);
        } catch (IllegalArgumentException e) {
            logger.error("创建{}索引失败", index, e);
            result.put("success", false);
            result.put("errCode", 403);
            result.put("errMsg", e.getMessage());
        } catch (Exception e) {
            logger.error("创建{}索引失败", index, e);
            result.put("success", false);
            result.put("errCode", 500);
            result.put("errMsg", e.getMessage());
        } finally {
            JSONObject params = new JSONObject();
            params.put("profile", profile);
            params.put("index", index);
            result.put("params", params);
        }
        return result;
    }

    @RequestMapping("addAliases")
    @ServiceRegister("add-aliases")
    public JSONObject addAliases(@RequestParam String index, @RequestParam Set<String> aliases) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtils.isBlank(index) || aliases == null || aliases.isEmpty()) {
                throw new IllegalArgumentException("索引名或索引别名不能为空");
            }
            result.put("acknowledged", this.indexManageService.aliasAnIndexName(index, aliases));
            result.put("success", true);
        } catch (IllegalArgumentException e) {
            logger.error("为索引index={}创建别名aliases={}失败", index, aliases, e);
            result.put("success", false);
            result.put("errCode", 403);
            result.put("errMsg", e.getMessage());
        } catch (Exception e) {
            logger.error("为索引index={}创建别名aliases={}失败", index, aliases, e);
            result.put("success", false);
            result.put("errCode", 500);
            result.put("errMsg", e.getMessage());
        } finally {
            JSONObject params = new JSONObject();
            params.put("index", index);
            params.put("aliases", aliases);
            result.put("params", params);
        }
        return result;
    }

    @RequestMapping("switchAliases")
    @ServiceRegister("switch-aliases")
    public JSONObject switchAliases(@RequestParam String index, @RequestParam Set<String> aliases) {
        JSONObject result = new JSONObject();
        try {
            if (StringUtils.isBlank(index) || aliases == null || aliases.isEmpty()) {
                throw new IllegalArgumentException("索引名或索引别名不能为空");
            }
            result.put("acknowledged", this.indexManageService.switchAliases(index, aliases));
            result.put("success", true);
        } catch (IllegalArgumentException e) {
            logger.error("为索引index={}创建唯一别名aliases={}失败", index, aliases, e);
            result.put("success", false);
            result.put("errCode", 403);
            result.put("errMsg", e.getMessage());
        } catch (Exception e) {
            logger.error("为索引index={}创建唯一别名aliases={}失败", index, aliases, e);
            result.put("success", false);
            result.put("errCode", 500);
            result.put("errMsg", e.getMessage());
        } finally {
            JSONObject params = new JSONObject();
            params.put("index", index);
            params.put("aliases", aliases);
            result.put("params", params);
        }
        return result;
    }

    @RequestMapping("removeAliases")
    @ServiceRegister("remove-aliases")
    public JSONObject removeAliases(String index, @RequestParam Set<String> aliases) {
        JSONObject result = new JSONObject();
        try {
            if (aliases == null || aliases.isEmpty()) {
                throw new IllegalArgumentException("索引别名不能为空");
            }
            boolean isAcknowledged;
            if (StringUtils.isBlank(index)) {
                isAcknowledged = this.indexManageService.removeAliases(aliases);
            } else {
                isAcknowledged = this.indexManageService.removeAliases(index, aliases);
            }
            result.put("acknowledged", isAcknowledged);
            result.put("success", true);
        } catch (IllegalArgumentException e) {
            logger.error("别名aliases={}失败，索引名index={}", aliases, index, e);
            result.put("success", false);
            result.put("errCode", 403);
            result.put("errMsg", e.getMessage());
        } catch (Exception e) {
            logger.error("别名aliases={}失败，索引名index={}", aliases, index, e);
            result.put("success", false);
            result.put("errCode", 500);
            result.put("errMsg", e.getMessage());
        } finally {
            JSONObject params = new JSONObject();
            params.put("aliases", aliases);
            params.put("index", index);
            result.put("params", params);
        }
        return result;
    }
}
