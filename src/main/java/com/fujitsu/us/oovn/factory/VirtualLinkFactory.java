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
import com.fujitsu.us.oovn.exception.InvalidNetworkConfigurationException;
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
                            throws InvalidNetworkConfigurationException
    {
        if(json == null)
            throw new InvalidNetworkConfigurationException(
                                    "No definition for this VirtualLink");
        
        if(!json.has("src"))
            throw new InvalidNetworkConfigurationException("No src port");
        if(!json.has("dst"))
            throw new InvalidNetworkConfigurationException("No dst port");
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
            throw new InvalidNetworkConfigurationException("No path for the VirtualLink");
        JsonArray pathJson = json.get("path").getAsJsonArray();
        
        List<PhysicalLink> path = new LinkedList<PhysicalLink>();
        for (JsonElement e : pathJson)
        {
            PhysicalLink plink = (PhysicalLink) ElementFactory.fromJson("PhysicalLink", (JsonObject) e, parentJson);
            path.add(plink);
        }
        if (!path.isEmpty())
            link.setPath(path);

        return link;
    }

}
