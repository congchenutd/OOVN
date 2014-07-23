package com.fujitsu.us.oovn.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fujitsu.us.oovn.element.network.VirtualNetwork;
import com.fujitsu.us.oovn.exception.InvalidVNOOperationException;
import com.fujitsu.us.oovn.map.LocalMap;
import com.fujitsu.us.oovn.verification.VerificationResult;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * A Tenant only interacts with the VNOs
 * VNO talks to VNOArbitor and other VNOs
 * 
 * The operations are delegated to VNOState
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class VNO
{
    private final Tenant         _tenant;
    private final int            _id;
    private VNOState             _state;
    private NetworkConfiguration _config;
    private VirtualNetwork       _network;
    private final LocalMap       _map;
    
    public VNO(Tenant tenant)
    {
        _tenant = tenant;
        _id     = VNOCounter.getNextID();
        _state  = VNOState.UNCONFIGURED;
        _map    = LocalMap.getInstance(this);
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
    
    public VirtualNetwork getNetwork() {
        return _network;
    }
    
    public NetworkConfiguration getConfiguration() {
        return _config;
    }
    
    public VNOState getState() {
        return _state;
    }
    
    public NetworkConfiguration getPhysicalTopology() {
        return VNOArbitor.getInstance().getPhysicalTopology();
    }
    
    public boolean isVerified() {
        return getConfiguration().isVerified();
    }
    
    /**
     * Attach a VirtualNetwork to this VNO, once it's created
     * @param network
     */
    public void setNetwork(VirtualNetwork network) {
        _network = network;
    }
    
    private void setConfiguration(NetworkConfiguration config)
    {
        _config = config;
        setVerified(false);
    }
    
    private void setVerified(boolean verified) {
        getConfiguration().setVerified(verified);
    }
    
    private LocalMap getMap() {
        return _map;
    }
    
    ///////////// The major APIs tenant can call ////////////////
    public void init(String configFileName) throws InvalidVNOOperationException {
        _state.init(this, configFileName);
    }
    
    public VerificationResult verify() throws InvalidVNOOperationException {
        return _state.verify(this);
    }
    
    public boolean activate() throws InvalidVNOOperationException {
        return _state.activate(this);
    }
    
    public boolean deactivate() throws InvalidVNOOperationException {
        return _state.deactivate(this);
    }
    
    public boolean decommission() throws InvalidVNOOperationException {
        return _state.decommission(this);
    }
    /////////////////////////////////////////////////////////////

    private void setState(VNOState state) {
        _state = state;
    }
    
    enum VNOState
    {
        UNCONFIGURED
        {
            @Override
            public void init(VNO vno, String configFileName)
            {
                try {
                    String config = new String(Files.readAllBytes(Paths.get(configFileName)));
                    vno.setConfiguration(new NetworkConfiguration(
                                    (JsonObject) new JsonParser().parse(config)));

                    // if the config is loaded successfully, go to the next state
                    vno.setState(UNVERIFIED);
                }
                catch(IOException e) {
                    e.printStackTrace();
                }
            }
        },
        UNVERIFIED
        {
            @Override
            public VerificationResult verify(VNO vno)
            {
                VerificationResult result = VNOArbitor.getInstance().verifyVNO(vno);
                if(result.isPassed())
                {
                    vno.setVerified(true);
                    vno.getTenant().registerVNO(vno);          // register to tenant
                    VNOArbitor.getInstance().registerVNO(vno); // to arbitor
                    vno.getMap().registerVNO();                // to local map
                    vno.setState(INACTIVE);
                }
                return result;
            }
            
        },
        INACTIVE
        {
            @Override
            public boolean activate(VNO vno)
            {
                if(VNOArbitor.getInstance().activateVNO(vno))
                {
                    vno.getMap().activateVNO();
                    vno.setState(ACTIVE);
                    return true;
                }
                return false;
            }
        },
        ACTIVE
        {
            @Override
            public boolean deactivate(VNO vno)
            {
                VNOArbitor.getInstance().deactivateVNO(vno);
                vno.getMap().deactivateVNO();
                vno.setState(INACTIVE);
                return true;
            }
        },
        DECOMMISSIONED
        {

        };
        
        public void init(VNO vno, String configFileName) {
        }
        
        public VerificationResult verify(VNO vno) throws InvalidVNOOperationException {
            throw new InvalidVNOOperationException("The VNO is not initialized (configured) yet");
        }
        
        public boolean activate(VNO vno) throws InvalidVNOOperationException {
            throw new InvalidVNOOperationException("The VNO is not verified yet");
        }
        
        public boolean deactivate(VNO vno) throws InvalidVNOOperationException {
            throw new InvalidVNOOperationException("The VNO is not activated yet");
        }
        
        public boolean decommission(VNO vno) throws InvalidVNOOperationException
        {
            VNOArbitor.getInstance().decommssionVNO(vno);
            vno.getMap().unregisterVNO();
            vno.getTenant().unregisterVNO(vno);
            vno.setState(DECOMMISSIONED);
            return true;
        }
    }

}

/**
 * A VNO id generator
 * The id is unique in the entire universe, NOT per tenant
 */
class VNOCounter
{
    private static int _counter = 0;
    
    public static int getNextID() {
        return ++ _counter;
    }
}