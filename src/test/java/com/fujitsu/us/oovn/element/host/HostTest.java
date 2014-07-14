package com.fujitsu.us.oovn.element.host;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.us.oovn.core.Tenant;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;
import com.fujitsu.us.oovn.element.port.VirtualPort;

public class HostTest
{
    private Tenant tenant;
    private VNO    vno;
    private Host   host1;
    private Host   host2;
    
    @Before
    public void setUp() throws Exception
    {
        tenant = new Tenant("Carl");
        vno    = new VNO(tenant);
        host1   = new Host(1, "H1", new MACAddress("0:0:0:0:0:1"), 
                                    new VirtualIPAddress(vno, "192.168.1.2"));
        host2   = new Host(1, "H1", new MACAddress("0:0:0:0:0:1"), 
                                    new VirtualIPAddress(vno, "192.168.1.2"));
    }

    @Test
    public final void testEquals()
    {
        Assert.assertThat(host1, is (host2));
        Assert.assertThat(host1, not(new Host(2, "H1", new MACAddress("0:0:0:0:0:1"), 
                                                       new VirtualIPAddress(vno, "192.168.1.2"))));
        Assert.assertThat(host1, not(new Host(1, "H2", new MACAddress("0:0:0:0:0:1"), 
                                                       new VirtualIPAddress(vno, "192.168.1.2"))));
        Assert.assertThat(host1, not(new Host(1, "H1", new MACAddress("0:0:0:0:0:2"), 
                                                       new VirtualIPAddress(vno, "192.168.1.2"))));
        Assert.assertThat(host1, not(new Host(1, "H1", new MACAddress("0:0:0:0:0:1"), 
                                                       new VirtualIPAddress(vno, "192.168.1.3"))));

        host1.setPort(new VirtualPort(1, new MACAddress("0:0:0:0:0:2")));
        Assert.assertThat(host1, not(host2));
        
        host2.setPort(new VirtualPort(2, new MACAddress("0:0:0:0:0:3")));
        Assert.assertThat(host1, not(host2));
    }

}
