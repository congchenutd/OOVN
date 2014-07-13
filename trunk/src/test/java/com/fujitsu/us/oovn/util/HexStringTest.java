package com.fujitsu.us.oovn.util;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HexStringTest
{
    private HexString hex1;
    private HexString hex2;

    @Before
    public void setUp()
    {
        hex1 = new HexString("01:23:45:67:89:ab:cd:ef", 8, ':');
        hex2 = new HexString(1234567890987654321L,      8, ':');
    }
    
    @Test
    public final void testToString()
    {
        Assert.assertThat(hex1.toString(), is("01:23:45:67:89:AB:CD:EF"));
        Assert.assertThat(hex2.toString(), is("11:22:10:F4:B1:6C:1C:B1"));
    }

    @Test
    public final void testToInt()
    {
        Assert.assertThat(hex1.toInt(), is(81985529216486895L));
        Assert.assertThat(hex2.toInt(), is(1234567890987654321L));
    }
    
    @Test
    public final void testEquals()
    {
        Assert.assertThat(hex1, is (new HexString(81985529216486895L, 8, ':')));
        Assert.assertThat(hex1, not(new HexString(81985529216486895L, 4, ':')));
        Assert.assertThat(hex1, not(new HexString(81985529216486895L, 8, '.')));
    }
}
