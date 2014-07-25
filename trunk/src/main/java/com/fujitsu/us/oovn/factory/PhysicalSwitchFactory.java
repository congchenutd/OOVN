package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
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
    protected PhysicalSwitch create(JsonObject json, VNO vno)
    {
        DPID dpid = new DPID(json.get("dpid").getAsString());
        String name = json.get("name").getAsString();
        PhysicalSwitch result = new PhysicalSwitch(dpid, name);
        
        // add ports
        for(JsonElement e: json.get("ports").getAsJsonArray())
        {
            JsonObject jsonObj = e.getAsJsonObject();
            result.addPort((PhysicalPort) ElementFactory.fromJson(jsonObj, null));
        }
        
        return result;
    }

}
