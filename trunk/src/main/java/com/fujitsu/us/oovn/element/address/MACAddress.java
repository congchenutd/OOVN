package com.fujitsu.us.oovn.element.address;

import java.util.Arrays;

import com.fujitsu.us.oovn.element.Jsonable;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class MACAddress implements Jsonable
{
    /**
     * MAC address is always 6 bytes long
     */
    public static final int MAC_ADDRESS_LEN = 6;
    private byte[] _address = new byte[MAC_ADDRESS_LEN];
    
    public MACAddress(final byte[] address) {
        _address = Arrays.copyOf(address, MAC_ADDRESS_LEN);
    }
    
    /**
     * @return a COPY of the address
     */
    public byte[] toBytes() {
        return Arrays.copyOf(_address, MAC_ADDRESS_LEN);
    }
    
    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        for (final byte b : _address)
        {
            if (builder.length() > 0)
                builder.append(":");
            builder.append(String.format("%02X", b & 0xFF));
        }
        return builder.toString();
    }
    
    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(toString());
    }

}
