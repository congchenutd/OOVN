package com.fujitsu.us.oovn.element.datapath;

import org.neo4j.cypher.javacompat.ExecutionEngine;

import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.port.PhysicalPort;

public class PhysicalSwitch extends Switch<PhysicalPort> implements Persistable
{
    public PhysicalSwitch(DPID dpid, String name)
    {
        super(dpid, name);
    }

    @Override
    public String toDBMatch() {
        return  "(" + toDBVariable() +
                ":Physical:Switch {" +
                "dpid:\"" + getDPID().toString() + "\"," +
                "name:\"" + getName() + "\"})";
    }

    @Override
    public void createSelf(ExecutionEngine engine)
    {
        // create the switch itself
        engine.execute("CREATE " + toDBMatch());
        
        // create and connect ports
        for(PhysicalPort port: getPorts().values())
        {
            port.createSelf(engine);
            engine.execute(
                    "MATCH \n" +
                    toDBMatch() + ",\n" +
                    port.toDBMatch() + "\n" +
                    "CREATE (" + toDBVariable() + ")-[:Has]->(" + port.toDBVariable() + ")");
        }
    }

    @Override
    public void createMapping(ExecutionEngine engine) {
    }
    
}
