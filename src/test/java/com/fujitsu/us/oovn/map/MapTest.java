package com.fujitsu.us.oovn.map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fujitsu.us.oovn.core.Tenant;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.SingleSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.link.VirtualLink;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.network.VirtualNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.fujitsu.us.oovn.exception.InvalidVNOOperationException;
import com.fujitsu.us.oovn.map.GlobalMap;

/**
 * Test the map (GlobalMap and LocalMap)
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class MapTest
{
    private static PhysicalNetwork _pnw;
    private static VNO             _vno1;
    private static VirtualNetwork  _vnw1;
    private static VNO             _vno2;
    private static VirtualNetwork  _vnw2;
    private static GlobalMap       _globalMap;
    
    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
        /**
         * set up a physical network and 2 virtual networks
         * 1-VS1-2---------------1-VS2-2
         * |  |  |    /       \  |  |  |
         * 1-S1--2-----1-S2--2---1-S3--2
         * |  |  |  |  |  |  |
         * 1-VS3-2-----1-VS4-2
         */
        PhysicalNetwork.init("PhysicalConfig.json");
        _pnw = PhysicalNetwork.getInstance();
        _globalMap = GlobalMap.getInstance();
        
        // assuming the VNOs will be verified
        _vno1 = new VNO(new Tenant("Carl"));
        _vno1.init("VirtualConfig1.json");
        _vno1.verify();
        _vno1.start();
        _vnw1 = _vno1.getNetwork();
        
        _vno2 = new VNO(new Tenant("Carl"));
        _vno2.init("VirtualConfig2.json");
        _vno2.verify();
        _vno2.start();
        _vnw2 = _vno2.getNetwork();
    }
    
    @AfterClass
    public static void tearDownAfterClass() throws InvalidVNOOperationException
    {
//        _vno1.decommission();
//        _vno2.decommission();
    }

    @Test
    public void testGetPort()
    {
        // vno 1
        VirtualPort  vPort = _vnw1.getSwitch("a:b:c:0:0:0:0:1").getPort(1);
        PhysicalPort pPort = _pnw .getSwitch("0:0:0:0:0:0:0:1").getPort(1);
        assertThat(_globalMap.getPhysicalPort(vPort),        is(pPort));
        assertThat(_globalMap.getVirtualPort (pPort, _vno1), is(vPort));
        
        vPort = _vnw1.getSwitch("a:b:c:0:0:0:0:1").getPort(2);
        pPort = _pnw .getSwitch("0:0:0:0:0:0:0:1").getPort(2);
        assertThat(_globalMap.getPhysicalPort(vPort),        is(pPort));
        assertThat(_globalMap.getVirtualPort (pPort, _vno1), is(vPort));
        
        vPort = _vnw1.getSwitch("a:b:c:0:0:0:0:2").getPort(1);
        pPort = _pnw .getSwitch("0:0:0:0:0:0:0:3").getPort(1);
        assertThat(_globalMap.getPhysicalPort(vPort),        is(pPort));
        assertThat(_globalMap.getVirtualPort (pPort, _vno1), is(vPort));
        
        vPort = _vnw1.getSwitch("a:b:c:0:0:0:0:2").getPort(2);
        pPort = _pnw .getSwitch("0:0:0:0:0:0:0:3").getPort(2);
        assertThat(_globalMap.getPhysicalPort(vPort),        is(pPort));
        assertThat(_globalMap.getVirtualPort (pPort, _vno1), is(vPort));
        
        // vno 2
        vPort = _vnw2.getSwitch("a:b:c:0:0:0:0:3").getPort(1);
        pPort = _pnw .getSwitch("0:0:0:0:0:0:0:1").getPort(1);
        assertThat(_globalMap.getPhysicalPort(vPort),        is(pPort));
        assertThat(_globalMap.getVirtualPort (pPort, _vno2), is(vPort));
        
        vPort = _vnw2.getSwitch("a:b:c:0:0:0:0:3").getPort(2);
        pPort = _pnw .getSwitch("0:0:0:0:0:0:0:1").getPort(2);
        assertThat(_globalMap.getPhysicalPort(vPort),        is(pPort));
        assertThat(_globalMap.getVirtualPort (pPort, _vno2), is(vPort));
        
        vPort = _vnw2.getSwitch("a:b:c:0:0:0:0:4").getPort(1);
        pPort = _pnw .getSwitch("0:0:0:0:0:0:0:2").getPort(1);
        assertThat(_globalMap.getPhysicalPort(vPort),        is(pPort));
        assertThat(_globalMap.getVirtualPort (pPort, _vno2), is(vPort));
        
        vPort = _vnw2.getSwitch("a:b:c:0:0:0:0:4").getPort(2);
        pPort = _pnw .getSwitch("0:0:0:0:0:0:0:2").getPort(2);
        assertThat(_globalMap.getPhysicalPort(vPort),        is(pPort));
        assertThat(_globalMap.getVirtualPort (pPort, _vno2), is(vPort));
    }
    
    @Test
    public void testGetSwitch()
    {
        // vno 1
        SingleSwitch   vsw = (SingleSwitch) _vnw1.getSwitch("a:b:c:0:0:0:0:1");
        PhysicalSwitch psw = _pnw.getSwitch("0:0:0:0:0:0:0:1");
        assertThat(_globalMap.getPhysicalSwitches(vsw).get(0), is(psw));
        assertThat((SingleSwitch) _globalMap.getVirtualSwitch(psw, _vno1),
                           is(vsw));
        
        vsw = (SingleSwitch) _vnw1.getSwitch("a:b:c:0:0:0:0:2");
        psw = _pnw.getSwitch("0:0:0:0:0:0:0:3");
        assertThat(_globalMap.getPhysicalSwitches(vsw).get(0), is(psw));
        assertThat((SingleSwitch) _globalMap.getVirtualSwitch(psw, _vno1),
                           is(vsw));
        
        // vno 2
        vsw = (SingleSwitch) _vnw2.getSwitch("a:b:c:0:0:0:0:3");
        psw = _pnw.getSwitch("0:0:0:0:0:0:0:1");
        assertThat(_globalMap.getPhysicalSwitches(vsw).get(0), is(psw));
        assertThat((SingleSwitch) _globalMap.getVirtualSwitch(psw, _vno2),
                           is(vsw));
        
        vsw = (SingleSwitch) _vnw2.getSwitch("a:b:c:0:0:0:0:4");
        psw = _pnw.getSwitch("0:0:0:0:0:0:0:2");
        assertThat(_globalMap.getPhysicalSwitches(vsw).get(0), is(psw));
        assertThat((SingleSwitch) _globalMap.getVirtualSwitch(psw, _vno2),
                           is(vsw));
    }

    @Test
    public void testGetLink()
    {
        // vno 1
        PhysicalLink pLink1 = _pnw.getLink("0:0:0:0:0:0:0:1", 2, 
                                           "0:0:0:0:0:0:0:2", 1);
        PhysicalLink pLink2 = _pnw.getLink("0:0:0:0:0:0:0:2", 2, 
                                           "0:0:0:0:0:0:0:3", 1);
        VirtualLink vLink = _vnw1.getLink("a:b:c:0:0:0:0:1", 2, 
                                          "a:b:c:0:0:0:0:2", 1);
        List<PhysicalLink> links = _globalMap.getPhysicalLinks(vLink);
        assertThat(links.get(0), is(pLink1));
        assertThat(links.get(1), is(pLink2));
        
        assertThat(_globalMap.getVirtualLink(pLink1, _vno1), is(vLink));
        assertThat(_globalMap.getVirtualLink(pLink2, _vno1), is(vLink));
        
        // vno 2
        pLink1 = _pnw.getLink("0:0:0:0:0:0:0:1", 2, 
                              "0:0:0:0:0:0:0:2", 1);
        vLink = _vnw2.getLink("a:b:c:0:0:0:0:3", 2, 
                              "a:b:c:0:0:0:0:4", 1);
        links = _globalMap.getPhysicalLinks(vLink);
        assertThat(links.get(0), is(pLink1));
        
        assertThat(_globalMap.getVirtualLink(pLink1, _vno2), is(vLink));
    }
    
    @Test
    public void testWrongMapping() throws InvalidVNOOperationException, IOException
    {
        VNO vno3 = new VNO(new Tenant("Tenant"));

        // mapping to a non-existing physical switch
        vno3.init("WrongMappingConfig.json");
        assertThat(vno3.verify().isPassed(), is(false));
        
        // two virtual switches map to the same physical switch
        vno3.init("ConflictedMappingConfig.json");
        assertThat(vno3.verify().isPassed(), is(false));
        
        vno3.decommission();
    }
}
