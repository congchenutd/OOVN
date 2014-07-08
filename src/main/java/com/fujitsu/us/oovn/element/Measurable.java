package com.fujitsu.us.oovn.element;

public interface Measurable
{
    void   setMeasurement(String key, Object value);
    Object getMeasurement(String key);
}
