package com.fujitsu.us.oovn.element.link;

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
public class Link<SwitchType extends Switch, PortType extends Port> implements Jsonable
{
    protected PortType _srcPort;
    protected PortType _dstPort;
    
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
    
    public SwitchType getSrcSwitch() {
        return (SwitchType) _srcPort.getSwitch();
    }
    
    public SwitchType getDstSwitch() {
        return (SwitchType) _dstPort.getSwitch();
    }
    
    public PortType getOtherPort(PortType port) {
        return port.equals(getSrcPort()) ? getDstPort() 
                                         : port.equals(getDstPort()) ? getSrcPort() 
                                                                     : null; 
    }
    
    public static boolean isConnected(Port srcPort, Port dstPort)
    {
        Link link = srcPort.getLink();
        return link.getOtherPort(srcPort) == dstPort; 
    }

    public String getName() {
        return getSrcPort().getName() + getDstPort().getName();
    }
    
    @Override
    public String toString() {
        return  getSrcPort().getName() + "-" + getDstPort().getName();                
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

}