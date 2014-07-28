package com.fujitsu.us.oovn.element.datapath;

import java.util.List;

import org.neo4j.graphdb.Node;

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
    
    /**
     * Find the virtual switch in the VNO from a db node
     * @param node  a Neo4j node representing a virtual switch
     * @param vno
     * @return      the corresponding VirtualSwitch object
     */
    public static VirtualSwitch fromNode(Node node, VNO vno)
    {
        DPID dpid = new DPID(node.getProperty("dpid").toString());
        return vno.getNetwork().getSwitch(dpid);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(!super.equals(obj))
            return false;
        
        if(!(obj instanceof VirtualSwitch))
            return false;
        
        // same vno
        VirtualSwitch that = (VirtualSwitch) obj;
        if(!this.getVNO().equals(that.getVNO()))
            return false;
        
        // same physical switches
        // XXX: Bug: order matters!!
        return this.getPhysicalSwitches().equals(that.getPhysicalSwitches());        
    }
}
