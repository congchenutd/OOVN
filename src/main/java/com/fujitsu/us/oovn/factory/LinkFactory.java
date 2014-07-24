package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;

public class LinkFactory extends ElementFactory {

    public LinkFactory() {}

    /**
     * Create a Link from a Neo4j node
     * @param node  a Neo4j node containing all the information of the Link
     * @param vno   the VNO, if the element is virtual. null for physical
     * @return      a Link object
     */
    @Override
    public Persistable create(Node node, VNO vno)
    {
        String srcDPID = node.getProperty("srcSwitch").toString();
        String dstDPID = node.getProperty("dstSwitch").toString();
        int  srcNumber = Integer.valueOf(node.getProperty("srcPort").toString());
        int  dstNumber = Integer.valueOf(node.getProperty("dstPort").toString());
        return vno == null ? PhysicalNetwork.getInstance()
                               .getLink(srcDPID, srcNumber, dstDPID, dstNumber)
                           : vno.getNetwork()
                               .getLink(srcDPID, srcNumber, dstDPID, dstNumber);
    }
}
