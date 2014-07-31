package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.exception.InvalidConfigurationException;
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
                                    throws InvalidConfigurationException
    {
        if(json == null || json.isJsonNull())
            throw new InvalidConfigurationException(
                            "No definition for this PhysicalLink. Json: " + json);
        
        // load ports info
        if(!json.has("src"))
            throw new InvalidConfigurationException("No src port. Json: " + json);
        if(!json.has("dst"))
            throw new InvalidConfigurationException("No dst port. Json: " + json);
        JsonObject srcJson = json.get("src").getAsJsonObject();
        JsonObject dstJson = json.get("dst").getAsJsonObject();
        
        PhysicalPort srcPort = (PhysicalPort) ElementFactory.fromJson(
                                            "PhysicalPort", srcJson, null);
        PhysicalPort dstPort = (PhysicalPort) ElementFactory.fromJson(
                                            "PhysicalPort", dstJson, null);

        // find an existing link
        PhysicalLink link = PhysicalNetwork.getInstance().getLink(srcPort, dstPort);
        if(link != null)
            return link;

        // create a new link
        // name is optional
        String name = json.has("name") ? json.get("name").getAsString() 
                                       : new String();
        return new PhysicalLink(name, srcPort, dstPort);
    }

    @Override
    protected String getTypeName() {
        return "PhysicalLink";
    }
}
