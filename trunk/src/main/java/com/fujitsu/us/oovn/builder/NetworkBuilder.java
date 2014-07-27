package com.fujitsu.us.oovn.builder;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.link.Link;
import com.fujitsu.us.oovn.element.network.Network;
import com.fujitsu.us.oovn.exception.InvalidNetworkConfigurationException;
import com.fujitsu.us.oovn.factory.ElementFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Build a network from a Json configuration
 * 
 * Internally relies on ElementFactories to build/fetch network elements
 * The configuration file must define each element type, 
 * e.g., "type" = "SingleSwitch"
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class NetworkBuilder
{
    /**
     * Build a network based on a Json configuration
     * @param json      A JsonObject describing the network configuration
     * @param network   A network object to be built
     * @param vno       The VNO this network belongs to. Null for physical network
     * @throws InvalidNetworkConfigurationException
     */
    @SuppressWarnings("unchecked")
    protected void build(JsonObject json, Network network, VNO vno) 
                                    throws InvalidNetworkConfigurationException
    {
        JsonArray switchesJson = json.get("switches").getAsJsonArray();
        for(JsonElement e: switchesJson)
            network.addSwitch((Switch) ElementFactory.fromJson((JsonObject) e, vno));
        
        JsonArray linksJson = json.get("links").getAsJsonArray();
        for(JsonElement e: linksJson)
            network.addLink((Link) ElementFactory.fromJson((JsonObject) e, vno));
    }
}
