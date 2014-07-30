package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.SingleSwitch;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.fujitsu.us.oovn.exception.InvalidConfigurationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SingleSwitchFactory extends ElementFactory {

    @Override
    public SingleSwitch create(Node node, VNO vno)
    {
        DPID dpid = new DPID(node.getProperty("dpid").toString());
        return (SingleSwitch) vno.getNetwork().getSwitch(dpid);
    }

    @Override
    protected SingleSwitch create(JsonObject json, JsonObject parentJson, VNO vno) 
                                throws InvalidConfigurationException
    {
        if(json == null)
            throw new InvalidConfigurationException(
                            "No definition for this SingleSwitch. Json: " + json);
        
        if(!json.has("dpid"))
            throw new InvalidConfigurationException(
                            "No dpid for this SingleSwitch. Json: " + json);
        
        DPID dpid = new DPID(json.get("dpid").getAsString());
        try {
            // found an existing switch
            return (SingleSwitch) vno.getNetwork().getSwitch(dpid);
        }
        catch(Exception ex)
        {
            // find physical switch
            if(!json.has("physical"))
                throw new InvalidConfigurationException(
                            "No physical switch defined for this SingleSwitch. Json: " + json);
            
            DPID phyID = new DPID(json.get("physical").getAsString());
            PhysicalSwitch psw = PhysicalNetwork.getInstance().getSwitch(phyID);
            
            // create virtual switch
            if(!json.has("name"))
                throw new InvalidConfigurationException(
                                "No name for this SingleSwitch. Json: " + json);
            String name = json.get("name").getAsString();
            SingleSwitch vsw = new SingleSwitch(vno, dpid, name);
            vsw.setPhysicalSwitch(psw);
            
            // create virtual ports
            if(!json.has("ports"))
                throw new InvalidConfigurationException(
                                "No ports for this SingleSwitch. Json: " + json);
            JsonArray portsJson = json.get("ports").getAsJsonArray();
            
            int index = 0;
            for(JsonElement e: portsJson)
            {
                int number = e.getAsInt();
                PhysicalPort port = psw.getPort(number);
                
                // XXX: is it OK to reuse physical MAC address? OVX does so.
                VirtualPort  vPort = new VirtualPort(++index, port.getMACAddress());
                vPort.setPhysicalPort(port);
                vsw.addPort(vPort);
            }
            
            return vsw;
        }
    }
    
    @Override
    protected String getTypeName() {
        return "SingleSwitch";
    }

}
