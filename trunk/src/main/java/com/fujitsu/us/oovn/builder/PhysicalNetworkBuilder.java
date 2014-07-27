package com.fujitsu.us.oovn.builder;

import com.fujitsu.us.oovn.element.network.Network;
import com.fujitsu.us.oovn.exception.InvalidNetworkConfigurationException;
import com.google.gson.JsonObject;

public class PhysicalNetworkBuilder extends NetworkBuilder
{
    
    public void build(JsonObject json, Network network) 
                        throws InvalidNetworkConfigurationException {
        super.build(json, network, null);
    }
}
