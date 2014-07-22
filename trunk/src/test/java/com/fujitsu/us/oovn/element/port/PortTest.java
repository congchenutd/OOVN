package com.fujitsu.us.oovn.element.port;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.link.VirtualLink;

public class PortTest
{
    private PhysicalPort pPort1;
    private PhysicalPort pPort2;
    private VirtualPort  vPort1;
    private VirtualPort  vPort2;
    
    @Before
    public void setUp()
    {
        pPort1 = new PhysicalPort(1, new MACAddress("0:0:0:0:0:1"));
        pPort2 = new PhysicalPort(1, new MACAddress("0:0:0:0:0:1"));
        vPort1 = new VirtualPort (1, new MACAddress("0:0:0:0:0:1"));
        vPort2 = new VirtualPort (1, new MACAddress("0:0:0:0:0:1"));
    }
    
    @Test
    public void testEquals()
    {
        // same mac and number
        assertThat(pPort1, is(pPort2));
        
        // same/diff switch
        PhysicalSwitch sw = new PhysicalSwitch(new DPID(1), "S1");
        pPort1.setSwitch(sw);
        assertThat(pPort1, not(pPort2));
        
        pPort2.setSwitch(sw);
        assertThat(pPort1, is(pPort2));
        
        // diff number
        pPort1.setNumber(3);
        assertThat(pPort1, not(pPort2));
        
        // virtual ports
        // same mac and number
        assertThat(vPort1, is(vPort2));
        
        // diff types
        assertThat(vPort1.equals(pPort1), not(true));
        
        // diff/same physical ports
        vPort1.setPhysicalPort(pPort1);
        assertThat(vPort1, not(vPort2));
        
        vPort2.setPhysicalPort(pPort1);
        assertThat(vPort1, is(vPort2));
    }
    
    @Test
    public void testGetNeighbor()
    {
        // no neighbor
        assertThat(pPort1.getNeighbor(), nullValue());
        
        // has neighbor
        new PhysicalLink(pPort1, pPort2);
        assertThat((PhysicalPort) pPort1.getNeighbor(), is(pPort2));
        assertThat((PhysicalPort) pPort2.getNeighbor(), is(pPort1));
        
        // no neighbor
        assertThat(vPort1.getNeighbor(), nullValue());
        
        // has neighbor
        new VirtualLink(null, vPort1, vPort2);
        assertThat((VirtualPort) vPort1.getNeighbor(), is(vPort2));
        assertThat((VirtualPort) vPort2.getNeighbor(), is(vPort1));
    }
    
}
