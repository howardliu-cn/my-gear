package cn.howardliu.gear.es.profile;

import com.alibaba.fastjson.JSONObject;
import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.exists.AliasesExistResponse;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;

/**
 * <br/>created at 16-9-12
 *
 * @author liuxh
 * @since 1.0.0
 */
public class EsProfileManager {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private Client esClient;
    private EsProfileLoader esProfileLoader;

    public EsProfileManager(Client esClient, EsProfileLoader esProfileLoader) {
        this.esClient = Validate.notNull(esClient, "es客户端不能为空");
        this.esProfileLoader = Validate.notNull(esProfileLoader, "es的profile不能为空");
    }

    public boolean createIndex(String profileName, String indexName) throws Exception {
        if (StringUtils.isBlank(indexName)) {
            indexName = Validate.notNull(profileName, "profile不能为空");
        }
        JSONObject profile = esProfileLoader.getProfile(profileName);
        if (profile == null) {
            throw new IllegalArgumentException(String.format("[profile=%s]对应配置不存在", profileName));
        }
        CreateIndexRequestBuilder indexBuilder = this.esClient.admin().indices().prepareCreate(indexName);
        JSONObject settings = profile.getJSONObject("settings");
        if (settings != null) {
            indexBuilder.setSettings(settings);
        }
        JSONObject mappings = profile.getJSONObject("mappings");
        for (String typeName : mappings.keySet()) {
            JSONObject mapping = mappings.getJSONObject(typeName);
            indexBuilder.addMapping(typeName, mapping.toJSONString());
        }
        return indexBuilder.get().isAcknowledged();
    }

    public boolean switchAliases(String indexName, Collection<String> aliases) throws Exception {
        if (StringUtils.isBlank(indexName) || aliases == null || aliases.isEmpty()) {
            return false;
        }
        IndicesAliasesRequestBuilder aliasesBuilder = this.esClient.admin().indices().prepareAliases();
        for (String alias : aliases) {
            aliasesBuilder.addAlias(indexName, alias);
        }
        for (String alias : aliases) {
            AliasesExistResponse aliasesExistResponse = this.esClient.admin().indices().prepareAliasesExist(alias)
                    .get();
            if (aliasesExistResponse.exists()) {
                GetAliasesResponse getAliasesResponse = this.esClient.admin().indices().prepareGetAliases(alias).get();
                ImmutableOpenMap<String, List<AliasMetaData>> response = getAliasesResponse.getAliases();
                for (ObjectObjectCursor<String, List<AliasMetaData>> it : response) {
                    aliasesBuilder.removeAlias(it.key, alias);
                }
            }
        }
        return aliasesBuilder.get().isAcknowledged();
    }

    public boolean aliasAnIndexName(String indexName, Collection<String> aliases) throws Exception {
        if (StringUtils.isBlank(indexName) || aliases == null || aliases.isEmpty()) {
            return false;
        }
        IndicesAliasesRequestBuilder aliasBuilder = this.esClient.admin().indices().prepareAliases();
        for (String alias : aliases) {
            aliasBuilder.addAlias(indexName, alias);
        }
        return aliasBuilder.get().isAcknowledged();
    }

    public boolean removeAliases(String indexName, Collection<String> aliases) throws Exception {
        if (StringUtils.isBlank(indexName) || aliases == null || aliases.isEmpty()) {
            return false;
        }
        IndicesAliasesRequestBuilder aliasesBuilder = this.esClient.admin().indices().prepareAliases();
        for (String alias : aliases) {
            aliasesBuilder.removeAlias(indexName, alias);
        }
        return aliasesBuilder.get().isAcknowledged();
    }

    public boolean removeAliases(Collection<String> aliases) throws Exception {
        if (aliases == null || aliases.isEmpty()) {
            return true;
        }
        IndicesAliasesRequestBuilder aliasesBuilder = this.esClient.admin().indices().prepareAliases();
        for (String alias : aliases) {
            AliasesExistResponse aliasesExistResponse = this.esClient.admin().indices().prepareAliasesExist(alias)
                    .get();
            if (aliasesExistResponse.exists()) {
                GetAliasesResponse getAliasesResponse = this.esClient.admin().indices().prepareGetAliases(alias).get();
                ImmutableOpenMap<String, List<AliasMetaData>> response = getAliasesResponse.getAliases();
                for (ObjectObjectCursor<String, List<AliasMetaData>> it : response) {
                    aliasesBuilder.removeAlias(it.key, alias);
                }
            }
        }
        return aliasesBuilder.get().isAcknowledged();
    }
}
