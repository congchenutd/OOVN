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
    
    public PhysicalPort getPhysicalPort() {
        return _physicalPort;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(!super.equals(obj))
            return false;
        
        if(!(obj instanceof VirtualPort))
            return false;
        
        VirtualPort that = (VirtualPort) obj;
        return this.getPhysicalPort().equals(that.getPhysicalPort());
    }
}
