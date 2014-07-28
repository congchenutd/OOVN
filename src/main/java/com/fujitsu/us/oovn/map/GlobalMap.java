package com.fujitsu.us.oovn.map;

import org.neo4j.graphdb.Transaction;

import com.fujitsu.us.oovn.element.network.PhysicalNetwork;

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
    
    private GlobalMap()
    {
        super();
        try(Transaction tx = _graphDb.beginTx())
        {
            clear();
            PhysicalNetwork.getInstance().createInDB(this);
            tx.success();
        }
    }

}
