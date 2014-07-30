/**
 * 
 */
package com.fujitsu.us.oovn.factory;

import java.util.LinkedList;
import java.util.List;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.link.VirtualLink;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.fujitsu.us.oovn.exception.InvalidConfigurationException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class VirtualLinkFactory extends ElementFactory {

    @Override
    public VirtualLink create(Node node, VNO vno)
    {
        String srcDPID = node.getProperty("srcSwitch").toString();
        String dstDPID = node.getProperty("dstSwitch").toString();
        int  srcNumber = Integer.valueOf(node.getProperty("srcPort").toString());
        int  dstNumber = Integer.valueOf(node.getProperty("dstPort").toString());
        return vno.getNetwork().getLink(srcDPID, srcNumber, dstDPID, dstNumber);
    }

    @Override
    protected VirtualLink create(JsonObject json, JsonObject parentJson, VNO vno) 
                            throws InvalidConfigurationException
    {
        if(json == null || json.isJsonNull())
            throw new InvalidConfigurationException(
                            "No definition for this VirtualLink. Json: " + json);
        
        if(!json.has("src"))
            throw new InvalidConfigurationException("No src port. Json: " + json);
        if(!json.has("dst"))
            throw new InvalidConfigurationException("No dst port. Json: " + json);
        JsonObject vSrcJson = json.get("src").getAsJsonObject();
        JsonObject vDstJson = json.get("dst").getAsJsonObject();
        
        VirtualPort srcPort = (VirtualPort) ElementFactory.fromJson("VirtualPort", vSrcJson, parentJson, vno);
        VirtualPort dstPort = (VirtualPort) ElementFactory.fromJson("VirtualPort", vDstJson, parentJson, vno);
        
        // link exists
        VirtualLink link = vno.getNetwork().getLink(srcPort, dstPort);
        if(link != null)
            return link;

        // create a new link
        link = new VirtualLink(vno, json.get("name").getAsString(), srcPort, dstPort);

        if(!json.has("path"))
            throw new InvalidConfigurationException(
                        "No path for the VirtualLink. Json: " + json);
        JsonArray pathJson = json.get("path").getAsJsonArray();
        
        List<PhysicalLink> path = new LinkedList<PhysicalLink>();
        for (JsonElement e : pathJson)
        {
            JsonObject linkJson = e.getAsJsonObject();
            path.add((PhysicalLink) ElementFactory.fromJson(
                                "PhysicalLink", linkJson, parentJson, null));
        }
        if (!path.isEmpty())
            link.setPath(path);

        return link;
    }

    @Override
    protected String getTypeName() {
        return "VirtualLink";
    }

}
