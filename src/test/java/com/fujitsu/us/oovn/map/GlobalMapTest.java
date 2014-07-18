package com.fujitsu.us.oovn.map;

import static org.hamcrest.CoreMatchers.is;

import java.util.List;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fujitsu.us.oovn.core.Tenant;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.SingleSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.link.VirtualLink;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.network.VirtualNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.fujitsu.us.oovn.map.GlobalMap;

public class GlobalMapTest
{
    private static VNO             _vno;
    private static GlobalMap       _map;
    private static VirtualNetwork  _vnw;
    private static PhysicalNetwork _pnw;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        /**
         * set up a physical network and a virtual network
         * 1-VS1-2-------------1-VS2-2
         * |  |  |             |  |  |
         * 1-S1--2----1-S2-2---1-S3--2
         */
        _vno = new VNO(new Tenant("Carl"));
        _vno.init("VirtualConfig.json");
        _vno.verify();
        _vno.activate();
        _map = GlobalMap.getInstance();
        _vnw = _vno.getNetwork();
        _pnw = PhysicalNetwork.getInstance();
    }

    @Test
    public final void testGetPort()
    {
        VirtualPort  vPort = (VirtualPort) _vnw.getSwitch(new DPID("a:b:c:0:0:0:0:1"))
                                               .getPort(1);
        PhysicalPort pPort = (PhysicalPort) _pnw.getSwitch(new DPID("0:0:0:0:0:0:0:1"))
                                                .getPort(1);
        Assert.assertThat(_map.getPhysicalPort(vPort),       is(pPort));
        Assert.assertThat(_map.getVirtualPort (pPort, _vno), is(vPort));
        
        vPort = (VirtualPort) _vnw.getSwitch(new DPID("a:b:c:0:0:0:0:1"))
                                  .getPort(2);
        pPort = (PhysicalPort) _pnw.getSwitch(new DPID("0:0:0:0:0:0:0:1"))
                                   .getPort(2);
        Assert.assertThat(_map.getPhysicalPort(vPort),       is(pPort));
        Assert.assertThat(_map.getVirtualPort (pPort, _vno), is(vPort));
        
        vPort = (VirtualPort) _vnw.getSwitch(new DPID("a:b:c:0:0:0:0:2"))
                                  .getPort(1);
        pPort = (PhysicalPort) _pnw.getSwitch(new DPID("0:0:0:0:0:0:0:3"))
                                   .getPort(1);
        Assert.assertThat(_map.getPhysicalPort(vPort),       is(pPort));
        Assert.assertThat(_map.getVirtualPort (pPort, _vno), is(vPort));
        
        vPort = (VirtualPort) _vnw.getSwitch(new DPID("a:b:c:0:0:0:0:2"))
                                  .getPort(2);
        pPort = (PhysicalPort) _pnw.getSwitch(new DPID("0:0:0:0:0:0:0:3"))
                                   .getPort(2);
        Assert.assertThat(_map.getPhysicalPort(vPort),       is(pPort));
        Assert.assertThat(_map.getVirtualPort (pPort, _vno), is(vPort));
    }
    
    @Test
    public final void testGetSwitch()
    {
        SingleSwitch   vsw = (SingleSwitch)   _vnw.getSwitch(new DPID("a:b:c:0:0:0:0:1"));
        PhysicalSwitch psw = (PhysicalSwitch) _pnw.getSwitch(new DPID("0:0:0:0:0:0:0:1"));
        Assert.assertThat(_map.getPhysicalSwitches(vsw).get(0), is(psw));
        Assert.assertThat((SingleSwitch) _map.getVirtualSwitch(psw, _vno),
                           is(vsw));
        
        vsw = (SingleSwitch)   _vnw.getSwitch(new DPID("a:b:c:0:0:0:0:2"));
        psw = (PhysicalSwitch) _pnw.getSwitch(new DPID("0:0:0:0:0:0:0:3"));
        Assert.assertThat(_map.getPhysicalSwitches(vsw).get(0), is(psw));
        Assert.assertThat((SingleSwitch) _map.getVirtualSwitch(psw, _vno),
                           is(vsw));
    }

    @Test
    public final void testGetLink()
    {
        PhysicalLink pLink1 = _pnw.getLink(new DPID("0:0:0:0:0:0:0:1"), 2, 
                                           new DPID("0:0:0:0:0:0:0:2"), 1);
        PhysicalLink pLink2 = _pnw.getLink(new DPID("0:0:0:0:0:0:0:2"), 2, 
                                           new DPID("0:0:0:0:0:0:0:3"), 1);
        VirtualLink vLink = _vnw.getLink(new DPID("a:b:c:0:0:0:0:1"), 2, 
                                         new DPID("a:b:c:0:0:0:0:2"), 1);
        List<PhysicalLink> links = _map.getPhysicalLinks(vLink);
        Assert.assertThat(links.get(0), is(pLink1));
        Assert.assertThat(links.get(1), is(pLink2));
        
        Assert.assertThat(_map.getVirtualLink(pLink1, _vno), is(vLink));
        Assert.assertThat(_map.getVirtualLink(pLink2, _vno), is(vLink));
    }
}
