package com.fujitsu.us.oovn.element.datapath;

import java.util.HashMap;
import java.util.Map;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.port.Port;
import com.fujitsu.us.oovn.exception.InvalidPortNumberException;
import com.fujitsu.us.oovn.map.MapBase;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Base class for all virtual and physical switches
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
@SuppressWarnings("rawtypes")
public abstract class Switch<PortType extends Port> implements Jsonable
{
    protected final DPID   _dpid;
    protected final String _name;
    protected final Map<Integer, PortType> _ports;  // port number -> port
    
    public Switch(DPID dpid, String name)
    {
        _dpid = dpid;
        _name = name;
        _ports = new HashMap<Integer, PortType>();
    }
    
    public String getName() {
        return _name;
    }
    
    public DPID getDPID() {
        return _dpid;
    }

    public String toDBVariable() {
        return getName();
    }
    
    /**
     * Attach a port to the switch
     * @return true if successful
     */
    @SuppressWarnings("unchecked")
    public void addPort(PortType port)
    {
        if(port != null)
        {
            _ports.put(port.getNumber(), port);
            port.setSwitch(this);
        }
    }
    
    public PortType getPort(int id) throws InvalidPortNumberException
    {
        if(!_ports.containsKey(id))
            throw new InvalidPortNumberException("Switch with dpid" + getDPID() + 
                                                 " has no such a port numbered " + id);
        return _ports.get(id);
    }
    
    public Map<Integer, PortType> getPorts() {
        return _ports;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("dpid: " + getDPID() + "\n" +
                       "name: " + getName() + "\n");
        if(_ports.size() > 0)
        {
            builder.append("ports:\n");
            for(Port port: _ports.values())
                builder.append(port.toString() + "\n");
        }
        return builder.toString();
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
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(!(obj instanceof Switch))
            return false;
        
        Switch that = (Switch) obj;
        return  this.getDPID() .equals(that.getDPID()) &&
                this.getName() .equals(that.getName());        
    }
    
    public void createInDB(MapBase map)
    {
        // create the switch itself
        map.query("MERGE " + toDBMatch());
        
        // create and connect ports
        for(PortType port: getPorts().values())
        {
            port.createInDB(map);
            map.query(
                    "MATCH \n" +
                    toDBMatch() + ",\n" +
                    port.toDBMatch() + "\n" +
                    "CREATE (" + toDBVariable() + ")-[:Has]->(" + port.toDBVariable() + ")");
        }
        
        // create the mapping
        createMapping(map);
    }
    
    // empty by default
    public void createMapping(MapBase map) {
    }
    
    public abstract String toDBMatch();
    
}
