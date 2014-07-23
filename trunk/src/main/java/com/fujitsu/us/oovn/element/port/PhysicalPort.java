package com.fujitsu.us.oovn.element.port;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;

public class PhysicalPort extends Port<PhysicalSwitch, PhysicalLink> 
                          implements Persistable
{
    public PhysicalPort(int number, MACAddress mac) {
        super(number, mac);
    }
    
    @Override
    public String toDBMatch() {
        return  "(" + toDBVariable() +
                ":Physical:Port {" +
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

}
