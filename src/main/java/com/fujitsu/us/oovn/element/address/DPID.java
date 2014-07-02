package com.fujitsu.us.oovn.element.address;

import com.fujitsu.us.oovn.util.HexString;

public class DPID extends HexString
{
    private long _value;
    
    public DPID(long value)
    {
        _value = value;
    }
    
    public DPID(String string)
    {
        final String err = "dpid must be in the format of xx:xx:xx:xx:xx:xx:xx:xx";
        if (string == null) {
            throw new IllegalArgumentException(err);
        }
        
        final String[] bytes = string.split("\\:");
        if (bytes.length != 8) {
            throw new IllegalArgumentException(err);
        }

        _value = 0;
        for (int i = 0; i < 8; ++i) {
            _value |= Integer.valueOf(bytes[i], 16) << (7 - i) * 8;
        }
    }
    
    public long value() {
        return _value;
    }
    
    @Override
    /**
     * Output "00:00:00:00:00:00:00:01"
     */
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        for(int i = 56; i >= 0; i -= 8)
        {
            if(builder.length() > 0)
                builder.append(":");
            builder.append(String.format("%02X", (_value >> i) & 0xFF));
        }
        return builder.toString();
    }
    
//    public static void main(String[] argvs)
//    {
//        DPID dpid = new DPID(-12345);
//        System.out.println(dpid);
//        
//        dpid = new DPID("FF:FF:FF:FF:FF:FF:CF:C7");
//        System.out.println(dpid.value());
//    }

}
