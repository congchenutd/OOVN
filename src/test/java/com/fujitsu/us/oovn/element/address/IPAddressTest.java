package com.fujitsu.us.oovn.element.address;

import junit.framework.Assert;

import org.junit.Test;

public class IPAddressTest
{
    private final IPAddress ip = new IPAddress("192.168.1.2");
    
    @Test
    public final void testToString() {
        Assert.assertEquals(ip.toString(), "192.168.1.2");
    }

    @Test
    public final void testToInt() {
        Assert.assertEquals(ip.toInt(), 3232235778L);
    }
    
    @Test
    public final void testEquals() {
        Assert.assertTrue(ip.equals(new IPAddress("192.168.1.2")));
    }

}
