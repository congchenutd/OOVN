package com.fujitsu.us.oovn.element.datapath;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.factory.ElementFactory;
import com.fujitsu.us.oovn.factory.PhysicalSwitchFactory;

public class PhysicalSwitch extends Switch<PhysicalPort> implements Persistable
{
    static {
        ElementFactory.registerElement(PhysicalSwitch.class, new PhysicalSwitchFactory());
    }
    
    public PhysicalSwitch(DPID dpid, String name) {
        super(dpid, name);
    }

    @Override
    public String toDBMatch() {
        return  "(" + toDBVariable() +
                ":ZPhysical:Switch {" +
                    "type:\"PhysicalSwitch\"," +
                    "dpid:\"" + getDPID().toString() + "\"," +
                    "name:\"" + getName() + 
                "\"})";
    }
    
    /**
     * Create a PhysicalSwitch object from a Neo4j node representing the switch
     */
    public static PhysicalSwitch fromNode(Node node)
    {
        DPID dpid = new DPID(node.getProperty("dpid").toString());
        return PhysicalNetwork.getInstance().getSwitch(dpid);
    }
    
}
