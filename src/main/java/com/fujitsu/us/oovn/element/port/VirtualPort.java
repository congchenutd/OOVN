package com.fujitsu.us.oovn.element.port;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
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
        PhysicalPort thisPhyPort = this.getPhysicalPort();
        PhysicalPort thatPhyPort = that.getPhysicalPort();
        return thisPhyPort == null && thatPhyPort == null ||
               thisPhyPort != null && thatPhyPort != null && thisPhyPort.equals(thatPhyPort);
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
                "CREATE \n" + 
                    "(" + toDBVariable() + ")-[:Maps]->(" + 
                    getPhysicalPort().toDBVariable() + ")");
    }
    
    /**
     * Find the virtual port in the VNO from a db node
     * @param node  a Neo4j node representing a virtual port
     * @param vno
     * @return      the corresponding VirtualPort object
     */
    public static VirtualPort fromNode(Node node, VNO vno)
    {
        DPID dpid   = new DPID(node.getProperty("switch").toString());
        int  number = Integer.valueOf(node.getProperty("number").toString());
        return vno.getNetwork().getSwitch(dpid).getPort(number);
    }

}
