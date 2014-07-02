package com.fujitsu.us.oovn.element.network;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.link.Link;
import com.fujitsu.us.oovn.element.link.LinkPair;
import com.fujitsu.us.oovn.element.port.Port;
import com.fujitsu.us.oovn.exception.InvalidDPIDException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Network implements Jsonable
{
    protected Map<Long, Switch> _switches;
    protected Set<LinkPair>     _linkPairs;
    
    public Network()
    {
        _switches  = new HashMap<Long, Switch>();
        _linkPairs = new HashSet<LinkPair>();
    }
    
    public boolean addSwitch(final Switch sw)
    {
        if(_switches.containsKey(sw.getDPID()))
            return false;
        _switches.put(sw.getDPID(), sw);
        return true;
    }
    
    public boolean removeSwitch(final Switch sw)
    {
        if(_switches.containsKey(sw.getDPID()))
        {
            _switches.remove(sw.getDPID());
            return true;
        }
        return false;
    }
    
    public boolean addLinkPair(LinkPair linkPair)
    {
        if(_linkPairs.contains(linkPair))
            return false;
        _linkPairs.add(linkPair);
        return true;
    }
    
    public boolean addLinkPair(Port port1, Port port2)
    {
        if(port1.getInLink()  != null && port1.getInLink() .getSrcPort() == port2 || 
           port2.getOutLink() != null && port2.getOutLink().getDstPort() == port2)
            return false;
        return addLinkPair(new LinkPair(port1, port2));
    }
    
    public boolean removeLink(final Link link) {
        return _linkPairs.remove(link);
    }
    
    public Switch getSwitch(DPID dpid) throws InvalidDPIDException
    {
        try {
            return _switches.get(dpid.value());
        }
        catch (NullPointerException e) {
            throw new InvalidDPIDException("DPID " + dpid + " is unknown");
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
    
    public Set<LinkPair> getLinks() {
        return Collections.unmodifiableSet(_linkPairs);
    }
    
    public Port getNeighborPort(final Port srcPort) {
        return srcPort.getLinkPair().getOutLink().getDstPort();
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        
        JsonArray switches = new JsonArray();
        for(Switch sw: getSwitches().values())
            switches.add(sw.toJson());
        result.add("switches", switches);
        
        JsonArray links = new JsonArray();
        for(LinkPair link: getLinks())
            links.add(link.toJson());
        result.add("links", links);
        
        return result;
    }
}
