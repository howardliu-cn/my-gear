package cn.howardliu.gear.es.profile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * <br>created at 16-9-12
 *
 * @author liuxh
 * @since 1.0.0
 */
public class EsProfileLoader implements ResourceLoaderAware {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private ResourceLoader resourceLoader;
    private String filePath;
    private final Map<String, JSONObject> profileMap = Collections.synchronizedMap(new HashMap<String, JSONObject>());

    public EsProfileLoader(String filePath) {
        this.filePath = filePath;
    }

    @PostConstruct
    public void loadEsProfile() throws IOException {
        profileMap.clear();
        InputStream inputStream = this.resourceLoader.getResource(filePath).getInputStream();
        JSONArray esProfileJson = JSON.parseArray(IOUtils.toString(inputStream, Charsets.UTF_8));
        if (esProfileJson == null) {
            logger.warn("got empty es profile, the file path is {}.", filePath);
            return;
        }
        for (int i = 0; i < esProfileJson.size(); i++) {
            JSONObject json = esProfileJson.getJSONObject(i);
            String indexName = json.getString("indexName");
            JSONObject profileJson = new JSONObject();
            profileMap.put(indexName, profileJson);

            String settingsFilePath = json.getString("settingsFile");
            if (StringUtils.isNotBlank(settingsFilePath)) {
                InputStream settingsFileIn = this.resourceLoader.getResource(settingsFilePath).getInputStream();
                JSONObject settingsFile = JSON.parseObject(IOUtils.toString(settingsFileIn, Charsets.UTF_8));
                profileJson.put("settings", settingsFile);
            }

            JSONArray mappingFiles = json.getJSONArray("mappingFiles");
            JSONObject mappings = new JSONObject();
            profileJson.put("mappings", mappings);
            for (int j = 0; j < mappingFiles.size(); j++) {
                JSONObject mappingFile = mappingFiles.getJSONObject(j);
                String typeName = mappingFile.getString("typeName");
                String mappingFilePath = mappingFile.getString("mappingFile");
                InputStream mappingFileIn = this.resourceLoader.getResource(mappingFilePath).getInputStream();
                JSONObject mapping = JSON.parseObject(IOUtils.toString(mappingFileIn, Charsets.UTF_8));
                mappings.put(typeName, mapping);
            }
        }
    }

    public Map<String, JSONObject> getProfiles() {
        return profileMap;
    }

    public JSONObject getProfile(String indexName) {
        return profileMap.get(indexName);
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
