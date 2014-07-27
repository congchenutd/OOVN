/**
 * 
 */
package com.fujitsu.us.oovn.factory;

import java.util.LinkedList;
import java.util.List;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
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
    public Persistable create(Node node, VNO vno)
    {
        String srcDPID = node.getProperty("srcSwitch").toString();
        String dstDPID = node.getProperty("dstSwitch").toString();
        int  srcNumber = Integer.valueOf(node.getProperty("srcPort").toString());
        int  dstNumber = Integer.valueOf(node.getProperty("dstPort").toString());
        return vno.getNetwork().getLink(srcDPID, srcNumber, dstDPID, dstNumber);
    }

    @Override
    protected Persistable create(JsonObject json, VNO vno) 
                            throws InvalidNetworkConfigurationException
    {
        JsonObject vSrcJson = json.get("src").getAsJsonObject();
        JsonObject vDstJson = json.get("dst").getAsJsonObject();
        
        VirtualPort srcPort = (VirtualPort) ElementFactory.fromJson(vSrcJson, vno);
        VirtualPort dstPort = (VirtualPort) ElementFactory.fromJson(vDstJson, vno);
        
        VirtualLink link = vno.getNetwork().getLink(srcPort, dstPort);
        if(link != null)
            return link;

        link = new VirtualLink(vno, json.get("name")
                .getAsString(), srcPort, dstPort);

        JsonArray pathJson = json.get("path").getAsJsonArray();
        List<PhysicalLink> path = new LinkedList<PhysicalLink>();
        for (JsonElement e : pathJson)
            path.add((PhysicalLink) ElementFactory.fromJson((JsonObject) e));
        if (!path.isEmpty())
            link.setPath(path);

        return link;
    }

}
