package com.fujitsu.us.oovn.factory;

import java.util.HashMap;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.NetworkElement;
import com.fujitsu.us.oovn.exception.InvalidNetworkConfigurationException;
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
     * Type.class -> factory
     */
    protected static final HashMap<Class<? extends NetworkElement>, ElementFactory> 
          _factories = new HashMap<Class<? extends NetworkElement>, ElementFactory>();
    
    /**
     * Register a concrete factory for a concrete network element
     * @param factory   A concrete factory for the network element type
     */
    public static void registerFactory(ElementFactory factory) {
        _factories.put(factory.getProductType(), factory);
    }
    
    /**
     * Register all concrete factories here
     */
    static 
    {
        ElementFactory.registerFactory(new PhysicalSwitchFactory());
        ElementFactory.registerFactory(new PhysicalLinkFactory());
        ElementFactory.registerFactory(new PhysicalPortFactory());
        ElementFactory.registerFactory(new SingleSwitchFactory());
        ElementFactory.registerFactory(new VirtualLinkFactory());
        ElementFactory.registerFactory(new VirtualPortFactory());
    }
    
    /**
     * Create a new or fetch an existing network element based on a Neo4j node
     * Find a concrete factory based on the "type" property of the node,
     * and use the factory to build the element
     * 
     * @param node  A Neo4j node describing the element
     * @param vno   The VNO the element belongs to. Null if physical.
     * @return      A network element object
     */
    public static NetworkElement fromNode(Node node, VNO vno)
    {
        Class<?> type;
        try {
            type = Class.forName(node.getProperty("type").toString());
            return _factories.containsKey(type)
                    ? _factories.get(type).create(node, vno)
                    : null;
        } catch (ClassNotFoundException e) {
            return null;
        }
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
     * @param json  A json object describing the element
     * @param vno   The VNO the element belongs to. Null if physical.
     * @return      A network element object
     * @throws      InvalidNetworkConfigurationException
     */
    public static NetworkElement fromJson(JsonObject json, JsonObject parentJson, VNO vno) 
                                    throws InvalidNetworkConfigurationException
    {
        if(!json.has("type"))
            throw new InvalidNetworkConfigurationException(
                                    "No type for json object: " + json);
        
        Class<?> type;
        try {
            type = Class.forName(json.get("type").getAsString());
        } catch (ClassNotFoundException e) {
            throw new InvalidNetworkConfigurationException(
                    "The type is not registered. Json: " + json);
        }
        
        if(!_factories.containsKey(type))
            throw new InvalidNetworkConfigurationException(
                    "The type is not registered. Json: " + json);
        
        return _factories.get(type).create(json, parentJson, vno);
    }
    
    /**
     * Create a new or fetch an existing network element based on given JsonObject
     * Find a concrete factory based on the type given by caller,
     * and use the factory to build the element
     * 
     * @param type  A XX.class
     * @param json  A json object describing the element
     * @param vno   The VNO the element belongs to. Null if physical.
     * @return      A network element object
     * @throws      InvalidNetworkConfigurationException
     */
    public static <T extends NetworkElement> T fromJson(
            Class<T> type, JsonObject json, JsonObject parentJson, VNO vno) 
                    throws InvalidNetworkConfigurationException
    {
        if(!_factories.containsKey(type))
            throw new InvalidNetworkConfigurationException(
                    "The type is not registered. Json: " + json);
        
        return (T) _factories.get(type).create(json, parentJson, vno);
    }
    
    /**
     * Fetch an EXISTING network element based on a Neo4j node
     * @param node  A Neo4j node representing the element
     * @param vno   The VNO the element belongs to. Null if physical
     * @return      A network element object
     */
    protected abstract NetworkElement create(Node node, VNO vno);
    
    /**
     * Create a new or fetch an existing network element based on given JsonObject
     * @param json  A json object describing the element
     * @param vno   The VNO the element belongs to. Null if physical.
     * @return      A network element object
     * @throws      InvalidNetworkConfigurationException
     */
    protected abstract NetworkElement create(JsonObject json, JsonObject parentJson, VNO vno) 
                                    throws InvalidNetworkConfigurationException;
    
    /**
     * @return  the Class of the product
     */
    protected abstract Class<? extends NetworkElement> getProductType();

}
