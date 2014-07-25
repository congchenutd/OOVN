/**
 * 
 */
package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
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
    protected Persistable create(JsonObject json, VNO vno) {
        // TODO Auto-generated method stub
        return null;
    }

}
