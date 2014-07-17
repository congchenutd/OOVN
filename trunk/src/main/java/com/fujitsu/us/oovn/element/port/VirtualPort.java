package com.fujitsu.us.oovn.element.port;

import org.neo4j.cypher.javacompat.ExecutionEngine;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.VirtualSwitch;
import com.fujitsu.us.oovn.element.link.VirtualLink;

public class VirtualPort extends Port<VirtualSwitch, VirtualLink> 
                         implements Persistable
{
    private PhysicalPort _physicalPort;
    
    public VirtualPort(int number, MACAddress mac) {
        super(number, mac);
    }

    public void setPhysicalPort(PhysicalPort port) {
        _physicalPort = port;
    }
    
    public PhysicalPort getPhysicalPort() {
        return _physicalPort;
    }
    
    public VNO getVNO() {
        return getSwitch().getVNO();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(!super.equals(obj))
            return false;
        
        if(!(obj instanceof VirtualPort))
            return false;
        
        VirtualPort that = (VirtualPort) obj;
        return this.getPhysicalPort().equals(that.getPhysicalPort());
    }
    
    @Override
    public String toDBMatch() {
        return  "(" + toDBVariable() +
                ":Virtual:Port {" +
                    "vnoid:" + getVNO().getID() + "," +
                    "switch:\"" + getSwitch().getDPID().toString() + "\"," +
                    "number:" + getNumber() + "," +
                    "mac:\""  + getMACAddress() + "\"" + 
                "})";
    }
    
    @Override
    public void createMapping(ExecutionEngine engine)
    {
        // nothing to map to
        if(getPhysicalPort() == null)
            return;
        
        engine.execute(
                "MATCH \n" + 
                toDBMatch() + ",\n" +
                getPhysicalPort().toDBMatch() + "\n" +
                "CREATE (" + toDBVariable() + ")-[:Maps]->(" + getPhysicalPort().toDBVariable() + ")");
    }

}
