package com.fujitsu.us.oovn.element.link;

import static org.hamcrest.CoreMatchers.*;

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
        Assert.assertThat(link.getOtherPort(sw2.getPort(1)), is(sw1.getPort(2)));
        Assert.assertThat(link.getOtherPort(sw1.getPort(1)), is(nullValue()));
    }

    @Test
    public final void testIsConnected() {
        Assert.assertThat(Link.isConnected(sw1.getPort(2), sw2.getPort(1)), is(true));
        Assert.assertThat(Link.isConnected(sw1.getPort(2), sw2.getPort(2)), is(false));
    }

    @Test
    public final void testGetName() {
        Assert.assertThat(link.toDBVariable(), is("S1P2S2P1"));
    }

    @Test
    public final void testToString() {
        Assert.assertThat(link.toString(), is("S1P2-S2P1"));
    }

    @Test
    public final void testEqualsObject()
    {
        Assert.assertThat(link, is (new Link(sw1.getPort(2), sw2.getPort(1))));
        Assert.assertThat(link, is (new Link(sw2.getPort(1), sw1.getPort(2))));
        Assert.assertThat(link, not(new Link(sw1.getPort(1), sw2.getPort(1))));
        Assert.assertThat(link, not(new Link(sw1.getPort(1), sw1.getPort(1))));
    }

}
