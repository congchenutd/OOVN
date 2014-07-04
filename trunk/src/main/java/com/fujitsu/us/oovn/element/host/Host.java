package com.fujitsu.us.oovn.element.host;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.element.address.IPAddress;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Host always connects to the virtual network
 * Unlike switches, hosts are "connected" to ports without Links
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class Host implements Jsonable
{
//    private static Logger     _log = LoggerFactory.getLogger(Host.class);
    private final  int        _id;
    private final  String     _name;
    private final  MACAddress _mac;
    private VirtualIPAddress  _ip;
    private VirtualPort       _port;
    
    public Host(Integer id, String name, MACAddress mac, VirtualIPAddress ip)
    {
        _id   = id;
        _name = name;
        _mac  = mac;
        _ip   = ip;
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
    
    public boolean setPort(VirtualPort port)
    {
        // TODO: how to check if this port has already connected to another host?
        // a port has no info about the host
        _port = port;
        _port.setIsEdge(true);
        return true;
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.addProperty("id", getID());
        result.add("mac",  getMACAddress().toJson());
        result.add("ip",   getIPAddress() .toJson());
        result.add("port", getPort().toJson());
        return result;
    }
}
