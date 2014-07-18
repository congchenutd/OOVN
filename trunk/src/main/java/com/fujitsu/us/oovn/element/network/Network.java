package com.fujitsu.us.oovn.element.network;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.neo4j.cypher.javacompat.ExecutionEngine;

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
@SuppressWarnings("rawtypes")
public abstract class Network<SwitchType extends Switch, 
                     LinkType extends Link, 
                     PortType extends Port> implements Jsonable
{
    protected Map<Long, SwitchType> _switches;    // dpid -> switch
    protected Set<LinkType>         _links;
    
    public Network()
    {
        _switches = new HashMap<Long, SwitchType>();
        _links    = new HashSet<LinkType>();
    }
    
    public boolean addSwitch(SwitchType sw)
    {
        if(_switches.containsKey(sw.getDPID().toInt()))
            return false;
        _switches.put(sw.getDPID().toInt(), sw);
        return true;
    }
    
    public boolean removeSwitch(SwitchType sw)
    {
        if(!_switches.containsKey(sw.getDPID().toInt()))
            return false;
        _switches.remove(sw.getDPID());
        return true;
    }
    
    public boolean addLink(LinkType link)
    {
        if(_links.contains(link))
            return false;
        _links.add(link);
        return true;
    }
    
    public boolean removeLink(LinkType link) {
        return _links.remove(link);
    }
    
    public SwitchType getSwitch(DPID dpid) {
        return _switches.containsKey(dpid.toInt()) ? _switches.get(dpid.toInt()) 
                                                   : null;
    }
    
    public Map<Long, SwitchType> getSwitches() {
        return Collections.unmodifiableMap(_switches);
    }
    
    @SuppressWarnings("unchecked")
    public LinkType getLink(DPID srcDPID, int srcNumber, DPID dstDPID, int dstNumber)
    {
        return getLink((PortType) getSwitch(srcDPID).getPort(srcNumber),
                       (PortType) getSwitch(dstDPID).getPort(dstNumber));
    }
    
    @SuppressWarnings("unchecked")
    public LinkType getLink(PortType srcPort, PortType dstPort)
    {
        LinkType link = (LinkType) srcPort.getLink();
        if(link.getOtherPort(srcPort).equals(dstPort))
            return link;
        return null;
    }
    
    public Set<LinkType> getLinks() {
        return Collections.unmodifiableSet(_links);
    }
    
    @SuppressWarnings("unchecked")
    public PortType getNeighborPort(PortType port) {
        return (PortType) port.getLink().getOtherPort(port);
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
    
    public abstract String toDBMatch();
    
    public String toDBVariable() {
        return null;
    }
    
    /**
     * Mapping is done by the component objects (switch, link, port)
     */
    public void createMapping(ExecutionEngine engine) {
    }
    
    /**
     * Create the network in the db using the engine
     * @param engine ExecutionEngine for running db queries
     */
    public void createInDB(ExecutionEngine engine)
    {
        for(SwitchType sw: getSwitches().values())
            sw.createInDB(engine);
        
        for(LinkType link: getLinks())
            link.createInDB(engine);
    }
    
}
