package com.fujitsu.us.oovn.element.port;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.link.Link;

public class PortTest
{
    private Port port1;
    private Port port2;
    
    @Before
    public void setUp()
    {
        port1 = new Port(1, new MACAddress("0:0:0:0:0:1"));
        port2 = new Port(1, new MACAddress("0:0:0:0:0:1"));
    }
    
    @Test
    public void testEquals()
    {
        Assert.assertThat(port1, is(port2));
        
        Switch sw = new Switch(new DPID(1), "S1");
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
        new Link(port1, port2);
        Assert.assertThat(port1.getNeighbor(), is(port2));
        Assert.assertThat(port2.getNeighbor(), is(port1));
    }
    
}
