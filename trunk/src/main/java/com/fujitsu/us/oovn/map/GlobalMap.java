package com.fujitsu.us.oovn.map;

import com.fujitsu.us.oovn.core.VNO;

/**
 * A graph holding all the mapping information
 * Possessed by VNOArbitor
 * 
 * A VNO holds a local VNOMap, a subgraph of the GlobalMap
 * 
 * The GlobalMap and the VNOMaps are synced
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class GlobalMap extends MapBase
{
    /**
     * Singleton
     */
    public static GlobalMap getInstance() {
        return LazyHolder._instance;
    }
    
    private static class LazyHolder {
        private static final GlobalMap _instance = new GlobalMap();
    }
    
    private GlobalMap() {
        super("GlobalMap");
    }
    
    /**
     * Verify whether the vno can be created
     * @param vno
     */
    public boolean verifyVNO(VNO vno)
    {
        // Currently, only verify that the mapping is correct
        
        
        return true;
    }

}
