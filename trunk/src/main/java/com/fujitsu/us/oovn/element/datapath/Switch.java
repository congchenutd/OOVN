package com.fujitsu.us.oovn.element.datapath;

import java.util.HashMap;

import com.fujitsu.us.oovn.element.port.Port;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Switch
{
    protected long   _dpid;
    protected String _name;
    protected HashMap<Integer, Port> _portMap = new HashMap<Integer, Port>();
    
    public Switch(long id, String name)
    {
        _dpid = id;
        _name = name;
    }
    
    public String getName() {
        return _name;
    }
    
    public long getDPID() {
        return _dpid;
    }
    
    public String getFormattedDPID()
    {
        final StringBuilder builder = new StringBuilder();
        for(int i = 56; i >= 0; i -= 8)
        {
            if(builder.length() > 0)
                builder.append(":");
            builder.append(String.format("%02X", (_dpid >> i) & 0xF));
        }
        return builder.toString();
    }
    
    public boolean addPort(final Port port)
    {
        if(_portMap.containsKey(port.getNumber()))
            return false;
        _portMap.put(port.getNumber(), port);
        port.setSwitch(this);
        return true;
    }
    
    public Port getPort(int id) {
        return _portMap.containsKey(id) ? _portMap.get(id) 
                                        : null;
    }
    
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.addProperty("dpid", getFormattedDPID());
        result.addProperty("name", getName());
        
        JsonArray ports = new JsonArray();
        for(Port port: _portMap.values())
            ports.add(port.toJson());
        if(ports.size() > 0)
            result.add("ports", ports);
        
        return result;
    }
}
