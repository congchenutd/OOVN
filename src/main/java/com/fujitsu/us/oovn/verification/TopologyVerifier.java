package com.fujitsu.us.oovn.verification;

import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import com.fujitsu.us.oovn.core.NetworkBuilder;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.map.GlobalMap;

/**
 * Verify if a VNO has valid topology
 * i.e., no wrong mappings (to none-existing physical node) 
 *       or conflicted mappings (n virtual -> 1 physical)
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class TopologyVerifier extends Verifier
{
    
    public TopologyVerifier(Verifier next) {
        super(next);
    }

    @Override
    public VerificationResult verify(VNO vno)
    {
//        // build the network based on the config
//        // this will check for wrong mappings
//        try {
//            NetworkBuilder.getInstance().build(vno);
//        } catch (Exception e) {
//            return new VerificationResult(false, e.getMessage());
//        }
//
//        // add this vno to global map for verification
//        GlobalMap.getInstance().registerVNO(vno);
//        
//        // check for conflicted mappings
//        ExecutionResult result = GlobalMap.getInstance().query(
//                "MATCH (v:Virtual {vnoid:" + vno.getID() + "})-[:Maps]->(p:ZPhysical)" +
//                " WITH p, count(v) AS mapCount" +
//                " WHERE mapCount > 1" +
//                " RETURN p");
//        
//        System.out.println(
//                "MATCH (v:Virtual {vnoid:" + vno.getID() + "})-[:Maps]->(p:ZPhysical)" +
//                " WITH p, count(v) AS mapCount" +
//                " WHERE mapCount > 1" +
//                " RETURN p");
//        ResourceIterator<Node> it = result.columnAs("p");
//        
//        // remove the vno from the global map
////        GlobalMap.getInstance().unregisterVNO(vno);
//
//        if(it.hasNext())
//            return new VerificationResult(false, 
//                    "This physical node is mapped by more than one virtual node: " +
//                    fromNode(it.next()).toString());
//
//        // pass the task to the next verifier
        return super.verify(vno);
    }
    
    /**
     * Generate a physical element from a db node
     */
    private Persistable fromNode(Node node)
    {
        if(node.hasLabel(DynamicLabel.label("ZPhysical")))
        {
            if(node.hasLabel(DynamicLabel.label("Switch")))
                return PhysicalSwitch.fromNode(node);
            else if(node.hasLabel(DynamicLabel.label("Link")))
                return PhysicalLink.fromNode(node);
            else if(node.hasLabel(DynamicLabel.label("Port")))
                return PhysicalPort.fromNode(node);
        }
        return null;
    }

}
