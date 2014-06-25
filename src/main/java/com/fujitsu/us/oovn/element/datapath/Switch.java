package com.fujitsu.us.oovn.element.datapath;

import java.util.HashMap;

import com.fujitsu.us.oovn.element.port.Port;

public class Switch
{
    protected long   _id;
    protected String _name;
    protected HashMap<Integer, Port> _portMap = new HashMap<Integer, Port>();
    
    public Switch(long id, final String name)
    {
        _id   = id;
        _name = name;
    }
    
    public String getName() {
        return _name;
    }
    
    public long getID() {
        return _id;
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
    
}
