package com.fujitsu.us.oovn.element.port;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.Switch;
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
    private boolean    _isEdge;
    private Switch     _switch;
    private Link       _link;

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
        return _isEdge;
    }

    public void setIsEdge(Boolean isEdge) {
        _isEdge = isEdge;
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
    public boolean equals(Object other)
    {
        if(this == other)
            return true;
        
        Port otherPort = (Port) other;
        Switch mySwitch   = getSwitch();
        Switch yourSwitch = otherPort.getSwitch();
        return  mySwitch == null && yourSwitch == null || 
                mySwitch != null && yourSwitch != null && 
                mySwitch.equals(yourSwitch) && 
                getMACAddress().equals(otherPort.getMACAddress()) &&
                getNumber() == otherPort.getNumber();
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.addProperty("switch", getSwitch().getDPID().toString());
        result.addProperty("number", getNumber());
        result.addProperty("edge",   isEdge());
        result.add        ("mac",    getMACAddress().toJson());
        return result;
    }
    
    
}
