package com.fujitsu.us.oovn.util;

import java.util.Arrays;

/**
 * A HexString is a string with multiple separated sections
 * Each section represents a byte in hex
 * e.g., a MAC address 00:00:00:00:0A:01
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 * 
 */
public class HexString
{
    private final byte[] _bytes;
    private String _separator = ":";
    
    public HexString(long value, int length, String separator)
    {
        _bytes     = new byte[length];
        _separator = separator;
        
        for(int i = 0; i < length; ++i)
            _bytes[i] = (byte) ((value >> (length-1-i)*8) & 0xFF);
    }
    
    public HexString(String string, String separator)
    {
        _separator = separator;
        
        if (string == null)
            throw new IllegalArgumentException("Null HexString");
        
        final String[] sections = string.split(_separator);
        if (sections.length == 0)
            throw new IllegalArgumentException("No separators found in the HexString");

        _bytes = new byte[sections.length];
        for(int i = 0; i< sections.length; ++i)
            _bytes[i] = Integer.valueOf(sections[i], 16).byteValue();
    }
    
    public HexString(byte[] bytes) {
        _bytes = bytes;
    }
    
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < _bytes.length; ++i)
        {
            if(i > 0)
                builder.append(_separator);
            builder.append(String.format("%02X", _bytes[i]));
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
    
}
