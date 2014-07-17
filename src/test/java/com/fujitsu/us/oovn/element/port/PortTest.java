package com.fujitsu.us.oovn.element.port;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;

public class PortTest
{
    private PhysicalPort port1;
    private PhysicalPort port2;
    
    @Before
    public void setUp()
    {
        port1 = new PhysicalPort(1, new MACAddress("0:0:0:0:0:1"));
        port2 = new PhysicalPort(1, new MACAddress("0:0:0:0:0:1"));
    }
    
    @Test
    public void testEquals()
    {
        Assert.assertThat(port1, is(port2));
        
        PhysicalSwitch sw = new PhysicalSwitch(new DPID(1), "S1");
        port1.setSwitch(sw);
        Assert.assertThat(port1, not(port2));
        
        port2.setSwitch(sw);
        Assert.assertThat(port1, is(port2));
        
        port1.setNumber(3);
        Assert.assertThat(port1, not(port2));
    }
    
    @Test
    public void testGetNeighbor()
    {
        Assert.assertThat(port1.getNeighbor(), nullValue());
        new PhysicalLink(port1, port2);
        Assert.assertThat((PhysicalPort) port1.getNeighbor(), is(port2));
        Assert.assertThat((PhysicalPort) port2.getNeighbor(), is(port1));
    }
    
}
