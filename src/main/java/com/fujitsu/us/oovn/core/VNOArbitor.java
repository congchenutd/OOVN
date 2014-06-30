package com.fujitsu.us.oovn.core;

import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.google.gson.JsonObject;

public class VNOArbitor
{
    private static VNOArbitor _instance = null;
    
    public static VNOArbitor getInstance()
    {
        if(_instance == null)
            _instance = new VNOArbitor();
        return _instance;
    }
    
    public JsonObject getPhysicalTopology()
    {
        PhysicalNetwork pnw = PhysicalNetwork.getInstance();
        return (JsonObject) pnw.toJson();
    }
    
    public boolean verifyConfiguration(JsonObject config)
    {
        return true;
    }
    
    public boolean activateVNO(VNO vno)
    {
        return true;
    }
    
    public boolean deactiveVNO(VNO vno)
    {
        return true;
    }
    
    public boolean decommssionVNO(VNO vno)
    {
        return true;
    }
}
