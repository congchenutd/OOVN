package com.fujitsu.us.oovn.element.datapath;

import static org.hamcrest.CoreMatchers.*;

import org.junit.Assert;
import org.junit.Test;

import com.fujitsu.us.oovn.element.address.DPID;

public class SwitchTest
{
    @Test
    public final void testEqualsObject()
    {
        Assert.assertThat(    new Switch(new DPID("0:0:0:0:0:0:0:a"), "S1"), 
                          is( new Switch(new DPID("0:0:0:0:0:0:0:a"), "S1")));
        Assert.assertThat(    new Switch(new DPID("0:0:0:0:0:0:0:a"), "S1"), 
                          not(new Switch(new DPID("0:0:0:0:0:0:0:b"), "S1")));
        Assert.assertThat(    new Switch(new DPID("0:0:0:0:0:0:0:a"), "S1"), 
                          not(new Switch(new DPID("0:0:0:0:0:0:0:a"), "S2")));
    }

}
