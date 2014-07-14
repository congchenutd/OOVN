package com.fujitsu.us.oovn.core;

import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.IPAddress;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;
import com.fujitsu.us.oovn.element.datapath.BigSwitch;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.SingleSwitch;
import com.fujitsu.us.oovn.element.datapath.VirtualSwitch;
import com.fujitsu.us.oovn.element.host.Host;
import com.fujitsu.us.oovn.element.link.Link;
import com.fujitsu.us.oovn.element.link.VirtualLink;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.network.VirtualNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Build a VirtualNetwork for a VNO, according to its configuration
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class NetworkBuilder
{
    // singleton
    public static NetworkBuilder getInstance() {
        return LazyHolder._instance;
    }
    
    private static class LazyHolder {
        private static final NetworkBuilder _instance = new NetworkBuilder();
    }
    
    private NetworkBuilder() {}

    /**
     * Build a VirtualNetwork for the given VNO
     * Traverse the configuration Json structure and add corresponding elements
     * @param vno the VNO the virtual network is built for
     * @return a new virtual network object for the VNO 
     */
    public VirtualNetwork build(VNO vno)
    {
        JsonObject json    = vno.getConfiguration().toJson();
        JsonObject vnoJson = json.getAsJsonObject("vno");
        
        // global
        String address = vnoJson.get("address").getAsString();
        int    mask    = vnoJson.get("mask")   .getAsInt();
        VirtualNetwork vnw = new VirtualNetwork(vno, new IPAddress(address), mask);
        
        // switches
        JsonArray switchesJson = vnoJson.getAsJsonArray("switches");
        for(JsonElement e: switchesJson)
        {
            JsonObject swJson = (JsonObject) e;
            VirtualSwitch sw = buildSwitch(swJson);
            vnw.addSwitch(sw);
        }
        
        // links
        JsonArray linksJson = vnoJson.getAsJsonArray("links");
        for(JsonElement e: linksJson)
        {
            JsonObject linkJson = (JsonObject) e;
            Link link = buildLink(linkJson, vnw);
            vnw.addLink(link);
        }
        
        // hosts
        JsonArray hostsJson = vnoJson.getAsJsonArray("hosts");
        for(JsonElement e: hostsJson)
        {
            JsonObject hostJson = (JsonObject) e;
            Host host = buildHost(hostJson, vnw);
            vnw.addHost(host);
        }
        
        return vnw;
    }

    /**
     * Build a SingleSwitch based on the given Json configuration
     * @param json the Json segment for the switch
     * @return a SingleSwitch object
     */
    private SingleSwitch buildSingleSwitch(JsonObject json)
    {
        DPID dpid   = new DPID(json.get("dpid").getAsString());
        String name = json.get("name").getAsString();
        SingleSwitch vsw = new SingleSwitch(dpid, name);
        
        // map to physical switch
        DPID phyID = new DPID(json.get("physical").getAsString());
        PhysicalSwitch psw = (PhysicalSwitch) PhysicalNetwork.getInstance().getSwitch(phyID);
        vsw.setPhysicalSwitch(psw);
        
        // virtual ports
        JsonArray portsJson = json.get("ports").getAsJsonArray();
        
        int index = 0;
        for(JsonElement e: portsJson)
        {
            int number = e.getAsInt();
            PhysicalPort port  = (PhysicalPort) psw.getPort(number);
            
            // QUESTION: is it OK to reuse physical MAC address? OVX does so.
            VirtualPort  vPort = new VirtualPort(++index, port.getMACAddress());
            vPort.setPhysicalPort(port);
            vsw.addPort(vPort);
        }
        
        return vsw;
    }
    
    /**
     * Build a BigSwitch based on the given Json configuration
     * @param json the Json segment for the switch
     * @return a BigSwitch object
     */
    private BigSwitch buildBigSwitch(JsonObject json)
    {
        return null;
    }
    
    /**
     * Build a VirtualSwitch (either SingleSwitch or BigSwitch)
     * based on the given Json configuration
     * @param json the Json segment for the switch
     * @return a VirtualSwitch object
     */
    private VirtualSwitch buildSwitch(JsonObject json) {
        return json.get("type").getAsString().equals("single") ? buildSingleSwitch(json) 
                                                               : buildBigSwitch(json);
    }
    
    /**
     * Build a VirtualPort based on the given Json configuration
     * @param json the segment of the configuration for the port
     * @param vnw the VirtualNetwork of this port
     * @return a VirtualPort object
     */
    private VirtualPort buildPort(JsonObject json, VirtualNetwork vnw)
    {
        DPID dpid   = new DPID(json.get("switch").getAsString());
        int  number = json.get("number").getAsInt();
        return (VirtualPort) vnw.getSwitch(dpid).getPort(number);
    }
    
    /**
     * Build a VirtualLink based on the given Json configuration
     * @param json the segment of the configuration for the VirtualLink
     * @param vnw the VirtualNetwork of this port
     * @return a VirtualLink object
     */
    private VirtualLink buildLink(JsonObject json, VirtualNetwork vnw)
    {
        if(json.isJsonNull())
            return null;
        
        JsonObject srcJson = json.get("src").getAsJsonObject();
        JsonObject dstJson = json.get("dst").getAsJsonObject();
        return new VirtualLink(buildPort(srcJson, vnw), buildPort(dstJson, vnw));
    }
    
    /**
     * Build a Host based on the given Json configuration
     * @param json the segment of the configuration for the Host
     * @param vnw the VirtualNetwork of this port
     * @return a Host object
     */
    private Host buildHost(JsonObject json, VirtualNetwork vnw)
    {
        int              id   = json.get("id").getAsInt();
        String           name = json.get("name").getAsString();
        MACAddress       mac  = new MACAddress(json.get("mac").getAsString());
        VirtualIPAddress ip   = new VirtualIPAddress(vnw.getVNO(),
                                                     json.get("ip").getAsString());
        Host host = new Host(id, name, mac, ip);
        
        JsonObject portJson = json.get("port").getAsJsonObject();
        host.setPort(buildPort(portJson, vnw));
        
        return host;
    }
    
}
