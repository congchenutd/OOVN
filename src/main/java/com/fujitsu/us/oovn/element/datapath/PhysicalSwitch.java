package com.fujitsu.us.oovn.element.datapath;

import java.util.HashMap;
import java.util.Map;

import com.fujitsu.us.oovn.element.Measurable;
import com.fujitsu.us.oovn.element.address.DPID;

public class PhysicalSwitch extends Switch implements Measurable
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

}
