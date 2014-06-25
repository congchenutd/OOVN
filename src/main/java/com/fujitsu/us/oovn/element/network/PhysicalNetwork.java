package com.fujitsu.us.oovn.element.network;



public class PhysicalNetwork extends Network
{

    private static PhysicalNetwork _instance;
    
    public static PhysicalNetwork getInstance()
    {
        if (_instance == null)
            _instance = new PhysicalNetwork();
        return _instance;
    }
    
    
}
