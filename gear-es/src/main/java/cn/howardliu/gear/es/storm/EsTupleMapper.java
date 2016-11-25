package cn.howardliu.gear.es.storm;

import backtype.storm.tuple.ITuple;

import java.io.Serializable;

/**
 * TupleMapper defines how to extract source, index, type, and id from tuple for ElasticSearch.
 * <br/>create at 16-3-8
 *
 * @author liuxh
 * @since 1.0.0
 */
public interface EsTupleMapper extends Serializable {
    /**
     * Extracts source from tuple.
     *
     * @param tuple source tuple
     * @return source
     */
    String getSource(ITuple tuple);

    /**
     * Extracts index from tuple.
     *
     * @param tuple source tuple
     * @return index
     */
    String getIndex(ITuple tuple);

    /**
     * Extracts type from tuple.
     *
     * @param tuple source tuple
     * @return type
     */
    String getType(ITuple tuple);

    /**
     * Extracts id from tuple.
     *
     * @param tuple source tuple
     * @return id
     */
    String getId(ITuple tuple);
}
