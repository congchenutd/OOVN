package com.fujitsu.us.oovn.element.port;

import com.fujitsu.us.oovn.element.address.MACAddress;

public class VirtualPort extends Port
{
    private PhysicalPort _physicalPort;
    
    public VirtualPort(int number, MACAddress mac) {
        super(number, mac);
    }

    public void setPhysicalPort(PhysicalPort port) {
        _physicalPort = port;
    }
    
}
