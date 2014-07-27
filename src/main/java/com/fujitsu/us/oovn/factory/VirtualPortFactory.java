package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.google.gson.JsonObject;

public class VirtualPortFactory extends ElementFactory {

    @Override
    protected Persistable create(Node node, VNO vno)
    {
        DPID dpid   = new DPID       (node.getProperty("switch").toString());
        int  number = Integer.valueOf(node.getProperty("number").toString());
        return vno.getNetwork().getSwitch(dpid).getPort(number);
    }

    @Override
    protected Persistable create(JsonObject json, VNO vno)
    {
        DPID dpid   = new DPID(json.get("switch").getAsString());
        int  number = json.get("number").getAsInt();
        return vno.getNetwork().getSwitch(dpid).getPort(number);
    }
}
