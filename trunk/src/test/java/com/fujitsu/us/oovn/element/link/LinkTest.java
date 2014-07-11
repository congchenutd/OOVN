package com.fujitsu.us.oovn.element.link;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.port.Port;

public class LinkTest
{
    private static Switch sw1;
    private static Switch sw2;    
    private static Link link;
    
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        sw1 = new Switch(new DPID(1), "S1");
        sw2 = new Switch(new DPID(2), "S2");
        sw1.addPort(new Port(1, new MACAddress("0:0:0:0:0:1")));
        sw1.addPort(new Port(2, new MACAddress("0:0:0:0:0:2")));
        sw2.addPort(new Port(1, new MACAddress("0:0:0:0:0:3")));
        sw2.addPort(new Port(2, new MACAddress("0:0:0:0:0:4")));
        link = new Link(sw1.getPort(2), sw2.getPort(1));
    }
    
    @Test
    public final void testGetSrcPort() {
        Assert.assertThat(link.getSrcPort(), is(sw1.getPort(2)));
    }

    @Test
    public final void testGetDstPort() {
        Assert.assertThat(link.getDstPort(), is(sw2.getPort(1)));
    }

    @Test
    public final void testGetSrcSwitch() {
        Assert.assertThat(link.getSrcSwitch(), is(sw1));
    }

    @Test
    public final void testGetDstSwitch() {
        Assert.assertThat(link.getDstSwitch(), is(sw2));
    }

    @Test
    public final void testGetOtherPort() {
        Assert.assertThat(link.getOtherPort(sw1.getPort(2)), is(sw2.getPort(1)));
    }

    @Test
    public final void testIsConnected() {
        Assert.assertThat(Link.isConnected(sw1.getPort(2), sw2.getPort(1)), is(true));
    }

    @Test
    public final void testGetName() {
        fail("Not yet implemented");
    }

    @Test
    public final void testToString() {
        fail("Not yet implemented");
    }

    @Test
    public final void testEqualsObject() {
        fail("Not yet implemented");
    }

}
