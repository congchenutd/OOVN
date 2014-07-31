package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.NetworkElement;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.exception.InvalidConfigurationException;
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
                            throws InvalidConfigurationException
    {
        if(json == null || json.isJsonNull())
            throw new InvalidConfigurationException(
                        "No definition for this VirtualPort. Json: " + json);
        
        // dpid
        DPID dpid = null;
        if(json.has("dpid"))
            dpid = new DPID(json.get("dpid").getAsString());
        else
        {
            // try to load dpid from parent json
            if(parentJson == null || !parentJson.has("dpid"))
                throw new InvalidConfigurationException(
                                "No dpid for this VirtualPort. Json: " + json);
            dpid = new DPID(parentJson.get("dpid").getAsString());
        }
        
        // port number
        if(!json.has("number"))
            throw new InvalidConfigurationException(
                            "No port number for this VirtualPort. Json: " + json);
        int number = json.get("number").getAsInt();
        
        return vno.getNetwork().getSwitch(dpid).getPort(number);
    }

    @Override
    protected String getTypeName() {
        return "VirtualPort";
    }
}
