package com.fujitsu.us.oovn.element.link;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

import com.fujitsu.us.oovn.core.Tenant;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.SingleSwitch;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;

public class LinkTest
{
    private static PhysicalSwitch _psw1;
    private static PhysicalSwitch _psw2;    
    private static PhysicalLink   _pLink;
    
    private static SingleSwitch _vsw1;
    private static SingleSwitch _vsw2;    
    private static VirtualLink  _vLink;
    
    /**
     * Build a physical and a virtual link
     * 1-vsw1-2---(vLink)---1-vsw2-2
     * |  |   |      |      |  |   |
     * 1-psw1-2---(pLink)---1-psw2-2
     */
    @BeforeClass
    public static void setUpClass() throws Exception
    {
        _psw1 = new PhysicalSwitch(new DPID(1), "S1");
        _psw2 = new PhysicalSwitch(new DPID(2), "S2");
        _psw1.addPort(new PhysicalPort(1, new MACAddress("0:0:0:0:0:1")));
        _psw1.addPort(new PhysicalPort(2, new MACAddress("0:0:0:0:0:2")));
        _psw2.addPort(new PhysicalPort(1, new MACAddress("0:0:0:0:0:3")));
        _psw2.addPort(new PhysicalPort(2, new MACAddress("0:0:0:0:0:4")));
        _pLink = new PhysicalLink(_psw1.getPort(2), _psw2.getPort(1));
        
        _vsw1 = new SingleSwitch(null, new DPID(1), "VS1");
        _vsw2 = new SingleSwitch(null, new DPID(2), "VS2");
        VirtualPort vPort11 = new VirtualPort(1, new MACAddress("0:0:0:0:0:1"));
        VirtualPort vPort12 = new VirtualPort(2, new MACAddress("0:0:0:0:0:2"));
        VirtualPort vPort21 = new VirtualPort(1, new MACAddress("0:0:0:0:0:3"));
        VirtualPort vPort22 = new VirtualPort(2, new MACAddress("0:0:0:0:0:4"));
        _vsw1.addPort(vPort11);
        _vsw1.addPort(vPort12);
        _vsw2.addPort(vPort21);
        _vsw2.addPort(vPort22);
        
        vPort11.setPhysicalPort(_psw1.getPort(1));
        vPort12.setPhysicalPort(_psw1.getPort(2));
        vPort21.setPhysicalPort(_psw2.getPort(1));
        vPort22.setPhysicalPort(_psw2.getPort(2));
        _vsw1.setPhysicalSwitch(_psw1);
        _vsw2.setPhysicalSwitch(_psw2);
        
        _vLink = new VirtualLink(null, _vsw1.getPort(2), _vsw2.getPort(1));
    }
    
    @Test
    public final void testGetSrcPort() {
        assertThat(_pLink.getSrcPort(), is(_psw1.getPort(2)));
        assertThat(_vLink.getSrcPort(), is(_vsw1.getPort(2)));
    }

    @Test
    public final void testGetDstPort() {
        assertThat(_pLink.getDstPort(), is(_psw2.getPort(1)));
        assertThat(_vLink.getDstPort(), is(_vsw2.getPort(1)));
    }

    @Test
    public final void testGetSrcSwitch() {
        assertThat(_pLink.getSrcSwitch(), is(_psw1));
        assertThat((SingleSwitch) _vLink.getSrcSwitch(), is(_vsw1));
    }

    @Test
    public final void testGetDstSwitch() {
        assertThat(_pLink.getDstSwitch(), is(_psw2));
        assertThat((SingleSwitch) _vLink.getDstSwitch(), is(_vsw2));
    }

    @Test
    public final void testGetOtherPort()
    {
        assertThat(_pLink.getOtherPort(_psw1.getPort(2)), is(_psw2.getPort(1)));
        assertThat(_pLink.getOtherPort(_psw2.getPort(1)), is(_psw1.getPort(2)));
        assertThat(_pLink.getOtherPort(_psw1.getPort(1)), is(nullValue()));
        
        assertThat(_vLink.getOtherPort(_vsw1.getPort(2)), is(_vsw2.getPort(1)));
        assertThat(_vLink.getOtherPort(_vsw2.getPort(1)), is(_vsw1.getPort(2)));
        assertThat(_vLink.getOtherPort(_vsw1.getPort(1)), is(nullValue()));
    }

    @Test
    public final void testIsConnected()
    {
        assertThat(Link.isConnected(_psw1.getPort(2), _psw2.getPort(1)), is(true));
        assertThat(Link.isConnected(_psw1.getPort(2), _psw2.getPort(2)), is(false));
        
        assertThat(Link.isConnected(_vsw1.getPort(2), _vsw2.getPort(1)), is(true));
        assertThat(Link.isConnected(_vsw1.getPort(2), _vsw2.getPort(2)), is(false));
    }

    @Test
    public final void testEquals()
    {
        assertThat(_pLink, is (new PhysicalLink(_psw1.getPort(2), _psw2.getPort(1))));
        assertThat(_pLink, is (new PhysicalLink(_psw2.getPort(1), _psw1.getPort(2))));
        assertThat(_pLink, not(new PhysicalLink(_psw1.getPort(1), _psw2.getPort(1))));
        assertThat(_pLink, not(new PhysicalLink(_psw1.getPort(1), _psw1.getPort(1))));
        
        assertThat(_vLink, is (new VirtualLink(null, _vsw1.getPort(2), _vsw2.getPort(1))));
        assertThat(_vLink, is (new VirtualLink(null, _vsw2.getPort(1), _vsw1.getPort(2))));
        assertThat(_vLink, not(new VirtualLink(null, _vsw1.getPort(1), _vsw2.getPort(1))));
        assertThat(_vLink, not(new VirtualLink(null, _vsw1.getPort(1), _vsw1.getPort(1))));
        
        // same VNO
        VNO vno = new VNO(new Tenant("Tenant"));
        assertThat(_vLink, not(new VirtualLink(vno, _vsw1.getPort(2), _vsw2.getPort(1))));
        
        // same path
        _vLink.setPath(new LinkedList<PhysicalLink>() {{add(_pLink);}});
        assertThat(_vLink, not(new VirtualLink(null, _vsw1.getPort(2), _vsw2.getPort(1))));
    }

}
