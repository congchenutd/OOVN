package com.fujitsu.us.oovn.element.address;

import com.fujitsu.us.oovn.util.SectionedString;


public class IPAddress extends SectionedString
{
    public IPAddress(String ipString) {
        super(ipString, 4, '.');
    }

//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + _ip;
//        return result;
//    }
//
//    @Override
//    public boolean equals(Object obj)
//    {
//        if (this == obj)
//            return true;
//        if (obj == null)
//            return false;
//        IPAddress other = (IPAddress) obj;
//        return _ip == other._ip;
//    }
    
    @Override
    protected String printSection(byte b) {
        return String.format("%d", b & 0xFF);
    }
    
    @Override
    protected byte sectionValue(String section) {
        return Integer.valueOf(section, 10).byteValue();
    }
    
}