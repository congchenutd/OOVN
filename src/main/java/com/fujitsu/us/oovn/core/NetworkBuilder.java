package com.fujitsu.us.oovn.core;


import java.util.LinkedList;
import java.util.List;

import com.fujitsu.us.oovn.element.address.*;
import com.fujitsu.us.oovn.element.datapath.*;
import com.fujitsu.us.oovn.element.host.*;
import com.fujitsu.us.oovn.element.link.*;
import com.fujitsu.us.oovn.element.network.*;
import com.fujitsu.us.oovn.element.port.*;
import com.fujitsu.us.oovn.exception.*;
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
    public boolean build(VNO vno) throws InvalidVNOConfigurationException
    {
        JsonObject json    = vno.getConfiguration().toJson();
        JsonObject vnoJson = json.getAsJsonObject("vno");
        if(vnoJson.isJsonNull())
            throw new InvalidVNOConfigurationException("No vno object in the json file");
        
        // global
        String address = vnoJson.get("address").getAsString();
        int    mask    = vnoJson.get("mask")   .getAsInt();
        VirtualNetwork vnw = new VirtualNetwork(vno, new IPAddress(address), mask);
        vno.setNetwork(vnw);
        
        // switches
        JsonArray switchesJson = vnoJson.getAsJsonArray("switches");
        if(switchesJson.isJsonNull())
            throw new InvalidVNOConfigurationException("No switches defined");
        
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
        
        return true;
    }

    /**
     * Build a SingleSwitch based on the given Json configuration
     * @param json the Json segment for the switch
     * @return a SingleSwitch object
     */
    private SingleSwitch buildSingleSwitch(JsonObject json, VNO vno)
                                throws InvalidVNOConfigurationException
    {
        // find physical switch
        DPID phyID = new DPID(json.get("physical").getAsString());
        PhysicalSwitch psw = PhysicalNetwork.getInstance().getSwitch(phyID);
        if(psw == null)
            throw new InvalidVNOConfigurationException(
                    "Mapped to none-existing physical switch: " +
                    phyID.toString());
        
        // create virtual switch
        DPID   dpid = new DPID(json.get("dpid").getAsString());
        String name = json.get("name").getAsString();
        SingleSwitch vsw = new SingleSwitch(vno, dpid, name);
        vsw.setPhysicalSwitch(psw);
        
        // add virtual ports
        JsonArray portsJson = json.get("ports").getAsJsonArray();
        
        int index = 0;
        for(JsonElement e: portsJson)
        {
            int number = e.getAsInt();
            PhysicalPort port = psw.getPort(number);
            if(port == null)
                throw new InvalidVNOConfigurationException(
                        "Mapped to none-existing physical port: " +
                        "dpid: " + dpid.toString() + " " +
                        "port: " + number);
            
            // XXX: is it OK to reuse physical MAC address? OVX does so.
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
     * @throws InvalidVNOConfigurationException 
     */
    private VirtualSwitch buildSwitch(JsonObject json, VNO vno) 
                                        throws InvalidVNOConfigurationException {
        return json.get("type").getAsString().equals("single") ? buildSingleSwitch(json, vno) 
                                                               : buildBigSwitch   (json, vno);
    }
    
    /**
     * Build a VirtualPort based on the given Json configuration
     * @param json the segment of the configuration for the port
     * @param vnw the VirtualNetwork of this port
     * @return a VirtualPort object
     * @throws InvalidVNOConfigurationException 
     */
    private VirtualPort buildVirtualPort(JsonObject json, VNO vno) 
                                        throws InvalidVNOConfigurationException
    {
        DPID dpid   = new DPID(json.get("switch").getAsString());
        int  number = json.get("number").getAsInt();
        
        VirtualSwitch vsw = vno.getNetwork().getSwitch(dpid);
        if(vsw == null)
            throw new InvalidVNOConfigurationException(
                    "The switch " + dpid + " doesn't exist");
                    
        return vsw.getPort(number);
    }
    
    /**
     * Build a PhysicalPort based on the given Json configuration
     * @param json the segment of the configuration for the port
     * @return a VirtualPort object
     * @throws InvalidVNOConfigurationException 
     */
    private PhysicalPort buildPhysicalPort(JsonObject json) 
                                            throws InvalidVNOConfigurationException
    {
        DPID dpid   = new DPID(json.get("switch").getAsString());
        int  number = json.get("number").getAsInt();
        
        PhysicalSwitch psw = PhysicalNetwork.getInstance().getSwitch(dpid);
        if(psw == null)
            throw new InvalidVNOConfigurationException(
                    "The switch " + dpid + " doesn't exist");
        
        return psw.getPort(number);
    }
    
    /**
     * Build a VirtualLink based on the given Json configuration
     * @param json the segment of the configuration for the VirtualLink
     * @param vnw the VirtualNetwork of this port
     * @return a VirtualLink object
     * @throws InvalidVNOConfigurationException 
     */
    private VirtualLink buildLink(JsonObject json, VNO vno) 
                                    throws InvalidVNOConfigurationException
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
     * @throws InvalidVNOConfigurationException 
     */
    private Host buildHost(JsonObject json, VNO vno) 
                            throws InvalidVNOConfigurationException
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
