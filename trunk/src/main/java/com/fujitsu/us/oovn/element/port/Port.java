package com.fujitsu.us.oovn.element.port;

import org.neo4j.cypher.javacompat.ExecutionEngine;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.host.Host;
import com.fujitsu.us.oovn.element.link.Link;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Base class for all ports
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
@SuppressWarnings("rawtypes")
public abstract class Port<SwitchType extends Switch, LinkType extends Link> implements Jsonable
{
    private int        _number;
    private MACAddress _mac;
    private SwitchType _switch;
    private LinkType   _link;   // XXX: a port can connect to either a link or a host
    private Host       _host;

    public Port(int number, MACAddress mac)
    {
        setNumber(number);
        setMACAddress(mac);
    }
    
    public SwitchType getSwitch() {
        return _switch;
    }
    
    public void setSwitch(SwitchType sw) {
        _switch = sw;
    }
    
    public Host getHost() {
        return _host;
    }
    
    public void setHost(Host host)
    {
        if(host != null)
        {
            _host = host;
            _link = null;
        }
    }
    
    public int getNumber() {
        return _number;
    }
    
    public void setNumber(int number) {
        _number = number;
    }

    public MACAddress getMACAddress() {
        return _mac;
    }

    public void setMACAddress(MACAddress mac) {
        _mac = mac;
    }

    public Boolean isEdge() {
        return _host != null;
    }

    public LinkType getLink() {
        return _link;
    }

    public void setLink(LinkType link)
    {
        if(link != null)
        {
            _link = link;
            _host = null;
        }
    }
    
    /**
     * @return the port that this port connects to, or null if it's not connected
     */
    @SuppressWarnings("unchecked")
    public Port getNeighbor()
    {
        LinkType link = getLink();
        if(link == null)
            return null;
        
        return link.getOtherPort(this);
    }

    @Override
    public String toString() {
        return  "PORT:" +
                "\n- switch: " + getSwitch().getName() +
                "\n- number: " + getNumber() + 
                "\n- mac:    " + getMACAddress() +
                "\n- isEdge: " + isEdge();
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(!(obj instanceof Port))
            return false;
        
        Port that = (Port) obj;
        return (this.getSwitch() == null && that.getSwitch() == null || 
                this.getSwitch() != null && that.getSwitch() != null && 
                this.getSwitch()    .equals(that.getSwitch()))    && 
                this.getMACAddress().equals(that.getMACAddress()) &&
                this.getNumber() == that.getNumber();
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        
        if(getSwitch() != null)
            result.addProperty("switch", getSwitch().getDPID().toString());
        
        if(getHost() != null)
            result.addProperty("host", getHost().getID());
        
        result.addProperty("number", getNumber());
        result.add        ("mac",    getMACAddress().toJson());
        return result;
    }    
    
    public abstract String toDBMatch();
    
    /**
     * @return variable name for Neo4j query, in the form of S1P1
     */
    public String toDBVariable() {
        return getSwitch().getName() + "P" + getNumber();
    }
    
    public void createInDB(ExecutionEngine engine)
    {
        engine.execute("MERGE " + toDBMatch());
        createMapping(engine);
    }

    public void createMapping(ExecutionEngine engine) {
    }
}
