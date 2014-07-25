package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.datapath.SingleSwitch;
import com.google.gson.JsonObject;

public class SingleSwitchFactory extends ElementFactory {

    @Override
    public Persistable create(Node node, VNO vno)
    {
        DPID dpid = new DPID(node.getProperty("dpid").toString());
        return (SingleSwitch) vno.getNetwork().getSwitch(dpid);
    }

    @Override
    protected Persistable create(JsonObject json, VNO vno) {
        // TODO Auto-generated method stub
        return null;
    }

}
