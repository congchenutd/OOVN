package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;

public class PortFactory extends ElementFactory {

    public PortFactory() {}

    /**
     * Create a Port from a Neo4j node
     * @param node  a Neo4j node containing all the information of the Port
     * @param vno   the VNO, if the element is virtual. null for physical
     * @return      a Port object
     */
    @Override
    public Persistable create(Node node, VNO vno)
    {
        DPID dpid   = new DPID       (node.getProperty("switch").toString());
        int  number = Integer.valueOf(node.getProperty("number").toString());
        return vno == null ? PhysicalNetwork.getInstance().getSwitch(dpid).getPort(number)
                           : vno.getNetwork().getSwitch(dpid).getPort(number);
    }
}
