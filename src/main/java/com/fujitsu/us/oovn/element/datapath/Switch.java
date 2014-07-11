package com.fujitsu.us.oovn.element.datapath;

import java.util.HashMap;
import java.util.Map;

import com.fujitsu.us.oovn.element.Jsonable;
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
public class Switch implements Jsonable
{
    protected DPID   _dpid;
    protected String _name;
    protected Map<Integer, Port> _ports;  // port number -> port
    
    public Switch(DPID dpid, String name)
    {
        _dpid = dpid;
        _name = name;
        _ports = new HashMap<Integer, Port>();
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
        if(_ports.containsKey(port.getNumber()))
            return false;
        _ports.put(port.getNumber(), port);
        port.setSwitch(this);
        return true;
    }
    
    public Port getPort(int id) {
        return _ports.containsKey(id) ? _ports.get(id) 
                                        : null;
    }
    
    public Map<Integer, Port> getPorts() {
        return _ports;
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.addProperty("dpid", getDPID().toString());
        result.addProperty("name", getName());
        
        if(_ports.size() > 0)
        {
            JsonArray portsJson = new JsonArray();
            for(Port port: _ports.values())
                portsJson.add(port.toJson());
            result.add("ports", portsJson);
        }
        
        return result;
    }
    
}
