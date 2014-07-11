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

public class Link implements Jsonable
{
    protected Port _srcPort;
    protected Port _dstPort;
    
    public Link(Port src, Port dst)
    {
        _srcPort = src;
        _dstPort = dst;
        _srcPort.setLink(this);
        _dstPort.setLink(this);
    }
    
    public Port getSrcPort() {
        return _srcPort;
    }
    
    public Port getDstPort() {
        return _dstPort;
    }
    
    public Switch getSrcSwitch() {
        return _srcPort.getSwitch();
    }
    
    public Switch getDstSwitch() {
        return _dstPort.getSwitch();
    }
    
    public Port getOtherPort(Port one) {
        return one == _srcPort ? getDstPort() : getSrcPort(); 
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
        
        Link other = (Link) obj;
        return getSrcPort() == null && getDstPort() == null && 
               other.getSrcPort() == null && other.getDstPort() == null ||
               getSrcPort() != null && getDstPort() != null && 
               other.getSrcPort() != null && other.getDstPort() != null &&
               getSrcPort().equals(other.getSrcPort()) &&
               getDstPort().equals(other.getDstPort());
    }

}