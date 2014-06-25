package com.fujitsu.us.oovn.element.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fujitsu.us.oovn.element.address.IPAddress;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;

public class Host
{

    private static Logger     _log = LoggerFactory.getLogger(Host.class);
    private final  int        _id;
    private final  MACAddress _mac;
//    private final  OVXPort port;
    private final IPAddress _ip = new VirtualIPAddress(0, "0.0.0.0");
    
    public Host(final Integer id, final MACAddress mac)
    {
        _id  = id;
        _mac = mac;
//        this.port = port;
    }
    
    public int getID() {
        return _id;
    }
}
