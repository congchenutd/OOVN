package com.fujitsu.us.oovn.map;

import static org.hamcrest.CoreMatchers.is;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fujitsu.us.oovn.core.Tenant;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;

public class GlobalMapTest
{
    private static VNO _vno;
    
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
    }

    @Test
    public final void testGetPort()
    {
        VirtualPort  vPort = (VirtualPort) _vno.getNetwork()
                                               .getSwitch(new DPID("a:b:c:0:0:0:0:1"))
                                               .getPort(1);
        PhysicalPort pPort = (PhysicalPort) PhysicalNetwork.getInstance()
                                                           .getSwitch(new DPID("0:0:0:0:0:0:0:1"))
                                                           .getPort(1);
        Assert.assertThat(GlobalMap.getInstance().getPhysicalPort(vPort),       is(pPort));
        Assert.assertThat(GlobalMap.getInstance().getVirtualPort (pPort, _vno), is(vPort));
        
        vPort = (VirtualPort) _vno.getNetwork()
                                  .getSwitch(new DPID("a:b:c:0:0:0:0:1"))
                                  .getPort(2);
        pPort = (PhysicalPort) PhysicalNetwork.getInstance()
                                              .getSwitch(new DPID("0:0:0:0:0:0:0:1"))
                                              .getPort(2);
        Assert.assertThat(GlobalMap.getInstance().getPhysicalPort(vPort), is(pPort));
        Assert.assertThat(GlobalMap.getInstance().getVirtualPort (pPort, _vno), is(vPort));
        
        vPort = (VirtualPort) _vno.getNetwork()
                                  .getSwitch(new DPID("a:b:c:0:0:0:0:2"))
                                  .getPort(1);
        pPort = (PhysicalPort) PhysicalNetwork.getInstance()
                                              .getSwitch(new DPID("0:0:0:0:0:0:0:3"))
                                              .getPort(1);
        Assert.assertThat(GlobalMap.getInstance().getPhysicalPort(vPort), is(pPort));
        Assert.assertThat(GlobalMap.getInstance().getVirtualPort (pPort, _vno), is(vPort));
        
        vPort = (VirtualPort) _vno.getNetwork()
                                  .getSwitch(new DPID("a:b:c:0:0:0:0:2"))
                                  .getPort(2);
        pPort = (PhysicalPort) PhysicalNetwork.getInstance()
                                              .getSwitch(new DPID("0:0:0:0:0:0:0:3"))
                                              .getPort(2);
        Assert.assertThat(GlobalMap.getInstance().getPhysicalPort(vPort), is(pPort));
        Assert.assertThat(GlobalMap.getInstance().getVirtualPort (pPort, _vno), is(vPort));
    }

}
