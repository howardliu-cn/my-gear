package cn.howardliu.gear.es.storm;

import java.io.Serializable;

/**
 * <br/>created at 16-4-7
 *
 * @author liuxh
 * @since 1.1.18
 */
public class EsSource implements Serializable {
    private String indexName;
    private String typeName;
    private String id;
    private byte[] source;

    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte[] getSource() {
        return source;
    }

    public void setSource(byte[] source) {
        this.source = source;
    }
}
