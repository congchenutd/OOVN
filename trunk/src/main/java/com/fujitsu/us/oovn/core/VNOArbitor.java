package com.fujitsu.us.oovn.core;

import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.network.VirtualNetwork;
import com.google.gson.JsonObject;

public class VNOArbitor
{
    // singleton
    public static VNOArbitor getInstance() {
        return LazyHolder._instance;
    }
    
    private static class LazyHolder {
        private static final VNOArbitor _instance = new VNOArbitor();
    }
    
    private VNOArbitor() {}
    
    public NetworkConfiguration getPhysicalTopology()
    {
        PhysicalNetwork pnw = PhysicalNetwork.getInstance();
        return new NetworkConfiguration((JsonObject) pnw.toJson());
    }
    
    public boolean verifyConfiguration(NetworkConfiguration config)
    {
        return true;
    }
    
    public boolean activateVNO(VNO vno)
    {
        // build a VirtualNetwork and assign it to VNO
        VirtualNetwork vn = NetworkBuilder.getInstance().build(vno);
        
        System.out.println(vn.toJson());
        
        vno.setNetwork(vn);
        VNOPool.getInstance().registerVNO(vno);
        return true;
    }
    
    public boolean deactiveVNO(VNO vno)
    {
        return true;
    }
    
    public boolean decommssionVNO(VNO vno)
    {
        VNOPool.getInstance().unregisterVNO(vno);
        return true;
    }
}
