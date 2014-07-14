package com.fujitsu.us.oovn.element.port;

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
public class Port implements Jsonable
{
    private int        _number;
    private MACAddress _mac;
    private Switch     _switch;
    private Link       _link;
    private Host       _host;

    public Port(int number, MACAddress mac)
    {
        setNumber(number);
        setMACAddress(mac);
    }
    
    public Switch getSwitch() {
        return _switch;
    }
    
    public void setSwitch(Switch sw) {
        _switch = sw;
    }
    
    public Host getHost() {
        return _host;
    }
    
    public void setHost(Host host) {
        _host = host;
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

    public Link getLink() {
        return _link;
    }

    public void setLink(Link link) {
        _link = link;
    }
    
    public Port getNeighbor()
    {
        Link link = getLink();
        if(link == null)
            return null;
        
        return link.getOtherPort(this);
    }

    public String getName() {
        return getSwitch().getName() + "P" + getNumber();
    }
    
    @Override
    public String toString() {
        return  "PORT:" +
                "\n- number: "      + getNumber() + 
                "\n- switch: "      + getSwitch().getName() +
                "\n- MACAddress: "  + getMACAddress() +
                "\n- isEdge: "      + isEdge();
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
                this.getSwitch().equals(that.getSwitch())) && 
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
    
    
}
