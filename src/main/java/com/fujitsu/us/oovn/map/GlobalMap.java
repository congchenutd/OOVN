package com.fujitsu.us.oovn.map;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.PhysicalIPAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.VirtualSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.link.VirtualLink;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;

/**
 * A graph holding all the mapping information
 * Possessed by VNOArbitor
 * 
 * A VNO holds a LocalMap, a subgraph of the GlobalMap
 * 
 * The GlobalMap and the LocalMaps are synced
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class GlobalMap
{
    
    public PhysicalSwitch getPhysicalSwitch(VirtualSwitch vsw)
    {
        return null;
    }
    
    public VirtualSwitch getVirtualSwitch(PhysicalSwitch psw, VNO vno)
    {
        return null;
    }
    
    public PhysicalPort getPhysicalPort(VirtualPort vport)
    {
        return null;
    }
    
    public VirtualPort getVirtualPort(PhysicalPort pport, VNO vno)
    {
        return null;
    }
    
    public PhysicalLink getPhysicalLink(VirtualLink vlink)
    {
        return null;
    }
    
    public VirtualLink getVirtualLink(PhysicalLink plink, VNO vno)
    {
        return null;
    }
    
    public PhysicalIPAddress getPhysicalIPAddress(VirtualIPAddress vip)
    {
        return null;
    }
    
    public VirtualIPAddress getVirtualIPAddress(PhysicalIPAddress pip, VNO vno)
    {
        return null;
    }

    /**
     * Verify whether the vno can be created
     * @param vno
     */
    public boolean verifyVNO(VNO vno)
    {
        return true;
    }

    /**
     * Add a VNO to the universe 
     * @param vno
     */
    public void registerVNO(VNO vno)
    {
        
    }
    
    /**
     * Remove a VNO from the universe
     * @param vno
     */
    public void unregisterVNO(VNO vno)
    {
        
    }
    
    // singleton
    public static GlobalMap getInstance() {
        return LazyHolder._instance;
    }
    
    private static class LazyHolder {
        private static final GlobalMap _instance = new GlobalMap();
    }
    
    private final GraphDatabaseService _graphDb;
    private final ExecutionEngine      _engine;
    
    /**
     * Initiate the map from the PhysicalNetwork
     */
    private GlobalMap()
    {
        _graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("db");
        _engine  = new ExecutionEngine(_graphDb);
        registerShutdownHook(_graphDb);
        
        try(Transaction tx = _graphDb.beginTx())
        {
            _engine.execute("CREATE " + PhysicalNetwork.getInstance().toDBCreate());
            tx.success();
        }
    }
    
    public static void main(String argvs[])
    {
        GlobalMap.getInstance();
    }
    
    private static void registerShutdownHook(final GraphDatabaseService graphDb)
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running application).
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        } );
    }
}