package com.fujitsu.us.oovn.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fujitsu.us.oovn.element.network.VirtualNetwork;
import com.fujitsu.us.oovn.exception.InvalidVNOOperationException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VNO
{
    private final Tenant         _tenant;
    private final int            _id;
    private State                _state;
    private NetworkConfiguration _config;
    private VirtualNetwork       _network;
    
    public VNO(Tenant tenant)
    {
        _tenant = tenant;
        _id     = VNOCounter.getNextID();
        _state = State.UNCONFIGURED;
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
    
    public NetworkConfiguration getPhysicalTopology() {
        return VNOArbitor.getInstance().getPhysicalTopology();
    }
    
    public void setConfiguration(NetworkConfiguration config) {
        _config = config;
    }

    public boolean isVerified() {
        return getConfiguration().isVerified();
    }
    
    public void setVerified(boolean verified) {
        getConfiguration().setVerified(verified);
    }
    
    public void setNetwork(VirtualNetwork network) {
        _network = network;
    }
    
    public void init(String configFileName) {
        _state.init(this, configFileName);
    }
    
    public boolean verify()
    {
        try {
            return _state.verify(this);
        }
        catch(InvalidVNOOperationException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean activate()
    {
        try {
            return _state.activate(this);
        }
        catch(InvalidVNOOperationException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean deactivate()
    {
        try {
            return _state.deactivate(this);
        }
        catch(InvalidVNOOperationException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    public boolean decommission()
    {
        try {
            return _state.decommission(this);
        }
        catch(InvalidVNOOperationException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void setState(State state) {
        _state = state;
    }
    
    enum State
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
            public boolean verify(VNO vno)
            {
                if(VNOArbitor.getInstance().verifyConfiguration(vno.getConfiguration()))
                {
                    vno.setVerified(true);
                    vno.getTenant().registerVNO(vno);
                    vno.setState(INACTIVE);
                }
                return vno.isVerified();
            }
            
        },
        INACTIVE
        {
            @Override
            public boolean activate(VNO vno)
            {
                if(VNOArbitor.getInstance().activateVNO(vno))
                {
                    vno.setState(ACTIVE);
                    return true;
                }
                return false;
            }
            
            @Override
            public boolean decommission(VNO vno)
            {
                vno.setState(DECOMMISSIONED);
                return true;
            }
        },
        ACTIVE
        {
            @Override
            public boolean deactivate(VNO vno)
            {
                vno.setState(INACTIVE);
                return true;
            }
            
            @Override
            public boolean decommission(VNO vno)
            {
                vno.setState(DECOMMISSIONED);
                return true;
            }
        },
        DECOMMISSIONED
        {

        };
        
        public void init(VNO vno, String configFileName) {
        }
        
        public boolean verify(VNO vno) throws InvalidVNOOperationException {
            throw new InvalidVNOOperationException("The VNO is not initialized (configured) yet");
        }
        
        public boolean activate(VNO vno) throws InvalidVNOOperationException {
            throw new InvalidVNOOperationException("The VNO is not verified yet");
        }
        
        public boolean deactivate(VNO vno) throws InvalidVNOOperationException {
            throw new InvalidVNOOperationException("The VNO is not activated yet");
        }
        
        public boolean decommission(VNO vno) throws InvalidVNOOperationException {
            throw new InvalidVNOOperationException("The VNO is not activated yet");
        }
    }

}

class VNOCounter
{
    private static int _counter = 0;
    
    public static int getNextID() {
        return ++ _counter;
    }
}