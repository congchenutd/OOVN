package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.NetworkElement;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.fujitsu.us.oovn.exception.InvalidNetworkConfigurationException;
import com.google.gson.JsonObject;

public class VirtualPortFactory extends ElementFactory {

    @Override
    protected NetworkElement create(Node node, VNO vno)
    {
        DPID dpid   = new DPID       (node.getProperty("switch").toString());
        int  number = Integer.valueOf(node.getProperty("number").toString());
        return vno.getNetwork().getSwitch(dpid).getPort(number);
    }

    @Override
    protected NetworkElement create(JsonObject json, JsonObject parentJson, VNO vno) 
                            throws InvalidNetworkConfigurationException
    {
        if(json == null)
            throw new InvalidNetworkConfigurationException(
                                    "No definition for this VirtualPort");
        
        DPID dpid = null;
        if(json.has("dpid"))
            dpid = new DPID(json.get("dpid").getAsString());
        else
        {
            if(parentJson == null || !parentJson.has("dpid"))
                throw new InvalidNetworkConfigurationException(
                                    "No dpid for this VirtualPort");
            dpid = new DPID(parentJson.get("dpid").getAsString());
        }
        
        if(!json.has("number"))
            throw new InvalidNetworkConfigurationException(
                                    "No port number for this VirtualPort");
        
        int number = json.get("number").getAsInt();
        return vno.getNetwork().getSwitch(dpid).getPort(number);
    }

    @Override
    protected Class<? extends NetworkElement> getProductType() {
        return VirtualPort.class;
    }
}
