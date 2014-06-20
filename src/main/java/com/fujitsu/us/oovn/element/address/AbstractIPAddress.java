package com.fujitsu.us.oovn.element.address;

import org.openflow.util.U8;

public abstract class AbstractIPAddress
{
    protected int _ip;

    protected AbstractIPAddress(final String ipString) {
//        this.ip = IPv4.toIPv4Address(ipAddress);
    }

    protected AbstractIPAddress() {
    }

    public int getIp() {
        return _ip;
    }

    public void setIp(final int ip) {
        _ip = ip;
    }

    public String toSimpleString() {
        return U8.f((byte) (_ip >> 24)) + "." + (_ip >> 16 & 0xFF)
                + "." + (_ip >> 8 & 0xFF) + "." + (_ip & 0xFF);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[" 
                + (_ip >> 24) + "."
                + (_ip >> 16 & 0xFF) + "." 
                + (_ip >> 8 & 0xFF) + "."
                + (_ip & 0xFF) + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + _ip;
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        AbstractIPAddress other = (AbstractIPAddress) obj;
        return _ip == other._ip;
    }
}