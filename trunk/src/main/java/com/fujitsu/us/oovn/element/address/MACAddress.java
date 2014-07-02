package com.fujitsu.us.oovn.element.address;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.util.HexString;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class MACAddress extends HexString implements Jsonable
{
    public MACAddress(byte[] bytes) {
        super(bytes);
    }
    
    public MACAddress(String string) {
        super(string, ":");
    }
    
    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(toString());
    }

}