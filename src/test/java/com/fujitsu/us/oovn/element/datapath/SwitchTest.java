package com.fujitsu.us.oovn.element.datapath;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.assertThat;
import org.junit.Test;

import com.fujitsu.us.oovn.core.Tenant;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;

public class SwitchTest
{
    @Test
    public final void testEquals()
    {
        // physical switches
        assertThat(    new PhysicalSwitch(new DPID("0:0:0:0:0:0:0:a"), "S1"), 
                   is( new PhysicalSwitch(new DPID("0:0:0:0:0:0:0:a"), "S1")));
        assertThat(    new PhysicalSwitch(new DPID("0:0:0:0:0:0:0:a"), "S1"), 
                   not(new PhysicalSwitch(new DPID("0:0:0:0:0:0:0:b"), "S1")));
        assertThat(    new PhysicalSwitch(new DPID("0:0:0:0:0:0:0:a"), "S1"), 
                   not(new PhysicalSwitch(new DPID("0:0:0:0:0:0:0:a"), "S2")));
        
        // single switches
        VNO vno = new VNO(new Tenant("Tenant"));
        SingleSwitch vsw1 = new SingleSwitch(vno,  new DPID("0:0:0:0:0:0:0:a"), "VS1");
        SingleSwitch vsw2 = new SingleSwitch(vno,  new DPID("0:0:0:0:0:0:0:a"), "VS1");
        SingleSwitch vsw3 = new SingleSwitch(null, new DPID("0:0:0:0:0:0:0:a"), "VS3");
        
        // same/diff vnos
        assertThat(vsw1, is (vsw2));
        assertThat(vsw1, not(vsw3));
        
        // same/diff physical switch
        vsw1.setPhysicalSwitch(new PhysicalSwitch(new DPID("0:0:0:0:0:0:0:a"), "S1"));
        assertThat(vsw1, not(vsw2));
        
        vsw2.setPhysicalSwitch(new PhysicalSwitch(new DPID("0:0:0:0:0:0:0:a"), "S1"));
        assertThat(vsw1, is(vsw2));
    }

}
