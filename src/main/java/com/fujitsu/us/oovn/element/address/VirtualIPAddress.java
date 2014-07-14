package com.fujitsu.us.oovn.element.address;

import com.fujitsu.us.oovn.core.VNO;
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
    private final VNO _vno;

    public VirtualIPAddress(VNO vno, String ipString)
    {
        super(ipString);
        _vno = vno;
    }

    public VNO getVNO() {
        return _vno;
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.addProperty("vno id", getVNO().getID());
        result.addProperty("address",   toString());
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(!(obj instanceof VirtualIPAddress))
            return false;
        
        if(!super.equals(obj))
            return false;
        
        VirtualIPAddress that = (VirtualIPAddress) obj;
        return this.getVNO().equals(that.getVNO());
    }
}
