package com.fujitsu.us.oovn.element.address;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IPAddressTest
{
    private IPAddress ip;
    
    @Before
    public void setUp()
    {
        ip = new IPAddress("192.168.1.2");
    }
    
    @Test
    public final void testToString() {
        Assert.assertThat(ip.toString(), is("192.168.1.2"));
    }

    @Test
    public final void testToInt() {
        Assert.assertThat(ip.toInt(), is(3232235778L));
    }
    
    @Test
    public final void testEquals() {
        Assert.assertThat(ip, is (new IPAddress("192.168.1.2")));
        Assert.assertThat(ip, not(new IPAddress("192.168.1.1")));
    }

}
