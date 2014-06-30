package com.fujitsu.us.oovn.element.port;

import com.fujitsu.us.oovn.element.address.MACAddress;

public class PhysicalPort extends Port {

    public PhysicalPort(int number, MACAddress mac)
    {
        super(number, mac);
    }

}
