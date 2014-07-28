package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.exception.InvalidNetworkConfigurationException;
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
    protected PhysicalLink create(JsonObject json, JsonObject parentJson, VNO vno) 
                                    throws InvalidNetworkConfigurationException
    {
        if(json == null)
            throw new InvalidNetworkConfigurationException(
                                    "No definition for this PhysicalLink");
        
        if(!json.has("src"))
            throw new InvalidNetworkConfigurationException("No src port. Json: " + json);
        if(!json.has("dst"))
            throw new InvalidNetworkConfigurationException("No dst port. Json: " + json);
        JsonObject srcJson = json.get("src").getAsJsonObject();
        JsonObject dstJson = json.get("dst").getAsJsonObject();
        
        PhysicalPort srcPort = (PhysicalPort) ElementFactory.fromJson("PhysicalPort", srcJson, null);
        PhysicalPort dstPort = (PhysicalPort) ElementFactory.fromJson("PhysicalPort", dstJson, null);

        PhysicalLink link = PhysicalNetwork.getInstance().getLink(srcPort, dstPort);
        if(link != null)
            return link;

        // name is optional
        String name = json.has("name") ? json.get("name").getAsString() 
                                       : new String();
        return new PhysicalLink(name, srcPort, dstPort);
    }
}
