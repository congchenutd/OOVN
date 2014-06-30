package com.fujitsu.us.oovn.element.link;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.port.Port;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class Link implements Jsonable
{
    protected Port _srcPort = null;
    protected Port _dstPort = null;
    
    public Link(Port src, Port dst)
    {
        _srcPort = src;
        _dstPort = dst;
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

    @Override
    public String toString() {
        return  getSrcSwitch().getName() + ":" + _srcPort.getNumber() + "-" + 
                getDstSwitch().getName() + ":" + _dstPort.getNumber();
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.add("src port", getSrcPort().toJson());
        result.add("dst port", getDstPort().toJson());
        return result;
    }
    
}
