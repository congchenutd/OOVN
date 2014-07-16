package com.fujitsu.us.oovn.element.datapath;

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
    public String toDBCreate()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("(" + toDBMatch() + ")");
        
        for(PhysicalPort port: getPorts().values())
        {
            builder.append(",\n");
            builder.append(port.toDBCreate() + ",\n");
            builder.append("(" + getName() + ")-[:Has]->(" + port.getName() + ")");
        }
        
        return builder.toString();
    }
    
    @Override
    public String toDBMatch() {
        return  getName() +
                ":Physical:Switch {" +
                "dpid:\"" + getDPID().toString() + "\"," +
                "name:\"" + getName() + "\"}";
    }
    
}
