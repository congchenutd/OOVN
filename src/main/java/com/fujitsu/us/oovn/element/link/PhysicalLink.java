package com.fujitsu.us.oovn.element.link;

import org.neo4j.cypher.javacompat.ExecutionEngine;

import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.port.PhysicalPort;

public class PhysicalLink extends Link<PhysicalSwitch, PhysicalPort> implements Persistable
{
    public PhysicalLink(PhysicalPort src, PhysicalPort dst) {
        super(src, dst);
    }
    
    @Override
    public String toDBMatch() {
        return "(" + toDBVariable() + 
                ":Physical:Link " + "{" + 
                "srcSwitch:" + "\"" + getSrcSwitch().getDPID().toString() + "\", " +
                "srcPort:" + getSrcPort().getNumber() + "," +
                "dstSwitch:" + "\"" + getDstSwitch().getDPID().toString() + "\", " +
                "dstPort:" + getDstPort().getNumber() +
                "})";
    }

    @Override
    public void createSelf(ExecutionEngine engine)
    {
        engine.execute("CREATE " + toDBMatch());
        engine.execute(
                "MATCH \n" +
                toDBMatch() + ",\n" +
                getSrcPort().toDBMatch() + ",\n" +
                getDstPort().toDBMatch() + "\n" +
                "CREATE " + 
                "(" + toDBVariable() + ")-[:Connects]->(" + getSrcPort().toDBVariable() + ")," +
                "(" + toDBVariable() + ")-[:Connects]->(" + getDstPort().toDBVariable() + ")");
    }
    
    @Override
    public void createMapping(ExecutionEngine engine) {
    }
    
}