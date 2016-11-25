package cn.howardliu.gear.es.profile.spring.service;

import java.util.Collection;

/**
 * <br/>created at 16-9-28
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface IndexManageService {
    boolean createIndex(String profile, String indexName) throws Exception;

    boolean aliasAnIndexName(String indexName, Collection<String> aliases) throws Exception;

    boolean switchAliases(String indexName, Collection<String> aliases) throws Exception;

    boolean removeAliases(String indexName, Collection<String> aliases) throws Exception;

    boolean removeAliases(Collection<String> aliases) throws Exception;
}
