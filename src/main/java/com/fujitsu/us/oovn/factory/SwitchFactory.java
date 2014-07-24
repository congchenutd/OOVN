package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;

public class SwitchFactory extends ElementFactory {

    public SwitchFactory() {}
    
    /**
     * Create a Switch from a Neo4j node
     * @param node  a Neo4j node containing all the information of the Switch
     * @param vno   the VNO, if the element is virtual. null for physical
     * @return      a Switch object
     */
    @Override
    public Persistable create(Node node, VNO vno)
    {
        DPID dpid = new DPID(node.getProperty("dpid").toString());
        return (Persistable) (
                vno == null ? PhysicalNetwork.getInstance().getSwitch(dpid)
                            : vno.getNetwork().getSwitch(dpid));
    }

}
