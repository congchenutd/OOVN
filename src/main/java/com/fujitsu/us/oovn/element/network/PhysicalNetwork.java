package com.fujitsu.us.oovn.element.network;

import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.port.PhysicalPort;

public class PhysicalNetwork extends Network
{
    private static PhysicalNetwork _instance;
    
    public static PhysicalNetwork getInstance()
    {
        if (_instance == null)
            _instance = new PhysicalNetwork();
        return _instance;
    }
    
    // for testing
    private PhysicalNetwork()
    {
        PhysicalSwitch sw1 = new PhysicalSwitch(1, "s1");
        sw1.addPort(new PhysicalPort(1, new MACAddress(new byte[] {0,0,0,0,0,1})));
        sw1.addPort(new PhysicalPort(2, new MACAddress(new byte[] {0,0,0,0,0,2})));
                
        PhysicalSwitch sw2 = new PhysicalSwitch(2, "s2");
        sw2.addPort(new PhysicalPort(1, new MACAddress(new byte[] {0,0,0,0,0,3})));
        sw2.addPort(new PhysicalPort(2, new MACAddress(new byte[] {0,0,0,0,0,4})));
        
        addSwitch(sw1);
        addSwitch(sw2);
        
        addLinkPair(sw1.getPort(2), sw2.getPort(1));
    }
    
}
