package com.fujitsu.us.oovn.factory;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.NetworkElement;
import com.fujitsu.us.oovn.exception.InvalidConfigurationException;
import com.google.gson.JsonObject;

/**
 * The base and entry class for network element factories
 * Each concrete factory creates a new OR fetches an existing network element
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public abstract class ElementFactory
{
    /**
     * Type name -> factory
     */
    private static final Map<String, ElementFactory> _factories 
                                = new HashMap<String, ElementFactory>();
    
    /**
     * Register a concrete factory for a concrete network element
     * @param factory   A concrete factory for the network element type
     */
    public static void registerElement(ElementFactory factory) {
        _factories.put(factory.getTypeName(), factory);
    }
    
    /**
     * Register all concrete factories here
     */
    static 
    {
        ElementFactory.registerElement(new PhysicalSwitchFactory());
        ElementFactory.registerElement(new PhysicalLinkFactory());
        ElementFactory.registerElement(new PhysicalPortFactory());
        ElementFactory.registerElement(new SingleSwitchFactory());
        ElementFactory.registerElement(new VirtualLinkFactory());
        ElementFactory.registerElement(new VirtualPortFactory());
    }
    
    /**
     * Fetch an existing network element based on a Neo4j node
     * Find a concrete factory based on the "type" property of the node,
     * and use the factory to build the element
     * 
     * @param node  A Neo4j node describing the element
     * @param vno   The VNO the element belongs to. Null if physical.
     * @return      A network element object
     */
    public static NetworkElement fromNode(Node node, VNO vno)
    {
        String type = node.getProperty("type").toString();
        return _factories.containsKey(type) ? _factories.get(type).create(node, vno)
                                            : null;
    }
    
    /**
     * Convenient for physical elements
     */
    public static NetworkElement fromNode(Node node) {
        return fromNode(node, null);
    }

    /**
     * Create a new or fetch an existing network element based on given JsonObject
     * Find a concrete factory based on the "type" property of the json obj,
     * and use the factory to build the element
     * 
     * 
     * @param json  A json object describing the element
     * @param parentJson    The json object for the parent element (e.g., switch of a port) 
     * @param vno   The VNO the element belongs to. Null if physical.
     * @return      A network element object
     * @throws      InvalidConfigurationException
     */
    public static NetworkElement fromJson(String type, JsonObject json,
                                          JsonObject parentJson, VNO vno)
                                        throws InvalidConfigurationException
    {
        // if the type is unknown, try to find it in the json object
        if(type == null || type.isEmpty())
        {
            if(!json.has("type"))
                throw new InvalidConfigurationException(
                                        "No type defined. Json: " + json);
            type = json.get("type").getAsString();
        }
        
        if(!_factories.containsKey(type))
            throw new InvalidConfigurationException(
                    "Type not registered. Json: " + json);
        
        return _factories.get(type).create(json, parentJson, vno); 
    }
    
    /**
     * Convenient for physical elements
     */
    public static NetworkElement fromJson(String type, JsonObject json,
                                          JsonObject parentJson)
                                    throws InvalidConfigurationException {
        return fromJson(type, json, parentJson, null);
    }
    
    /**
     * Fetch an existing network element based on a Neo4j node
     * @param node  A Neo4j node representing the element
     * @param vno   The VNO the element belongs to. Null if physical
     * @return      A network element object
     */
    protected abstract NetworkElement create(Node node, VNO vno);
    
    /**
     * Create a new or fetch an existing network element based on given JsonObject
     * @param json  A json object describing the element
     * @param parentJson    The json object for the parent element (e.g., switch of a port) 
     * @param vno   The VNO the element belongs to. Null if physical.
     * @return      A network element object
     * @throws      InvalidConfigurationException
     */
    protected abstract NetworkElement create(JsonObject json, JsonObject parentJson, VNO vno) 
                                    throws InvalidConfigurationException;

    /**
     * @return product class name
     */
    protected abstract String getTypeName();
}
