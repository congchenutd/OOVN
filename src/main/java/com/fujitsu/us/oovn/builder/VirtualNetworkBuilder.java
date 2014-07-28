package com.fujitsu.us.oovn.builder;


import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.*;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.host.*;
import com.fujitsu.us.oovn.element.link.Link;
import com.fujitsu.us.oovn.element.link.VirtualLink;
import com.fujitsu.us.oovn.element.network.*;
import com.fujitsu.us.oovn.element.port.*;
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
    public void build(VNO vno) throws InvalidNetworkConfigurationException {
        build(vno.getConfiguration().toJson(), vno.getNetwork(), vno);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void build(JsonObject json, Network network, VNO vno) 
                                throws InvalidNetworkConfigurationException
    {
        JsonObject vnoJson = json.getAsJsonObject("vno");
        
        // global
        String address = vnoJson.get("address").getAsString();
        int    mask    = vnoJson.get("mask")   .getAsInt();
        VirtualNetwork vnw = (VirtualNetwork) network;
        vnw.setIP(new IPAddress(address));
        vnw.setMask(mask);
        
        // network
        JsonArray switchesJson = vnoJson.get("switches").getAsJsonArray();
        for (JsonElement e : switchesJson)
            network.addSwitch((Switch) ElementFactory.fromJson(
                    null, (JsonObject) e, null, vno));

        JsonArray linksJson = vnoJson.get("links").getAsJsonArray();
        for (JsonElement e : linksJson)
        {
            VirtualLink link = (VirtualLink) ElementFactory.fromJson(
                    "VirtualLink", (JsonObject) e, null, vno);
            network.addLink(link);
        }
        
        // hosts
        JsonArray hostsJson = vnoJson.getAsJsonArray("hosts");
        for(JsonElement e: hostsJson)
            vnw.addHost(buildHost((JsonObject) e, vno));
    }
    
    /**
     * Build a Host based on the given JsonObject
     * @param json  the segment of the configuration for the Host
     * @param vno   the VNO of this port
     * @return      a Host object
     */
    private Host buildHost(JsonObject json, VNO vno) 
                            throws InvalidNetworkConfigurationException 
    {
        int              id   = json.get("id").getAsInt();
        String           name = json.get("name").getAsString();
        MACAddress       mac  = new MACAddress(json.get("mac").getAsString());
        VirtualIPAddress ip   = new VirtualIPAddress(vno,
                                                     json.get("ip").getAsString());
        Host host = new Host(id, name, mac, ip);
        
        JsonObject portJson = json.get("port").getAsJsonObject();
        host.setPort((VirtualPort) ElementFactory.fromJson("VirtualPort", portJson, null, vno));
        
        return host;
    }
    
}
