package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.exception.InvalidNetworkConfigurationException;
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
                                    throws InvalidNetworkConfigurationException
    {
        if(json == null)
            throw new InvalidNetworkConfigurationException(
                                    "No definition for this PhysicalSwitch");
        
        if(!json.has("dpid"))
            throw new InvalidNetworkConfigurationException(
                                    "No dpid for this PhysicalSwitch");
        
        DPID dpid = new DPID(json.get("dpid").getAsString());
        try {
            // found an existing switch
            return PhysicalNetwork.getInstance().getSwitch(dpid);
        }
        catch(Exception ex)
        {
            // create a new one
            if(!json.has("name"))
                throw new InvalidNetworkConfigurationException(
                                "No name for this PhysicalSwitch. Json: " + json);
            String name = json.get("name").getAsString();
            PhysicalSwitch result = new PhysicalSwitch(dpid, name);
            
            // add ports
            if(!json.has("ports"))
                throw new InvalidNetworkConfigurationException(
                                "No ports for this PhysicalSwitch. Json: " + json);
            for(JsonElement e: json.get("ports").getAsJsonArray())
            {
                JsonObject jsonObj = e.getAsJsonObject();
                result.addPort((PhysicalPort) ElementFactory.fromJson("PhysicalPort", jsonObj, json));
            }
            
            return result;
        }
    }

}
