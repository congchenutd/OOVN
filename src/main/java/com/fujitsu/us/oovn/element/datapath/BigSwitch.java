package com.fujitsu.us.oovn.element.datapath;

import com.fujitsu.us.oovn.element.address.DPID;

/**
 * A BigSwitch consists of multiple physical switches
 * It handles the internal route by itself
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class BigSwitch extends VirtualSwitch {

    public BigSwitch(DPID dpid, String name) {
        super(dpid, name);
    }

}
