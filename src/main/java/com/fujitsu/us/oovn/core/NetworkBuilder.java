package com.fujitsu.us.oovn.core;

import java.util.LinkedList;
import java.util.List;

import com.fujitsu.us.oovn.element.address.*;
import com.fujitsu.us.oovn.element.datapath.*;
import com.fujitsu.us.oovn.element.host.*;
import com.fujitsu.us.oovn.element.link.*;
import com.fujitsu.us.oovn.element.network.*;
import com.fujitsu.us.oovn.element.port.*;
import com.google.gson.*;

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
    public boolean build(VNO vno)
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
            VirtualSwitch sw = buildSwitch(swJson, vno);
            vnw.addSwitch(sw);
        }
        
        // links
        JsonArray linksJson = vnoJson.getAsJsonArray("links");
        for(JsonElement e: linksJson)
        {
            JsonObject linkJson = (JsonObject) e;
            VirtualLink link = buildLink(linkJson, vno);
            vnw.addLink(link);
        }
        
        // hosts
        JsonArray hostsJson = vnoJson.getAsJsonArray("hosts");
        for(JsonElement e: hostsJson)
        {
            JsonObject hostJson = (JsonObject) e;
            Host host = buildHost(hostJson, vno);
            vnw.addHost(host);
        }
        
        vno.setNetwork(vnw);
        return true;
    }

    /**
     * Build a SingleSwitch based on the given Json configuration
     * @param json the Json segment for the switch
     * @return a SingleSwitch object
     */
    private SingleSwitch buildSingleSwitch(JsonObject json, VNO vno)
    {
        DPID dpid   = new DPID(json.get("dpid").getAsString());
        String name = json.get("name").getAsString();
        SingleSwitch vsw = new SingleSwitch(vno, dpid, name);
        
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
            PhysicalPort port  = psw.getPort(number);
            
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
    private BigSwitch buildBigSwitch(JsonObject json, VNO vno)
    {
        return null;
    }
    
    /**
     * Build a VirtualSwitch (either SingleSwitch or BigSwitch)
     * based on the given Json configuration
     * @param json the Json segment for the switch
     * @return a VirtualSwitch object
     */
    private VirtualSwitch buildSwitch(JsonObject json, VNO vno) {
        return json.get("type").getAsString().equals("single") ? buildSingleSwitch(json, vno) 
                                                               : buildBigSwitch   (json, vno);
    }
    
    /**
     * Build a VirtualPort based on the given Json configuration
     * @param json the segment of the configuration for the port
     * @param vnw the VirtualNetwork of this port
     * @return a VirtualPort object
     */
    private VirtualPort buildVirtualPort(JsonObject json, VNO vno)
    {
        DPID dpid   = new DPID(json.get("switch").getAsString());
        int  number = json.get("number").getAsInt();
        return (VirtualPort) vno.getNetwork().getSwitch(dpid).getPort(number);
    }
    
    /**
     * Build a PhysicalPort based on the given Json configuration
     * @param json the segment of the configuration for the port
     * @return a VirtualPort object
     */
    private PhysicalPort buildPhysicalPort(JsonObject json)
    {
        DPID dpid   = new DPID(json.get("switch").getAsString());
        int  number = json.get("number").getAsInt();
        return (PhysicalPort) PhysicalNetwork.getInstance().getSwitch(dpid).getPort(number);
    }
    
    /**
     * Build a VirtualLink based on the given Json configuration
     * @param json the segment of the configuration for the VirtualLink
     * @param vnw the VirtualNetwork of this port
     * @return a VirtualLink object
     */
    private VirtualLink buildLink(JsonObject json, VNO vno)
    {
        if(json.isJsonNull())
            return null;
        
        JsonObject vSrcJson = json.get("src").getAsJsonObject();
        JsonObject vDstJson = json.get("dst").getAsJsonObject();
        
        VirtualLink result = new VirtualLink(vno,
                                             buildVirtualPort(vSrcJson, vno),
                                             buildVirtualPort(vDstJson, vno));
        
        JsonArray pathJson = json.get("path").getAsJsonArray();
        List<PhysicalLink> path = new LinkedList<PhysicalLink>();
        for(JsonElement e: pathJson)
        {
            JsonObject linkJson = (JsonObject) e;
            JsonObject pSrcJson = linkJson.get("src").getAsJsonObject();
            JsonObject pDstJson = linkJson.get("dst").getAsJsonObject();
            PhysicalLink pLink = new PhysicalLink(buildPhysicalPort(pSrcJson), 
                                                  buildPhysicalPort(pDstJson));
            path.add(pLink);
        }
        if(!path.isEmpty())
            result.setPath(path);
        
        return result;
    }
    
    /**
     * Build a Host based on the given Json configuration
     * @param json the segment of the configuration for the Host
     * @param vnw the VirtualNetwork of this port
     * @return a Host object
     */
    private Host buildHost(JsonObject json, VNO vno)
    {
        int              id   = json.get("id").getAsInt();
        String           name = json.get("name").getAsString();
        MACAddress       mac  = new MACAddress(json.get("mac").getAsString());
        VirtualIPAddress ip   = new VirtualIPAddress(vno,
                                                     json.get("ip").getAsString());
        Host host = new Host(id, name, mac, ip);
        
        JsonObject portJson = json.get("port").getAsJsonObject();
        host.setPort(buildVirtualPort(portJson, vno));
        
        return host;
    }
    
}
