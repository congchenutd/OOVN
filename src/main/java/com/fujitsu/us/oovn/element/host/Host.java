package com.fujitsu.us.oovn.element.host;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fujitsu.us.oovn.element.Jsonable;
import com.fujitsu.us.oovn.element.address.IPAddress;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Host implements Jsonable
{
    private static Logger     _log = LoggerFactory.getLogger(Host.class);
    private final  int        _id;
    private final  MACAddress _mac;
//    private final  OVXPort port;
    private final IPAddress _ip = new VirtualIPAddress(0, "0.0.0.0");
    
    public Host(Integer id, MACAddress mac)
    {
        _id  = id;
        _mac = mac;
//        this.port = port;
    }
    
    public int getID() {
        return _id;
    }
    
    public MACAddress getMACAddress() {
        return _mac;
    }
    
    public IPAddress getIPAddress() {
        return _ip;
    }
    
    @Override
    public JsonElement toJson()
    {
        JsonObject result = new JsonObject();
        result.addProperty("vno id", getID());
        result.add("mac address", getMACAddress().toJson());
        result.add("ip address",  getIPAddress() .toJson());
        return result;
    }
    
    public static void main(String argv[])
    {
        Host host = new Host(1, new MACAddress(new byte[] {0,0,0,0,0,1}));
        System.out.println(host.toJson().toString());
    }
}
