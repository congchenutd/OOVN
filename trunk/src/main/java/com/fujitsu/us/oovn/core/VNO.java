package com.fujitsu.us.oovn.core;

import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.network.VirtualNetwork;
import com.fujitsu.us.oovn.element.port.Port;

public class VNO
{
    private final int            _id;
    private       Tenant         _tenant;
    private final       VirtualNetwork _network = null;
    
    public VNO(Tenant tenant)
    {
        _id     = VNOCounter.getNextID();
        _tenant = tenant;
    }
     
    public int getID() {
        return _id;
    }
    
    public Tenant getTenant() {
        return _tenant;
    }
    
    public int getTenantID() {
        return getTenant().getID();
    }

    public void setTenant(Tenant tenant) {
        _tenant = tenant;
    }
    
    public boolean init()
    {
        return true;
    }
    
    public boolean activate()
    {
        return true;
    }
    
    public boolean deactivate()
    {
        return true;
    }
    
    public boolean decommssion()
    {
        return true;
    }
    
    public static void main(String[] args)
    {
        NetworkConfiguration config = new NetworkConfiguration();
        config.setTenantID(1);
        config.setIP("192.168.0.0");
        config.setMask(24);
        
        Switch sw1 = new Switch(1, "S1");
        sw1.addPort(new Port(1));
        sw1.addPort(new Port(2));
        
        Switch sw2 = new Switch(2, "S2");
        sw2.addPort(new Port(1));
        sw2.addPort(new Port(2));
        
        config.addSwitch(sw1);
        config.addSwitch(sw2);
        
        config.addLink(sw1.getPort(2), sw2.getPort(1));
        
        String jsonString = config.toJsonString();
        System.out.println(jsonString);
//        System.out.println(config.getIP());

//        VNOConfiguration config2 = VNOConfiguration.fromJson(jsonString);
//        Set<LinkPair> links = config2.getLinks();
//        for(LinkPair lp: config2.getLinks())
//            System.out.println(lp);
    }
}

class VNOCounter
{
    private static int _counter = 0;
    
    public static int getNextID() {
        return ++ _counter;
    }
}
