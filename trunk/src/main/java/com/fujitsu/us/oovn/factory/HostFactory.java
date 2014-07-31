package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.NetworkElement;
import com.fujitsu.us.oovn.element.address.MACAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;
import com.fujitsu.us.oovn.element.host.Host;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.fujitsu.us.oovn.exception.InvalidConfigurationException;
import com.google.gson.JsonObject;

public class HostFactory extends ElementFactory
{

    @Override
    protected NetworkElement create(Node node, VNO vno) {
        return null;
    }

    @Override
    protected NetworkElement create(JsonObject json, JsonObject parentJson, VNO vno) 
                                    throws InvalidConfigurationException
    {
        if(!json.has("id"))
            throw new InvalidConfigurationException("No id defined. Json: " + json);
        int id = json.get("id").getAsInt();
        
        if(!json.has("name"))
            throw new InvalidConfigurationException("No name defined. Json: " + json);
        String name = json.get("name").getAsString();
        
        if(!json.has("mac"))
            throw new InvalidConfigurationException("No mac defined. Json: " + json);
        MACAddress mac = new MACAddress(json.get("mac").getAsString());
        
        if(!json.has("ip"))
            throw new InvalidConfigurationException("No ip defined. Json: " + json);
        VirtualIPAddress ip = new VirtualIPAddress(vno,
                                                   json.get("ip").getAsString());
        
        if(!json.has("port"))
            throw new InvalidConfigurationException("No port defined. Json: " + json);
        VirtualPort port = (VirtualPort) 
                           ElementFactory.fromJson("VirtualPort", 
                                                   json.get("port").getAsJsonObject(), 
                                                   null, vno);
        
        return new Host(id, name, mac, ip, port);
    }

    @Override
    protected String getTypeName() {
        return "Host";
    }

}
