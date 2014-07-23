package com.fujitsu.us.oovn.element.address;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.fujitsu.us.oovn.core.Tenant;
import com.fujitsu.us.oovn.core.VNO;

public class IPAddressTest
{
    @Test
    public final void testToString() {
        assertThat(new IPAddress("192.168.1.2").toString(), is("192.168.1.2"));
    }

    @Test
    public final void testToInt() {
        assertThat(new IPAddress("192.168.1.2").toInt(), is(3232235778L));
    }
    
    @Test
    public final void testEquals()
    {
        assertThat(new IPAddress("192.168.1.2"), is (new IPAddress("192.168.1.2")));
        assertThat(new IPAddress("192.168.1.2"), not(new IPAddress("192.168.1.1")));
        
        // virtual ip
        VNO vno1 = new VNO(new Tenant("Tenant"));
        VNO vno2 = new VNO(new Tenant("Tenant"));
        assertThat(    new VirtualIPAddress(vno1, "192.168.1.1"),
                   not(new VirtualIPAddress(null, "192.168.1.1")));
        assertThat(    new VirtualIPAddress(vno1, "192.168.1.1"),
                   not(new VirtualIPAddress(vno2, "192.168.1.1")));
        assertThat(    new VirtualIPAddress(vno1, "192.168.1.1"),
                   is (new VirtualIPAddress(vno1, "192.168.1.1")));
    }

}
