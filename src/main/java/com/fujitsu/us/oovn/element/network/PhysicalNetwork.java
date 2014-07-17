package com.fujitsu.us.oovn.element.network;

import org.neo4j.cypher.javacompat.ExecutionEngine;

import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.port.PhysicalPort;

/**
 * Should sync with real physical network
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class PhysicalNetwork extends Network<PhysicalSwitch, PhysicalLink, PhysicalPort>
                             implements Persistable
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
        
        PhysicalSwitch sw3 = new PhysicalSwitch(new DPID(3), "S3");
        sw3.addPort(new PhysicalPort(1, new MACAddress("0:0:0:0:0:5")));
        sw3.addPort(new PhysicalPort(2, new MACAddress("0:0:0:0:0:6")));
        
        addSwitch(sw1);
        addSwitch(sw2);
        addSwitch(sw3);
        
        addLink(new PhysicalLink(sw1.getPort(2), sw2.getPort(1)));
        addLink(new PhysicalLink(sw2.getPort(2), sw3.getPort(1)));
    }
    
    @Override
    public String toDBMatch() {
        return "(:Physical)";
    }

    @Override
    public void createSelf(ExecutionEngine engine)
    {
        for(PhysicalSwitch sw: getSwitches().values())
            sw.createSelf(engine);
        
        for(PhysicalLink link: getLinks())
            link.createSelf(engine);
    }

    @Override
    public void createMapping(ExecutionEngine engine) {
    }

}
