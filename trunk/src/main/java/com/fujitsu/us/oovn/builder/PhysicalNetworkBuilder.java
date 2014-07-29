package com.fujitsu.us.oovn.builder;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.network.Network;
import com.fujitsu.us.oovn.exception.InvalidNetworkConfigurationException;
import com.fujitsu.us.oovn.factory.ElementFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PhysicalNetworkBuilder implements NetworkBuilder
{
    
    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void build(JsonObject json, Network network, VNO vno)
                                throws InvalidNetworkConfigurationException
    {
        JsonArray switchesJson = json.get("switches").getAsJsonArray();
        for (JsonElement e : switchesJson)
            network.addSwitch(ElementFactory.fromJson(
                    PhysicalSwitch.class, (JsonObject) e, null, null));

        JsonArray linksJson = json.get("links").getAsJsonArray();
        for (JsonElement e : linksJson)
            network.addLink(ElementFactory.fromJson(
                    PhysicalLink.class, (JsonObject) e, null, null));
    }
}
