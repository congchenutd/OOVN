package com.fujitsu.us.oovn.element.network;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.IPAddress;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class VirtualNetwork extends Network
{
    private final VNO       _vno;
    private final IPAddress _networkIP;
    private final int       _mask;

    public VirtualNetwork(final VNO vno, final IPAddress ip, int mask)
    {
        _vno       = vno;
        _networkIP = ip;
        _mask      = mask;
    }
    
    public int getID() {
        return _vno.getID();
    }
    
    public int getTenantID() {
        return _vno.getTenant().getID();
    }
    
    public IPAddress getNetworkAddress() {
        return _networkIP;
    }
    
    public int getMask() {
        return _mask;
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.add(        "ip address", getNetworkAddress().toJson());
        result.addProperty("mask",       getMask());
        result.add(        "network",    super.toJson());
        return result;
    }
    
}


