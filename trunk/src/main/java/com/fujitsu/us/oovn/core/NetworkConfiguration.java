package com.fujitsu.us.oovn.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fujitsu.us.oovn.element.address.IPAddress;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.link.Link;
import com.fujitsu.us.oovn.element.link.LinkPair;
import com.fujitsu.us.oovn.element.port.Port;
import com.fujitsu.us.oovn.util.NoJson;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A description of VNO configuration
 * Loads configuration info from a JSon string
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class NetworkConfiguration
{
    private int               _tenantID;
    private IPAddress         _ip;
    private int               _mask;
    private final Map<Long, Switch> _switches = new HashMap<Long, Switch>();
    private final Set<LinkPair>     _links    = new HashSet<LinkPair>();
    
    public static NetworkConfiguration fromJson(String jsonString)
    {
        Gson gson = new Gson();
        NetworkConfiguration result = gson.fromJson(jsonString, NetworkConfiguration.class);
        return result;
    }
    
    public String toJsonString()
    {
        Gson gson = new GsonBuilder()
                        .setExclusionStrategies(new MyExclusionStrategy())
                        .serializeNulls()
                        .setPrettyPrinting()
                        .create();
        return gson.toJson(this);
    }

    public int getTenantID() {
        return _tenantID;
    }

    public void setTenantID(int id) {
        _tenantID = id;
    }

    public String getIP() {
        return _ip.toString();
    }

    public void setIP(String ip) {
        _ip = new IPAddress(ip);        
    }
    
    public int getMask() {
        return _mask;
    }

    public void setMask(int mask) {
        _mask = mask;
    }
    
    public void addSwitch(final Switch sw) {
        _switches.put(sw.getID(), sw);
    }
    
    public void addLink(final Port port1, final Port port2)
    {
        if(!_switches.containsKey(port1.getSwitch().getID()))
            return;
        if(!_switches.containsKey(port2.getSwitch().getID()))
            return;
        _links.add(new LinkPair(new Link(port1, port2), new Link(port2, port1)));
    }
    
    public Map<Long, Switch> getSwitches() {
        return Collections.unmodifiableMap(_switches);
    }
    
    public Set<LinkPair> getLinks() {
        return Collections.unmodifiableSet(_links);
    }
}

class MyExclusionStrategy implements ExclusionStrategy
{
    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getAnnotation(NoJson.class) != null;
    }
}