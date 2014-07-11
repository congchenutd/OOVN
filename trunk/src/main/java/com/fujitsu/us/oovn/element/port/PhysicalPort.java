package com.fujitsu.us.oovn.element.port;

import java.util.HashMap;
import java.util.Map;

import com.fujitsu.us.oovn.element.Measurable;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.MACAddress;

public class PhysicalPort extends Port implements Measurable, Persistable
{
    private final Map<String, Object> _attributes = new HashMap<String, Object>();
    
    public PhysicalPort(int number, MACAddress mac)
    {
        super(number, mac);
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
    public String toDBCreate() {
        return  "(" + getName() +
                ":Physical:Port {" +
                "switch:\"" + getSwitch().getDPID().toString() + "\"," +
                "number:" + getNumber() + "," +
                "mac:\""  + getMACAddress() + "\"})";
    }
    
    @Override
    public String toDBMatch() {
        return  getName() +
                ":Physical:Port {" +
                "switch:\"" + getSwitch().getDPID().toString() + "\"," + 
                "number:" + getNumber() + "}";
    }

}
