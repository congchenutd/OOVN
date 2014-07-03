package com.fujitsu.us.oovn.util;

import java.util.Arrays;

import com.fujitsu.us.oovn.element.Jsonable;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * A SectionedString is a string with multiple separated sections
 * Each section represents a byte
 * e.g., a MAC address 00:00:00:00:0A:01 or a ip address 192.168.1.2
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 * 
 */
public abstract class SectionedString implements Jsonable
{
    private final byte[] _bytes;
    private char _separator = ':';
    
    public SectionedString(long value, int length, char separator)
    {
        _bytes     = new byte[length];
        _separator = separator;
        
        for(int i = 0; i < length; ++i)
            _bytes[i] = (byte) ((value >> (length-1-i)*8) & 0xFF);
    }
    
    public SectionedString(String string, int length, char separator)
    {
        _separator = separator;
        
        if (string == null)
            throw new IllegalArgumentException("Null HexString");
        
        String s = String.valueOf(_separator);
        if(s.equals("."))
            s = "\\.";
        final String[] sections = string.split(s);
        if (sections.length != length)
            throw new IllegalArgumentException("Wrong length");

        _bytes = new byte[sections.length];
        for(int i = 0; i< sections.length; ++i)
            _bytes[i] = sectionValue(sections[i]);
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < _bytes.length; ++i)
        {
            if(i > 0)
                builder.append(_separator);
            builder.append(printSection(_bytes[i]));
        }
        return builder.toString();
    }
    
    public long toInt()
    {
        long result = 0;
        for (int i = 0; i < _bytes.length; ++i) {
            result |= (long)_bytes[i] << (_bytes.length-1-i) * 8;
        }
        return result;
    }
    
    public byte[] toBytes() {
        return Arrays.copyOf(_bytes, _bytes.length);
    }
    
    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(toString());
    }
    
    protected abstract String printSection(byte b);
    protected abstract byte   sectionValue(String section);
    
}