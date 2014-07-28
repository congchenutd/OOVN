package com.fujitsu.us.oovn.element.port;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.element.NetworkElement;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PhysicalPort extends Port<PhysicalSwitch, PhysicalLink> 
                          implements NetworkElement
{
    public PhysicalPort(int number, MACAddress mac) {
        super(number, mac);
    }
    
    @Override
    public String toDBMatch() {
        return  "(" + toDBVariable() +
                ":ZPhysical:Port {" +
                    "type:\"PhysicalPort\"," +
                    "switch:\"" + getSwitch().getDPID().toString() + "\"," +
                    "number:" + getNumber() + "," +
                    "mac:\""  + getMACAddress() + "\"" +
                "})";
    }
    
    /**
     * Create a PhysicalPort object from a Neo4j node representing the port
     */
    public static PhysicalPort fromNode(Node node)
    {
        DPID dpid   = new DPID       (node.getProperty("switch").toString());
        int  number = Integer.valueOf(node.getProperty("number").toString());
        return PhysicalNetwork.getInstance().getSwitch(dpid).getPort(number);
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = (JsonObject) super.toJson();
        result.addProperty("type", "PhysicalPort");
        return result;
    }

}
