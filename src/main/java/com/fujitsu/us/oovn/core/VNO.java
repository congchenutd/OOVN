package com.fujitsu.us.oovn.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VNO
{
    private final int            _id;
    private final Tenant         _tenant;
//    private final       VirtualNetwork _network = null;
    private       NetworkConfiguration     _config = null;
    
    public VNO(Tenant tenant)
    {
        _id     = VNOCounter.getNextID();
        _tenant = tenant;
    }
     
    public int getID() {
        return _id;
    }
    
    public Tenant getTenant() {
        return _tenant;
    }
    
    public int getTenantID() {
        return getTenant().getID();
    }
    
    public NetworkConfiguration getConfiguration() {
        return _config;
    }
    
    public JsonObject getPhysicalTopology()
    {
        VNOArbitor arbitor = VNOArbitor.getInstance();
        return arbitor.getPhysicalTopology();
    }
    
    public void init(String configFileName)
    {
        try {
            String config = new String(Files.readAllBytes(Paths.get(configFileName)));
            _config = new NetworkConfiguration(
                            (JsonObject) new JsonParser().parse(config));
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public boolean verify()
    {
        VNOArbitor arbitor = VNOArbitor.getInstance();
        if(arbitor.verifyConfiguration(getConfiguration().toJson()) == true)
            getConfiguration().setVerified(true);
        return getConfiguration().isVerified();
    }
    
    public boolean activate()
    {
        return true;
    }
    
    public boolean deactivate()
    {
        return true;
    }
    
    public boolean decommission()
    {
        return true;
    }
    
}


class VNOCounter
{
    private static int _counter = 0;
    
    public static int getNextID() {
        return ++ _counter;
    }
}
