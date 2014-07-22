package com.fujitsu.us.oovn.element.host;

import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

import com.fujitsu.us.oovn.core.Tenant;
import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;
import com.fujitsu.us.oovn.element.port.VirtualPort;

public class HostTest
{
    private VNO  _vno;
    private Host _host1;
    private Host _host2;
    
    @Before
    public void setUp() throws Exception
    {
        _vno   = new VNO(new Tenant("Carl"));
        _host1 = new Host(1, "H1", new MACAddress("0:0:0:0:0:1"), 
                                   new VirtualIPAddress(_vno, "192.168.1.2"));
        _host2 = new Host(1, "H1", new MACAddress("0:0:0:0:0:1"), 
                                   new VirtualIPAddress(_vno, "192.168.1.2"));
    }

    @Test
    public final void testEquals()
    {
        assertThat(_host1, is (_host2));
        
        // diff number
        assertThat(_host1, not(new Host(2, "H1", new MACAddress("0:0:0:0:0:1"), 
                                                 new VirtualIPAddress(_vno, "192.168.1.2"))));
        
        // diff name
        assertThat(_host1, not(new Host(1, "H2", new MACAddress("0:0:0:0:0:1"), 
                                                 new VirtualIPAddress(_vno, "192.168.1.2"))));
        
        // diff MAC
        assertThat(_host1, not(new Host(1, "H1", new MACAddress("0:0:0:0:0:2"), 
                                                 new VirtualIPAddress(_vno, "192.168.1.2"))));
        // diff ip
        assertThat(_host1, not(new Host(1, "H1", new MACAddress("0:0:0:0:0:1"), 
                                                 new VirtualIPAddress(_vno, "192.168.1.3"))));

        // diff port
        _host1.setPort(new VirtualPort(1, new MACAddress("0:0:0:0:0:2")));
        assertThat(_host1, not(_host2));
        
        // same port
        _host2.setPort(new VirtualPort(1, new MACAddress("0:0:0:0:0:2")));
        assertThat(_host1, is(_host2));
    }

}
