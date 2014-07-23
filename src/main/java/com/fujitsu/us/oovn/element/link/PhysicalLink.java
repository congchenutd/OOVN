package com.fujitsu.us.oovn.element.link;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;

public class PhysicalLink extends Link<PhysicalSwitch, PhysicalPort> implements Persistable
{
    public PhysicalLink(PhysicalPort src, PhysicalPort dst) {
        super(src, dst);
    }
    
    @Override
    public String toDBMatch() {
        return "(" + toDBVariable() + 
                ":Physical:Link " + "{" + 
                    "srcSwitch:" + "\"" + getSrcSwitch().getDPID().toString() + "\", " +
                    "srcPort:" + getSrcPort().getNumber() + "," +
                    "dstSwitch:" + "\"" + getDstSwitch().getDPID().toString() + "\", " +
                    "dstPort:" + getDstPort().getNumber() +
                "})";
    }
    
    /**
     * Create a PhysicalLink object from a Neo4j node representing the link
     */
    public static PhysicalLink fromNode(Node node)
    {
        String srcDPID = node.getProperty("srcSwitch").toString();
        String dstDPID = node.getProperty("dstSwitch").toString();
        int  srcNumber = Integer.valueOf(node.getProperty("srcPort").toString());
        int  dstNumber = Integer.valueOf(node.getProperty("dstPort").toString());
        return PhysicalNetwork.getInstance()
                              .getLink(srcDPID, srcNumber, dstDPID, dstNumber);
    }
}