package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.google.gson.JsonObject;

public class PhysicalLinkFactory extends ElementFactory {

    @Override
    public PhysicalLink create(Node node, VNO vno)
    {
        String srcDPID = node.getProperty("srcSwitch").toString();
        String dstDPID = node.getProperty("dstSwitch").toString();
        int  srcNumber = Integer.valueOf(node.getProperty("srcPort").toString());
        int  dstNumber = Integer.valueOf(node.getProperty("dstPort").toString());
        return PhysicalNetwork.getInstance()
                               .getLink(srcDPID, srcNumber, dstDPID, dstNumber);
    }

    @Override
    protected PhysicalLink create(JsonObject json, VNO vno)
    {
        JsonObject srcJson = json.get("src").getAsJsonObject();
        JsonObject dstJson = json.get("dst").getAsJsonObject();
        
        PhysicalPort srcPort = (PhysicalPort) ElementFactory.fromJson(srcJson, null);
        PhysicalPort dstPort = (PhysicalPort) ElementFactory.fromJson(dstJson, null);
        
        try {
            return PhysicalNetwork.getInstance().getLink(srcPort, dstPort);
        } catch(Exception e) {
            String name = json.get("name").getAsString();
            return new PhysicalLink(name, srcPort, dstPort);
        }
    }
}
