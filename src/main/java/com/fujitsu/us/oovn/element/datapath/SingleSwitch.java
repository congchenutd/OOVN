package com.fujitsu.us.oovn.element.datapath;

public class SingleSwitch extends VirtualSwitch
{
    private PhysicalSwitch _physicalSwitch;
    
    public SingleSwitch(long dpid, String name) {
        super(dpid, name);
    }

}
