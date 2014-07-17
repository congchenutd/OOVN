package com.fujitsu.us.oovn.element;

public interface Persistable
{
    /**
     * @return a Neo4j clause for matching the object
     */
    public String toDBMatch();
    
    /**
     * @return a Neo4j clause for creating the object without "CREATE"
     */
    public String toDBCreate();
    
    /**
     * @return a Neo4J clause for creating virtual->physical mapping
     */
    public String toDBMapping();
}
