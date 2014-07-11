package com.fujitsu.us.oovn.element.datapath;

import java.util.HashMap;
import java.util.Map;

import com.fujitsu.us.oovn.element.Measurable;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.Port;

public class PhysicalSwitch extends Switch implements Measurable, Persistable
{
    private final Map<String, Object> _attributes = new HashMap<String, Object>();

    public PhysicalSwitch(DPID dpid, String name)
    {
        super(dpid, name);
    }

    @Override
    public void setMeasurement(String key, Object value) {
        _attributes.put(key, value);
    }

    @Override
    public Object getMeasurement(String key) {
        return _attributes.get(key);
    }

    @Override
    public String toDBCreate()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("(" + toDBMatch() + ")");
        
        for(Port port: getPorts().values())
        {
            builder.append(",\n");
            builder.append(((PhysicalPort) port).toDBCreate() + ",\n");
            builder.append("(" + getName() + ")-[:Has]->(" + port.getName() + ")");
        }
        
        return builder.toString();
    }
    
    @Override
    public String toDBMatch() {
        return  getName() +
                ":Physical:Switch {" +
                "dpid:\"" + getDPID().toString() + "\"," +
                "name:\"" + getName() + "\"}";
    }
    
}
