package com.fujitsu.us.oovn.element.address;

import com.fujitsu.us.oovn.util.HexString;

/**
 * A DPID is a 8-byte, : separated HexString
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class DPID extends HexString
{
    public DPID(long value) {
        super(value, 8, ':');
    }
    
    public DPID(String string) {
        super(string, 8, ':');
    }
}
