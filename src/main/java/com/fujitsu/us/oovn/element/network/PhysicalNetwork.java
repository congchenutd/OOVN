package com.fujitsu.us.oovn.element.network;

import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.port.PhysicalPort;

public class PhysicalNetwork extends Network
{
    // singleton
    public static PhysicalNetwork getInstance() {
        return LazyHolder._instance;
    }
    
    private static class LazyHolder {
        private static final PhysicalNetwork _instance = new PhysicalNetwork();
    }
    
    // for testing
    private PhysicalNetwork()
    {
        PhysicalSwitch sw1 = new PhysicalSwitch(new DPID(1), "s1");
        sw1.addPort(new PhysicalPort(1, new MACAddress(new byte[] {0,0,0,0,0,1})));
        sw1.addPort(new PhysicalPort(2, new MACAddress(new byte[] {0,0,0,0,0,2})));
                
        PhysicalSwitch sw2 = new PhysicalSwitch(new DPID(2), "s2");
        sw2.addPort(new PhysicalPort(1, new MACAddress(new byte[] {0,0,0,0,0,3})));
        sw2.addPort(new PhysicalPort(2, new MACAddress(new byte[] {0,0,0,0,0,4})));
        
        addSwitch(sw1);
        addSwitch(sw2);
        
        addLinkPair(sw1.getPort(2), sw2.getPort(1));
    }
    
}
