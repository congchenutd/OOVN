package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
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
    protected PhysicalPort create(JsonObject json, VNO vno)
    {
        DPID dpid  = new DPID(json.get("switch").getAsString());
        int number = json.get("number").getAsInt();
        
        try {
            return PhysicalNetwork.getInstance().getPort(dpid, number);
        } catch(Exception e) {
            String mac = json.get("mac")   .getAsString();
            return new PhysicalPort(number, new MACAddress(mac));
        }
    }

}
