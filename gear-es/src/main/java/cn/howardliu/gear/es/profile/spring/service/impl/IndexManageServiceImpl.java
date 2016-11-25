package cn.howardliu.gear.es.profile.spring.service.impl;

import cn.howardliu.gear.es.profile.EsProfileManager;
import cn.howardliu.gear.es.profile.spring.service.IndexManageService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * <br/>created at 16-9-28
 *
 * @author liuxh
 * @since 1.0.0
 */
@Service("indexManageService")
public class IndexManageServiceImpl implements IndexManageService {
    private static final Logger logger = LoggerFactory.getLogger(IndexManageServiceImpl.class);
    @Autowired
    private EsProfileManager esProfileManager;

    @Override
    public boolean createIndex(String profile, String indexName) throws Exception {
        return StringUtils.isNotBlank(profile) && this.esProfileManager.createIndex(profile.trim(), indexName);
    }

    @Override
    public boolean aliasAnIndexName(String indexName, Collection<String> aliases) throws Exception {
        return StringUtils.isNotBlank(indexName) && aliases != null && !aliases.isEmpty()
                && this.esProfileManager.aliasAnIndexName(indexName, aliases);
    }

    @Override
    public boolean switchAliases(String indexName, Collection<String> aliases) throws Exception {
        return StringUtils.isNotBlank(indexName) && aliases != null && !aliases.isEmpty()
                && this.esProfileManager.switchAliases(indexName, aliases);
    }

    @Override
    public boolean removeAliases(String indexName, Collection<String> aliases) throws Exception {
        return StringUtils.isNotBlank(indexName) && aliases != null && !aliases.isEmpty()
                && this.esProfileManager.removeAliases(indexName, aliases);
    }

    @Override
    public boolean removeAliases(Collection<String> aliases) throws Exception {
        return aliases != null && !aliases.isEmpty() && this.esProfileManager.removeAliases(aliases);
    }
}
