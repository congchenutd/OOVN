package com.fujitsu.us.oovn.core;

public class VNOArbitor
{
    public NetworkConfiguration getPhysicalConfiguration()
    {
        return new NetworkConfiguration();
    }
    
    public boolean verifyConfiguration(NetworkConfiguration config)
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
    
    public boolean decommsionVNO(VNO vno)
    {
        return true;
    }
}
