package com.fujitsu.us.oovn.core;

import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.exception.InvalidNetworkConfigurationException;
import com.fujitsu.us.oovn.map.GlobalMap;
import com.fujitsu.us.oovn.verification.TopologyVerifier;
import com.fujitsu.us.oovn.verification.VerificationResult;
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
     * @return the allowable physical topology, which may not be the actual one
     */
    public NetworkConfiguration getPhysicalTopology()
    {
        PhysicalNetwork pnw = PhysicalNetwork.getInstance();
        return new NetworkConfiguration((JsonObject) pnw.toJson());
    }
    
    /**
     * Verify the vno
     * @return a VerificationResult
     */
    public VerificationResult verifyVNO(VNO vno) {
        return new TopologyVerifier(null).verify(vno);
    }
    
    /**
     * Add the vno to GlobalMap
     * @return true if successful
     */
    public boolean registerVNO(VNO vno)
    {
        // build a VirtualNetwork and assign it to VNO
        if(vno.getNetwork() == null)
        {
            try {
                vno.build();
            } catch (InvalidNetworkConfigurationException e) {
                e.printStackTrace();
                return false;
            }
        }
        
        GlobalMap.getInstance().registerVNO(vno);   // add to global map
        VNOPool  .getInstance().registerVNO(vno);   // add to pool
        return true;
    }
    
    /**
     * Turn on the vno
     * @return true if successful
     */
    public boolean activateVNO(VNO vno)
    {
        GlobalMap.getInstance().activateVNO(vno);
        return true;
    }
    
    /**
     * Turn off the vno
     * @return true if successful
     */
    public boolean deactivateVNO(VNO vno)
    {
        boolean result = vno.getNetwork().deactivate();
        GlobalMap.getInstance().deactivateVNO(vno);
        return result;
    }
    
    /**
     * Permanently remove the vno
     * @return true if successful
     */
    public boolean decommssionVNO(VNO vno)
    {
        GlobalMap.getInstance().unregisterVNO(vno);
        VNOPool  .getInstance().unregisterVNO(vno);
        return true;
    }

}
