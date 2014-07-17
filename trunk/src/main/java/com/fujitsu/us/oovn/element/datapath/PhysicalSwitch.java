package com.fujitsu.us.oovn.element.datapath;

import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.port.PhysicalPort;

public class PhysicalSwitch extends Switch<PhysicalPort> implements Persistable
{
    public PhysicalSwitch(DPID dpid, String name) {
        super(dpid, name);
    }

    @Override
    public String toDBMatch() {
        return  "(" + toDBVariable() +
                ":Physical:Switch {" +
                "dpid:\"" + getDPID().toString() + "\"," +
                "name:\"" + getName() + "\"})";
    }
    
}
