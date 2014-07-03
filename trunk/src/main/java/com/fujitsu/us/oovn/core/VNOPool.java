package com.fujitsu.us.oovn.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VNOPool
{
    private final Map<Integer, VNO> _vnos;
    
    // singleton
    public static VNOPool getInstance() {
        return LazyHolder._instance;
    }
    
    private static class LazyHolder {
        private static final VNOPool _instance = new VNOPool();
    }
    
    private VNOPool() {
        _vnos = new HashMap<Integer, VNO>();
    }
    
    public VNO getVNO(int vnoID) {
        return _vnos.containsKey(vnoID) ? _vnos.get(vnoID) : null;            
    }
    
    public Map<Integer, VNO> getVNOs() {
        return Collections.unmodifiableMap(_vnos);
    }
    
    public boolean registerVNO(VNO vno)
    {
        if(_vnos.containsKey(vno.getID()))
            return false;
        _vnos.put(vno.getID(), vno);
        return true;
    }
    
    public void unregisterVNO(VNO vno) {
        _vnos.remove(vno.getID());
    }
}
