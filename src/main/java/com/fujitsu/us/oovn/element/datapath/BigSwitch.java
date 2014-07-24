package com.fujitsu.us.oovn.element.datapath;

import java.util.List;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.map.MapBase;

/**
 * A BigSwitch consists of multiple physical switches
 * It handles the internal route by itself
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class BigSwitch extends VirtualSwitch {

    public BigSwitch(VNO vno, DPID dpid, String name) {
        super(vno, dpid, name);
    }

    @Override
    public List<PhysicalSwitch> getPhysicalSwitches() {
        return null;
    }

    @Override
    public void createInDB(MapBase map) {
    }

    @Override
    public String toDBMatch() {
        return null;
    }

}
