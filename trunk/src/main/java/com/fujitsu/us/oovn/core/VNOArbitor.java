package com.fujitsu.us.oovn.core;

import java.util.List;

import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.Switch;
import com.fujitsu.us.oovn.element.datapath.VirtualSwitch;
import com.fujitsu.us.oovn.element.link.LinkPair;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.network.VirtualNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.Port;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.google.gson.JsonObject;

/**
 * Controller of the entire virtual universe
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class VNOArbitor
{
    // singleton
    public static VNOArbitor getInstance() {
        return LazyHolder._instance;
    }
    
    private static class LazyHolder {
        private static final VNOArbitor _instance = new VNOArbitor();
    }
    
    private VNOArbitor() {}
    
    /**
     * @return the allowable physical topology, may not be the actual one
     */
    public NetworkConfiguration getPhysicalTopology()
    {
        PhysicalNetwork pnw = PhysicalNetwork.getInstance();
        return new NetworkConfiguration((JsonObject) pnw.toJson());
    }
    
    public boolean verifyVNO(VNO vno)
    {
        return true;
    }
    
    public boolean activateVNO(VNO vno)
    {
        // build a VirtualNetwork and assign it to VNO
        if(vno.getNetwork() == null)
        {
            VirtualNetwork vn = NetworkBuilder.getInstance().build(vno);
            vno.setNetwork(vn);
            VNOPool.getInstance().registerVNO(vno);
            vn.activate();
        }
        updateGlobalMap(vno);
        return true;
    }
    
    public boolean deactivateVNO(VNO vno)
    {
        boolean result = vno.getNetwork().deactivate();
        updateGlobalMap(vno);
        return result;
    }
    
    public boolean decommssionVNO(VNO vno)
    {
        VNOPool.getInstance().unregisterVNO(vno);
        updateGlobalMap(vno);
        return true;
    }
    
    private void updateGlobalMap(VNO vno)
    {
        // add the vno info to the global map
        if(vno.getState() == VNO.VNOState.ACTIVE)
        {
            VirtualNetwork vn = vno.getNetwork();
            
            // switches
            for(Switch sw : vn.getSwitches().values())
            {
                List<PhysicalSwitch> psws = ((VirtualSwitch) sw).getPhysicalSwitches();
                for(PhysicalSwitch psw: psws)
                {
                    int count = (Integer) psw.getMeasurement("sharedcount");
                    psw.setMeasurement("sharedcount", count + 1);
                }
                
                // ports
                for(Port port: sw.getPorts().values())
                {
                    PhysicalPort pport = ((VirtualPort) port).getPhysicalPort();
                    int count = (Integer) pport.getMeasurement("sharedcount");
                    pport.setMeasurement("sharedcount", count + 1);
                }
            }
            
            // links
            for(LinkPair linkPair: vn.getLinks())
            {
//                PhysicalLink inLink = ((VirtualLink) linkPair.getInLink()).getPhysicalLink();
            }

        }
        
        // remove the vno info from the global map
        else
        {
            
        }
    }
}
