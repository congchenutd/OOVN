package com.fujitsu.us.oovn.element.address;

import org.openflow.util.U8;


public class IPAddress
{
    protected int _ip;

    public IPAddress(final String ipString) {
        setIP(ipString);
    }

    public int getIp() {
        return _ip;
    }

    public void setIp(final int ip) {
        _ip = ip;
    }
    
    public void setIP(final String ipString)
    {
        _ip = 0;
        if (ipString == null) {
            throw new IllegalArgumentException("IPv4 address must be in the format of x.x.x.x");
        }
        
        final String[] octets = ipString.split("\\.");
        if (octets.length != 4) {
            throw new IllegalArgumentException("IPv4 address must be in the format of x.x.x.x");
        }

        for (int i = 0; i < 4; ++i) {
            _ip |= Integer.valueOf(octets[i]) << (3 - i) * 8;
        }
    }

    @Override
    public String toString() {
        return U8.f((byte)  (_ip >> 24))       + "." + 
                            (_ip >> 16 & 0xFF) + "." + 
                            (_ip >> 8  & 0xFF) + "." + 
                            (_ip & 0xFF);
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
        IPAddress other = (IPAddress) obj;
        return _ip == other._ip;
    }
}