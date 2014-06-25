package com.fujitsu.us.oovn.element.link;

import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.port.Port;


public class Link
{
    protected Port _srcPort = null;
    protected Port _dstPort = null;
    
    public Link(final Port src, final Port dst)
    {
        _srcPort = src;
        _dstPort = dst;
    }

    public Port getSrcPort() {
        return _srcPort;
    }
    
    public Port getDstPort() {
        return _dstPort;
    }
    
    public Switch getSrcSwitch() {
        return _srcPort.getSwitch();
    }
    
    public Switch getDstSwitch() {
        return _dstPort.getSwitch();
    }

    @Override
    public String toString() {
        return  getSrcSwitch().getName() + ":" + _srcPort.getNumber() + "-" + 
                getDstSwitch().getName() + ":" + _dstPort.getNumber();
    }
    
}
