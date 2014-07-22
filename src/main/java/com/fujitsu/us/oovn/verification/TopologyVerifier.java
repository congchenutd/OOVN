package com.fujitsu.us.oovn.verification;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.ResourceIterator;

import com.fujitsu.us.oovn.core.NetworkBuilder;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.exception.InvalidVNOConfigurationException;
import com.fujitsu.us.oovn.map.GlobalMap;

/**
 * Verify if the requested topology is correct
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
        // build the network based on the config
        // this will check for wrong mappings
        try {
            NetworkBuilder.getInstance().build(vno);
        } catch (InvalidVNOConfigurationException e) {
            return new VerificationResult(false, e.getMessage());
        }
        
        // check for conflicted mappings
        ExecutionEngine engine = GlobalMap.getInstance().getExecutionEngine();
        ExecutionResult result = engine.execute(
                "MATCH (v:Virtual {vnoid:" + vno.getID() + "})-[:Maps]->(p:Physical)" +
                "WITH p, count(v) AS mapCount" +
                "WHERE mapCount > 1" +
                "return p");
        ResourceIterator<Node> it = result.columnAs("p");
        if(it.hasNext())
        {
//            String dpid = it.next().getProperty("dpid").toString();
//            if(it.next().hasLabel("Switch"))
//            return new VerificationResult(false, "Physical node")
        }
        
        // pass the task to the next verifier
        return super.verify(vno);
    }

}
