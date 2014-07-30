package com.fujitsu.us.oovn.builder;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.network.Network;
import com.fujitsu.us.oovn.exception.InvalidConfigurationException;
import com.google.gson.JsonObject;

/**
 * Build a network from a Json configuration
 * 
 * Internally relies on ElementFactories to build/fetch network elements
 * The configuration file must define each element type, 
 * e.g., "type" = "SingleSwitch"
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public interface NetworkBuilder
{
    /**
     * Build a network based on a Json configuration
     * @param json      A JsonObject describing the network configuration
     * @param network   A network object to be built
     * @param vno       The VNO this network belongs to. Null for physical network
     * @throws InvalidConfigurationException
     */
    @SuppressWarnings("rawtypes")
    public void build(JsonObject json, Network network, VNO vno) 
                                    throws InvalidConfigurationException;
}
