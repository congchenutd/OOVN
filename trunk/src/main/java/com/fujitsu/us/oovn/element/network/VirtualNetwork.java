package com.fujitsu.us.oovn.element.network;

import java.util.HashMap;
import java.util.Map;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.IPAddress;
import com.fujitsu.us.oovn.element.datapath.VirtualSwitch;
import com.fujitsu.us.oovn.element.host.Host;
import com.fujitsu.us.oovn.element.link.VirtualLink;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class VirtualNetwork extends Network<VirtualSwitch, VirtualLink, VirtualPort>
                            implements Persistable
{
    private final VNO       _vno;
    private final IPAddress _networkIP;
    private final int       _mask;
    private final Map<Integer, Host> _hosts;   // host id -> host

    public VirtualNetwork(VNO vno, IPAddress ip, int mask)
    {
        _vno       = vno;
        _networkIP = ip;
        _mask      = mask;
        _hosts     = new HashMap<Integer, Host>();
    }
    
    public VNO getVNO() {
        return _vno;
    }
    
    public int getVNOID() {
        return getVNO().getID();
    }
    
    public int getTenantID() {
        return _vno.getTenant().getID();
    }
    
    public IPAddress getNetworkAddress() {
        return _networkIP;
    }
    
    public int getMask() {
        return _mask;
    }
    
    public boolean addHost(Host host)
    {
        if(_hosts.containsKey(host.getID()))
            return false;
        _hosts.put(host.getID(), host);
        return true;
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.add        ("address", getNetworkAddress().toJson());
        result.addProperty("mask",    getMask());
        
        JsonArray hostsJson = new JsonArray();
        for(Host h: _hosts.values())
            hostsJson.add(h.toJson());
        JsonObject nwJson = (JsonObject) super.toJson();
        nwJson.add("hosts", hostsJson);
        result.add("network", nwJson);

        return result;
    }
    
    @Override
    public String toDBMatch() {
        return "(:Virtual [vnoid=" + getVNOID() + "])";
    }

}


