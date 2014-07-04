package com.fujitsu.us.oovn.element.address;

import com.fujitsu.us.oovn.element.Jsonable;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * VirtualIPAddress = tenant id + PhysicalIPAddress
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class VirtualIPAddress extends IPAddress implements Jsonable
{
    private final int _tenantID;

    public VirtualIPAddress(int tenantID, String ipString)
    {
        super(ipString);
        _tenantID = tenantID;
    }

    public int getTenantID() {
        return _tenantID;
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.addProperty("tenant id", getTenantID());
        result.addProperty("address",   toString());
        return result;
    }
}
