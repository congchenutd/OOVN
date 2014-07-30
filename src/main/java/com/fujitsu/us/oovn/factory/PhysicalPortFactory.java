package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.exception.InvalidConfigurationException;
import com.google.gson.JsonObject;

public class PhysicalPortFactory extends ElementFactory {

    @Override
    protected PhysicalPort create(Node node, VNO vno)
    {
        DPID dpid   = new DPID       (node.getProperty("switch").toString());
        int  number = Integer.valueOf(node.getProperty("number").toString());
        return PhysicalNetwork.getInstance().getSwitch(dpid).getPort(number);
    }
    
    @Override
    protected PhysicalPort create(JsonObject json, JsonObject parentJson, VNO vno) 
                                            throws InvalidConfigurationException
    {
        if(json == null)
            throw new InvalidConfigurationException(
                            "No definition for this PhysicalPort. Json: " + json);
        
        DPID dpid = null;
        if(json.has("dpid"))
            dpid = new DPID(json.get("dpid").getAsString());
        else
        {
            if(parentJson == null || !parentJson.has("dpid"))
                throw new InvalidConfigurationException(
                            "No dpid for this PhysicalPort. Json: " + json);
            dpid = new DPID(parentJson.get("dpid").getAsString());
        }
              
        if(!json.has("number"))
            throw new InvalidConfigurationException(
                            "No port number for this VirtualPort. Json: " + json);
        int number = json.get("number").getAsInt();
        
        try {
            return PhysicalNetwork.getInstance().getPort(dpid, number);
        } catch(Exception e) {
            String mac = json.get("mac").getAsString();
            return new PhysicalPort(number, new MACAddress(mac));
        }
    }
    
    @Override
    protected String getTypeName() {
        return "PhysicalPort";
    }

}
