package com.fujitsu.us.oovn.element.host;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.element.NetworkElement;
import com.fujitsu.us.oovn.element.address.IPAddress;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.fujitsu.us.oovn.map.MapBase;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Host always connects to the virtual network
 * Unlike switches, hosts are "connected" to ports without Links
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class Host implements Jsonable, NetworkElement
{
    private final int              _id;
    private final String           _name;
    private final MACAddress       _mac;
    private final VirtualIPAddress _ip;
    private       VirtualPort      _port;
    
    public Host(Integer id, String name, MACAddress mac, 
                VirtualIPAddress ip, VirtualPort port)
    {
        _id   = id;
        _name = name;
        _mac  = mac;
        _ip   = ip;
        setPort(port);
    }
    
    public int getID() {
        return _id;
    }
    
    public String getName() {
        return _name;
    }
    
    public MACAddress getMACAddress() {
        return _mac;
    }
    
    public IPAddress getIPAddress() {
        return _ip;
    }
    
    public VirtualPort getPort() {
        return _port;
    }
    
    public void setPort(VirtualPort port)
    {
        _port = port;
        if(_port != null)
            _port.setHost(this);
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(this == obj)
            return true;
        
        if(! (obj instanceof Host))
            return false;
        
        Host that = (Host) obj;
        return  this.getID()         == that.getID()              &&
                this.getName().      equals(that.getName())       &&
                this.getIPAddress(). equals(that.getIPAddress())  &&
                this.getMACAddress().equals(that.getMACAddress()) &&
                (this.getPort() == null && that.getPort() == null || 
                 this.getPort() != null && this.getPort().equals(that.getPort()));
    }
    
    @Override
    public String toString() {
        return  "id: "   + getID() +
                "name: " + getName() +
                "mac: "  + getMACAddress() +
                "ip: "   + getIPAddress() +
                "port: " + getPort();
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.addProperty("id", getID());
        result.add("mac",  getMACAddress().toJson());
        result.add("ip",   getIPAddress() .toJson());
        if(getPort() != null)
            result.add("port", getPort().toJson());
        return result;
    }

    @Override
    public String toDBVariable() {
        return null;
    }

    @Override
    public String toDBMatch() {
        return null;
    }

    @Override
    public void createInDB(MapBase map) {
    }

    @Override
    public void createMapping(MapBase map) {
    }
}
