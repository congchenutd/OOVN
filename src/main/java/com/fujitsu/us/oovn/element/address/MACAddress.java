package com.fujitsu.us.oovn.element.address;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.util.HexString;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

/**
 * MAC address is a 6-byte, : separated HexString
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class MACAddress extends HexString implements Jsonable
{
    public MACAddress(String string) {
        super(string, 6, ':');
    }
    
    @Override
    public JsonElement toJson() {
        return new JsonPrimitive(toString());
    }

}