package com.fujitsu.us.oovn.element.datapath;

import java.util.List;

import com.fujitsu.us.oovn.element.address.DPID;

public abstract class VirtualSwitch extends Switch {

    public VirtualSwitch(DPID dpid, String name) {
        super(dpid, name);
    }

    public abstract List<PhysicalSwitch> getPhysicalSwitches();
}
