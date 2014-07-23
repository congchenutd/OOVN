package com.fujitsu.us.oovn.map;

/**
 * A graph holding all the mapping information
 * 
 * Possessed and accessible only by VNOArbitor
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

}
