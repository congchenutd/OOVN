package com.fujitsu.us.oovn.element.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fujitsu.us.oovn.element.address.AbstractIPAddress;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;

public class Host
{

    private static Logger     _log = LoggerFactory.getLogger(Host.class);
    private final  Integer    _id;
    private final  MACAddress _mac;
//    private final  OVXPort port;
    private final AbstractIPAddress _ip = new VirtualIPAddress(0, 0);
    
    public Host(final Integer id, final MACAddress mac)
    {
        _id  = id;
        _mac = mac;
//        this.port = port;
    }
}
