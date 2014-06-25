package com.fujitsu.us.oovn.element.network;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.openflow.util.HexString;

import com.fujitsu.us.oovn.core.NetworkConfiguration;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.link.Link;
import com.fujitsu.us.oovn.element.port.Port;
import com.fujitsu.us.oovn.exception.InvalidDPIDException;

public class Network
{
    protected Map<Long, Switch> _switches;
    protected Set<Link>         _links;
    
    public Network()
    {
        _switches  = new HashMap<Long, Switch>();
        _links     = new HashSet<Link>();
    }
    
    public boolean addSwitch(final Switch sw)
    {
        if(_switches.containsKey(sw.getID()))
            return false;
        _switches.put(sw.getID(), sw);
        return true;
    }
    
    public boolean removeSwitch(final Switch sw)
    {
        if(_switches.containsKey(sw.getID()))
        {
            _switches.remove(sw.getID());
            return true;
        }
        return false;
    }
    
    public boolean addLink(final Link link)
    {
        if(_links.contains(link))
            return false;
        _links.add(link);
        return true;
    }
    
    public boolean removeLink(final Link link) {
        return _links.remove(link);
    }
    
    public Switch getSwitch(long dpid) throws InvalidDPIDException
    {
        try {
            return _switches.get(dpid);
        }
        catch (NullPointerException e) {
            throw new InvalidDPIDException("DPID " + HexString.toHexString(dpid) + " is unknown");
        }
    }
    
    public Map<Long, Switch> getSwitches() {
        return Collections.unmodifiableMap(_switches);
    }
    
    public Link getLink(final Port srcPort, final Port dstPort)
    {
        Link link = srcPort.getLinkPair().getOutLink();
        if(link.getDstPort().equals(dstPort))
            return link;
        return null;
    }
    
    public Set<Link> getLinks() {
        return Collections.unmodifiableSet(_links);
    }
    
    public Port getNeighborPort(final Port srcPort) {
        return srcPort.getLinkPair().getOutLink().getDstPort();
    }
    
    public NetworkConfiguration toConfiguration()
    {
        NetworkConfiguration config = new NetworkConfiguration();
        return config;
    }
}
