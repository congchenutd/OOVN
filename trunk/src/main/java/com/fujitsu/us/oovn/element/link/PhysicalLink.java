package com.fujitsu.us.oovn.element.link;

import java.util.HashMap;
import java.util.Map;

import com.fujitsu.us.oovn.element.Measurable;
import com.fujitsu.us.oovn.element.port.Port;

public class PhysicalLink extends Link implements Measurable
{
    private final Map<String, Object> _attributes = new HashMap<String, Object>();

    public PhysicalLink(Port src, Port dst) {
        super(src, dst);
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
