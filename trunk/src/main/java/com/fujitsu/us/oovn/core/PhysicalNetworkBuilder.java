package com.fujitsu.us.oovn.core;

import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.factory.ElementFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PhysicalNetworkBuilder
{
    public void build(JsonObject json, PhysicalNetwork network)
    {
        JsonArray switchesJson = json.get("switches").getAsJsonArray();
        for(JsonElement e: switchesJson)
        {
            JsonObject switchJson = (JsonObject) e;
            PhysicalSwitch sw = (PhysicalSwitch) ElementFactory.fromJson(switchJson, null);
            network.addSwitch(sw);
        }
        
        JsonArray linksJson = json.get("links").getAsJsonArray();
        for(JsonElement e: linksJson)
        {
            JsonObject linkJson = (JsonObject) e;
            PhysicalSwitch sw = (PhysicalSwitch) ElementFactory.fromJson(linkJson, null);
            network.addSwitch(sw);
        }
    }
    
}
