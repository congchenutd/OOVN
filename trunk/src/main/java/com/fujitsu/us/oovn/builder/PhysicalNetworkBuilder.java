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

public class PhysicalNetworkBuilder implements NetworkBuilder
{
    
    @SuppressWarnings("unchecked")
    public void build(JsonObject json, Network network, VNO vno)
                                throws InvalidNetworkConfigurationException
    {
        JsonArray switchesJson = json.get("switches").getAsJsonArray();
        for (JsonElement e : switchesJson)
            network.addSwitch((Switch) ElementFactory.fromJson(
                    "PhysicalSwitch", (JsonObject) e, null, vno));

        JsonArray linksJson = json.get("links").getAsJsonArray();
        for (JsonElement e : linksJson)
            network.addLink((Link) ElementFactory.fromJson(
                    "PhysicalLink", (JsonObject) e, null, vno));
    }
}
