package com.fujitsu.us.oovn.element.address;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

public class IPAddressTest
{
    private IPAddress _ip;
    
    @Before
    public void setUp() {
        _ip = new IPAddress("192.168.1.2");
    }
    
    @Test
    public final void testToString() {
        assertThat(_ip.toString(), is("192.168.1.2"));
    }

    @Test
    public final void testToInt() {
        assertThat(_ip.toInt(), is(3232235778L));
    }
    
    @Test
    public final void testEquals() {
        assertThat(_ip, is (new IPAddress("192.168.1.2")));
        assertThat(_ip, not(new IPAddress("192.168.1.1")));
    }

}
