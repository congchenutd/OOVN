package com.fujitsu.us.oovn.element.link;

import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.port.PhysicalPort;

public class PhysicalLink extends Link<PhysicalSwitch, PhysicalPort> implements Persistable
{
    public PhysicalLink(PhysicalPort src, PhysicalPort dst) {
        super(src, dst);
    }
    
    @Override
    public String toDBCreate() {
        return "(" + getName() + 
                ":Physical:Link " + "{" + 
                "srcSwitch:" + "\"" + getSrcSwitch().getDPID().toString() + "\", " +
                "srcPort:" + getSrcPort().getNumber() + "," +
                "dstSwitch:" + "\"" + getDstSwitch().getDPID().toString() + "\", " +
                "dstPort:" + getDstPort().getNumber() +
                "})";
    }

    @Override
    public String toDBMatch() {
        return  toDBCreate();
    }
    
    @Override
    public String toDBMapping() {
        return null;
    }
    
}