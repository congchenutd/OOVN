package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.exception.InvalidConfigurationException;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PhysicalSwitchFactory extends ElementFactory {

    @Override
    public PhysicalSwitch create(Node node, VNO vno)
    {
        DPID dpid = new DPID(node.getProperty("dpid").toString());
        return PhysicalNetwork.getInstance().getSwitch(dpid);
    }

    @Override
    protected PhysicalSwitch create(JsonObject json, JsonObject parentJson, VNO vno) 
                                    throws InvalidConfigurationException
    {
        if(json == null)
            throw new InvalidConfigurationException(
                        "No definition for this PhysicalSwitch. Json: " + json);
        
        if(!json.has("dpid"))
            throw new InvalidConfigurationException(
                        "No dpid for this PhysicalSwitch. Json: " + json);
        
        DPID dpid = new DPID(json.get("dpid").getAsString());
        try {
            // found an existing switch
            return PhysicalNetwork.getInstance().getSwitch(dpid);
        }
        catch(Exception ex)
        {
            // create a new one
            if(!json.has("name"))
                throw new InvalidConfigurationException(
                                "No name for this PhysicalSwitch. Json: " + json);
            String name = json.get("name").getAsString();
            PhysicalSwitch result = new PhysicalSwitch(dpid, name);
            
            // add ports
            if(!json.has("ports"))
                throw new InvalidConfigurationException(
                                "No ports for this PhysicalSwitch. Json: " + json);
            for(JsonElement e: json.get("ports").getAsJsonArray())
            {
                JsonObject jsonObj = e.getAsJsonObject();
                result.addPort((PhysicalPort) ElementFactory.fromJson(
                                    "PhysicalPort", jsonObj, json));
            }
            
            return result;
        }
    }
    
    @Override
    protected String getTypeName() {
        return "PhysicalSwitch";
    }

}
