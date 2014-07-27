package com.fujitsu.us.oovn.element.network;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fujitsu.us.oovn.builder.PhysicalNetworkBuilder;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.exception.InvalidNetworkConfigurationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Should sync with real physical network
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class PhysicalNetwork extends Network<PhysicalSwitch, PhysicalLink, PhysicalPort>
                             implements Persistable
{
    // initialize the network from a json configuration
    // TODO: from discovery
    static
    {
        try
        {
            String config = new String(Files.readAllBytes(Paths.get("PhysicalConfig.json")));
            JsonObject json = (JsonObject) new JsonParser().parse(config);
            PhysicalNetwork pnw = PhysicalNetwork.getInstance();
            new PhysicalNetworkBuilder().build(json, pnw);
        }
        catch (IOException | InvalidNetworkConfigurationException e) {
            e.printStackTrace();
        }
    }
    
    // singleton
    public static PhysicalNetwork getInstance() {
        return LazyHolder._instance;
    }
    
    private static class LazyHolder {
        private static final PhysicalNetwork _instance = new PhysicalNetwork();
    }
    
    private PhysicalNetwork() {}
    
    @Override
    public String toDBMatch() {
        return "(:ZPhysical {type:\"PhysicalNetwork\"})";
    }

}
