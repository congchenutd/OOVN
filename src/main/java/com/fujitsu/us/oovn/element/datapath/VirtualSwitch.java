package com.fujitsu.us.oovn.element.datapath;

import java.util.List;

import org.neo4j.cypher.javacompat.ExecutionEngine;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.port.VirtualPort;

public abstract class VirtualSwitch extends Switch<VirtualPort>
{
    protected final VNO _vno;

    public VirtualSwitch(VNO vno, DPID dpid, String name)
    {
        super(dpid, name);
        _vno = vno;
    }
    
    public VNO getVNO() {
        return _vno;
    }

    public abstract List<PhysicalSwitch> getPhysicalSwitches();
    public abstract void createSelf(ExecutionEngine engine);
}
