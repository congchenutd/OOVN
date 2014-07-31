package com.fujitsu.us.oovn.builder;


import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.*;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.host.*;
import com.fujitsu.us.oovn.element.link.VirtualLink;
import com.fujitsu.us.oovn.element.network.*;
import com.fujitsu.us.oovn.exception.*;
import com.fujitsu.us.oovn.factory.ElementFactory;
import com.google.gson.*;

/**
 * Build a VirtualNetwork for a VNO according to its configuration
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class VirtualNetworkBuilder implements NetworkBuilder
{
    /**
     * For convenience
     */
    public void build(VNO vno) throws InvalidConfigurationException {
        build(vno.getConfiguration().toJson(), vno.getNetwork(), vno);
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void build(JsonObject json, Network network, VNO vno) 
                                throws InvalidConfigurationException
    {
        if(!json.has("vno"))
            throw new InvalidConfigurationException("No vno defined");
        JsonObject vnoJson = json.getAsJsonObject("vno");
        
        // global
        VirtualNetwork vnw = (VirtualNetwork) network;
        if(!vnoJson.has("address"))
            throw new InvalidConfigurationException("No address defined");
        String address = vnoJson.get("address").getAsString();
        vnw.setIP(new IPAddress(address));
        
        if(!vnoJson.has("mask"))
            throw new InvalidConfigurationException("No mask defined");
        int mask = vnoJson.get("mask").getAsInt();
        vnw.setMask(mask);
        
        // switches
        if(!vnoJson.has("switches"))
            throw new InvalidConfigurationException("No switches defined");
        
        JsonArray switchesJson = vnoJson.get("switches").getAsJsonArray();
        for (JsonElement e : switchesJson)
            // switch type is unknown here, load from json
            network.addSwitch((Switch) ElementFactory.fromJson(
                    null, (JsonObject) e, null, vno));

        // links
        if(vnoJson.has("links"))
        {
            JsonArray linksJson = vnoJson.get("links").getAsJsonArray();
            for (JsonElement e : linksJson)
            {
                VirtualLink link = (VirtualLink) ElementFactory.fromJson(
                        "VirtualLink", (JsonObject) e, null, vno);
                network.addLink(link);
            }
        }
        
        // hosts
        if(vnoJson.has("hosts"))
        {
            JsonArray hostsJson = vnoJson.getAsJsonArray("hosts");
            for(JsonElement e: hostsJson)
                vnw.addHost((Host) ElementFactory.fromJson(
                                        "Host", (JsonObject) e, null, vno));
        }
    }
    
}
