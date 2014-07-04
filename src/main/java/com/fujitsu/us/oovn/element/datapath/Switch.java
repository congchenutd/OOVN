package com.fujitsu.us.oovn.element.datapath;

import java.util.HashMap;

import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.port.Port;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Base class for all virtual and physical switches
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class Switch
{
    protected DPID   _dpid;
    protected String _name;
    protected HashMap<Integer, Port> _portMap;  // port number -> port
    
    public Switch(DPID dpid, String name)
    {
        _dpid = dpid;
        _name = name;
        _portMap = new HashMap<Integer, Port>();
    }
    
    public String getName() {
        return _name;
    }
    
    public DPID getDPID() {
        return _dpid;
    }

    /**
     * Attach a port to the switch
     * @return true if successful
     */
    public boolean addPort(Port port)
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
        result.addProperty("dpid", getDPID().toString());
        result.addProperty("name", getName());
        
        if(_portMap.size() > 0)
        {
            JsonArray portsJson = new JsonArray();
            for(Port port: _portMap.values())
                portsJson.add(port.toJson());
            result.add("ports", portsJson);
        }
        
        return result;
    }
}
