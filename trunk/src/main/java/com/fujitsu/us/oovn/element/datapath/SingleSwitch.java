package com.fujitsu.us.oovn.element.datapath;

import com.fujitsu.us.oovn.element.address.DPID;

/**
 * A SingleSwitch maps to one physical switch
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class SingleSwitch extends VirtualSwitch
{
    private PhysicalSwitch _physicalSwitch;
    
    public SingleSwitch(DPID dpid, String name) {
        super(dpid, name);
    }

    public PhysicalSwitch getPhysicalSwitch() {
        return _physicalSwitch;
    }
    
    public void setPhysicalSwitch(PhysicalSwitch physical) {
        _physicalSwitch = physical;
    }

}
