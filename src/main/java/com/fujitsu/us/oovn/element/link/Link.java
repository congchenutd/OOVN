package com.fujitsu.us.oovn.element.link;

import org.neo4j.cypher.javacompat.ExecutionEngine;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.port.Port;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Base class for all the links
 * Switches are connected by Links, while hosts are not
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */

@SuppressWarnings("rawtypes")
public abstract class Link<SwitchType extends Switch, PortType extends Port> implements Jsonable
{
    protected PortType _srcPort;
    protected PortType _dstPort;
    
    @SuppressWarnings("unchecked")
    public Link(PortType src, PortType dst)
    {
        _srcPort = src;
        _dstPort = dst;
        _srcPort.setLink(this);
        _dstPort.setLink(this);
    }
    
    public PortType getSrcPort() {
        return _srcPort;
    }
    
    public PortType getDstPort() {
        return _dstPort;
    }
    
    @SuppressWarnings("unchecked")
    public SwitchType getSrcSwitch() {
        return (SwitchType) _srcPort.getSwitch();
    }
    
    @SuppressWarnings("unchecked")
    public SwitchType getDstSwitch() {
        return (SwitchType) _dstPort.getSwitch();
    }
    
    public PortType getOtherPort(PortType port) {
        return port.equals(getSrcPort()) ? getDstPort() 
                                         : port.equals(getDstPort()) ? getSrcPort() 
                                                                     : null; 
    }
    
    /**
     * @return whether srcPort and dstPort are connected by a link
     */
    @SuppressWarnings("unchecked")
    public static boolean isConnected(Port srcPort, Port dstPort)
    {
        Link link = srcPort.getLink();
        return link.getOtherPort(srcPort) == dstPort; 
    }

    /**
     * @return variable name for Neo4j query, in the form of S1P1S2P2
     */
    public String toDBVariable() {
        return getSrcPort().toDBVariable() + getDstPort().toDBVariable();
    }
    
    @Override
    public String toString() {
        return  getSrcPort().toDBVariable() + "-" + getDstPort().toDBVariable();                
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.add("src", getSrcPort().toJson());
        result.add("dst", getDstPort().toJson());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(!(obj instanceof Link))
            return false;
        
        Link that = (Link) obj;
        
        // both null
        if(this.getSrcPort() == null && this.getDstPort() == null && 
           that.getSrcPort() == null && that.getDstPort() == null)
            return true;

        // same or same reversed ends
        if(this.getSrcPort() != null && this.getDstPort() != null && 
           that.getSrcPort() != null && that.getDstPort() != null)
        {
            return getSrcPort().equals(that.getSrcPort()) &&
                   getDstPort().equals(that.getDstPort())    ||
                   getSrcPort().equals(that.getDstPort()) &&
                   getDstPort().equals(that.getSrcPort());
        }
        return false;
    }

    public abstract String toDBMatch();
    
    public void createInDB(ExecutionEngine engine)
    {
        // create the link node itself
        engine.execute("CREATE " + toDBMatch());
        
        // create the relationships to the 2 ports
        engine.execute(
                "MATCH \n" +
                toDBMatch() + ",\n" +
                getSrcPort().toDBMatch() + ",\n" +
                getDstPort().toDBMatch() + "\n" +
                "CREATE " + 
                "(" + toDBVariable() + ")-[:Connects]->(" + getSrcPort().toDBVariable() + ")," +
                "(" + toDBVariable() + ")-[:Connects]->(" + getDstPort().toDBVariable() + ")");
        
        createMapping(engine);
    }
    
    public void createMapping(ExecutionEngine engine) {
    }
}