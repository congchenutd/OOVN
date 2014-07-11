package com.fujitsu.us.oovn.element.network;

import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.link.Link;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.port.PhysicalPort;

/**
 * Should sync with real physical network
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class PhysicalNetwork extends Network
{
    // singleton
    public static PhysicalNetwork getInstance() {
        return LazyHolder._instance;
    }
    
    private static class LazyHolder {
        private static final PhysicalNetwork _instance = new PhysicalNetwork();
    }
    
    // for testing
    private PhysicalNetwork()
    {
        PhysicalSwitch sw1 = new PhysicalSwitch(new DPID(1), "S1");
        sw1.addPort(new PhysicalPort(1, new MACAddress("0:0:0:0:0:1")));
        sw1.addPort(new PhysicalPort(2, new MACAddress("0:0:0:0:0:2")));
                
        PhysicalSwitch sw2 = new PhysicalSwitch(new DPID(2), "S2");
        sw2.addPort(new PhysicalPort(1, new MACAddress("0:0:0:0:0:3")));
        sw2.addPort(new PhysicalPort(2, new MACAddress("0:0:0:0:0:4")));
        
        addSwitch(sw1);
        addSwitch(sw2);
        
        addLink(new Link(sw1.getPort(2), sw2.getPort(1)));
    }
    
    public String toDBMatch() {
        return ":Physical";
    }
    
    public String toDBCreate()
    {
        StringBuilder builder = new StringBuilder();
        
        int swIndex = 0;
        for(Switch sw: getSwitches().values())
        {
            if(swIndex++ > 0)
                builder.append(",\n");
            builder.append(((PhysicalSwitch) sw).toDBCreate());
        }
        
        for(Link link: getLinks())
        {
            builder.append(",\n");
            builder.append(((PhysicalLink) link).toDBCreate() + ",\n");
            builder.append("(" + link.getName() + ")-[:Connects]->(" + link.getSrcPort().getName() + ")" + ",\n");
            builder.append("(" + link.getName() + ")-[:Connects]->(" + link.getDstPort().getName() + ")");
        }
        return builder.toString();
    }
    
}
