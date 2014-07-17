package com.fujitsu.us.oovn.element;

import org.neo4j.cypher.javacompat.ExecutionEngine;

public interface Persistable
{
    /**
     * @return a variable name for Neo4J query, without ()
     */
    public String toDBVariable();
    
    /**
     * @return a Neo4j clause for matching the node, including ()
     * Should include all the information of the node so that it can be used for creating the node
     */
    public String toDBMatch();
    
    /**
     * Create the node(s) (network, switch, link, port) in the db
     * @param engine execution engine of Neo4j
     */
    public void create(ExecutionEngine engine);
    
    /**
     * Create the mapping to the physical node(s) in the db
     * @param engine execution engine of Neo4j
     */
    public void createMapping(ExecutionEngine engine);
}
