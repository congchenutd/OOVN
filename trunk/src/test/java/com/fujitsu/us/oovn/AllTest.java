package com.fujitsu.us.oovn;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.fujitsu.us.oovn.core.VNOTest;
import com.fujitsu.us.oovn.element.ElementTest;
import com.fujitsu.us.oovn.map.MapTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ElementTest.class,
    VNOTest.class,
    MapTest.class
})

public class AllTest {
}
