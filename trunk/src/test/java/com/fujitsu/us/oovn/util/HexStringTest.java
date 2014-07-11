package com.fujitsu.us.oovn.util;

import junit.framework.Assert;

import org.junit.Test;

public class HexStringTest
{
    private final HexString hex1 = new HexString("01:23:45:67:89:ab:cd:ef", 8, ':');
    private final HexString hex2 = new HexString(1234567890987654321L,      8, ':');
    
    @Test
    public final void testToString()
    {
        Assert.assertEquals(hex1.toString(), "01:23:45:67:89:AB:CD:EF");
        Assert.assertEquals(hex2.toString(), "11:22:10:F4:B1:6C:1C:B1");
    }

    @Test
    public final void testToInt()
    {
        Assert.assertEquals(hex1.toInt(), 81985529216486895L);
        Assert.assertEquals(hex2.toInt(), 1234567890987654321L);
    }
    
    @Test
    public final void testEquals() {
        Assert.assertTrue (hex1.equals(new HexString(81985529216486895L, 8, ':')));
        Assert.assertFalse(hex1.equals(new HexString(81985529216486895L, 4, ':')));
        Assert.assertFalse(hex1.equals(new HexString(81985529216486895L, 8, '.')));
    }
}
