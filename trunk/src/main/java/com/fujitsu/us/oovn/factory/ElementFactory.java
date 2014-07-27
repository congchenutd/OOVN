package com.fujitsu.us.oovn.factory;

import java.util.HashMap;
import java.util.Map;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
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
     * Type name -> factory
     */
    private static final Map<String, ElementFactory> _factories 
                                = new HashMap<String, ElementFactory>();
    
    /**
     * Register a concrete factory for a concrete network element
     * @param type      Name of a concrete network element class
     * @param factory   A concrete factory for the network element type
     */
    public static void registerElement(String type, ElementFactory factory) {
        _factories.put(type, factory);
    }
    
    /**
     * Register all concrete factories here
     */
    static 
    {
        ElementFactory.registerElement("PhysicalSwitch", new PhysicalSwitchFactory());
        ElementFactory.registerElement("PhysicalLink",   new PhysicalLinkFactory());
        ElementFactory.registerElement("PhysicalPort",   new PhysicalPortFactory());
        ElementFactory.registerElement("SingleSwitch",   new SingleSwitchFactory());
        ElementFactory.registerElement("VirtualLink",    new VirtualLinkFactory());
        ElementFactory.registerElement("VirtualPort",    new VirtualPortFactory());
    }
    
    /**
     * Create a new or fetch an existing network element based on a Neo4j node
     * @param node  A Neo4j node describing the element
     * @param vno   The VNO the element belongs to. Null if physical.
     * @return      A network element object
     */
    public static Persistable fromNode(Node node, VNO vno)
    {
        String type = node.getProperty("type").toString();
        return _factories.containsKey(type) ? _factories.get(type).create(node, vno)
                                            : null;
    }
    
    /**
     * Convenient for physical elements
     */
    public static Persistable fromNode(Node node) {
        return fromNode(node, null);
    }
    
    /**
     * Create a new or fetch an existing network element based on given JsonObject
     * @param json  A json object describing the element
     * @param vno   The VNO the element belongs to. Null if physical.
     * @return      A network element object
     * @throws      InvalidNetworkConfigurationException
     */
    public static Persistable fromJson(JsonObject json, VNO vno) 
                                    throws InvalidNetworkConfigurationException
    {
        if(!json.has("type"))
            throw new InvalidNetworkConfigurationException(
                                    "No type for json object: " + json);
        
        String type = json.get("type").getAsString();
        return _factories.containsKey(type) ? _factories.get(type).create(json, vno)
                                            : null;
    }
    
    /**
     * Convenient for physical elements
     */
    public static Persistable fromJson(JsonObject json) 
                                    throws InvalidNetworkConfigurationException {
        return fromJson(json, null);
    }
    
    /**
     * Fetch an existing network element based on a Neo4j node
     * @param node  A Neo4j node representing the element
     * @param vno   The VNO the element belongs to. Null if physical
     * @return      A network element object
     */
    protected abstract Persistable create(Node node, VNO vno);
    
    /**
     * Create a new or fetch an existing network element based on given JsonObject
     * @param json  A json object describing the element
     * @param vno   The VNO the element belongs to. Null if physical.
     * @return      A network element object
     * @throws      InvalidNetworkConfigurationException
     */
    protected abstract Persistable create(JsonObject json, VNO vno) 
                                    throws InvalidNetworkConfigurationException;

}
