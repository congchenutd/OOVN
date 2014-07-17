package com.fujitsu.us.oovn.element;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.fujitsu.us.oovn.element.address.IPAddressTest;
import com.fujitsu.us.oovn.element.datapath.SwitchTest;
import com.fujitsu.us.oovn.element.host.HostTest;
import com.fujitsu.us.oovn.element.link.LinkTest;
import com.fujitsu.us.oovn.element.port.PortTest;
import com.fujitsu.us.oovn.util.HexStringTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    PortTest.class,
    LinkTest.class,
    SwitchTest.class,
    HostTest.class,
    IPAddressTest.class,
    HexStringTest.class
})

public class ElementTest {
}


