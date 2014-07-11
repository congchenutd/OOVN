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
import com.fujitsu.us.oovn.element.port.Port;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Base class for all networks
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class Network implements Jsonable
{
    protected Map<Long, Switch> _switches;    // dpid -> switch
    protected Set<Link>         _links;
    
    public Network()
    {
        _switches = new HashMap<Long, Switch>();
        _links    = new HashSet<Link>();
    }
    
    public boolean addSwitch(Switch sw)
    {
        if(_switches.containsKey(sw.getDPID().toInt()))
            return false;
        _switches.put(sw.getDPID().toInt(), sw);
        return true;
    }
    
    public boolean removeSwitch(Switch sw)
    {
        if(!_switches.containsKey(sw.getDPID().toInt()))
            return false;
        _switches.remove(sw.getDPID());
        return true;
    }
    
    public boolean addLink(Link link)
    {
        if(_links.contains(link))
            return false;
        _links.add(link);
        return true;
    }
    
//    public boolean addLinkPair(Port port1, Port port2)
//    {
//        if(port1.getInLink()  != null && port1.getInLink() .getSrcPort() == port2 || 
//           port2.getOutLink() != null && port2.getOutLink().getDstPort() == port2)
//            return false;
//        return addLinkPair(new LinkPair(port1, port2));
//    }
    
    public boolean removeLink(Link link) {
        return _links.remove(link);
    }
    
    public Switch getSwitch(DPID dpid) {
        return _switches.containsKey(dpid.toInt()) ? _switches.get(dpid.toInt()) 
                                                   : null;
    }
    
    public Map<Long, Switch> getSwitches() {
        return Collections.unmodifiableMap(_switches);
    }
    
    public Link getLink(Port srcPort, Port dstPort)
    {
        Link link = srcPort.getLink();
        if(link.getOtherPort(srcPort).equals(dstPort))
            return link;
        return null;
    }
    
    public Set<Link> getLinks() {
        return Collections.unmodifiableSet(_links);
    }
    
    public Port getNeighborPort(Port srcPort) {
        return srcPort.getLink().getDstPort();
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        
        JsonArray switches = new JsonArray();
        for(Switch sw: _switches.values())
            switches.add(sw.toJson());
        result.add("switches", switches);
        
        JsonArray links = new JsonArray();
        for(Link link: _links)
            links.add(link.toJson());
        result.add("links", links);
        
        return result;
    }
    
    public boolean activate()
    {
        return true;
    }
    
    public boolean deactivate()
    {
        return true;
    }
}
