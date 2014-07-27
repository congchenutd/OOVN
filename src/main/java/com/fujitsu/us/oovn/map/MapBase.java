package com.fujitsu.us.oovn.map;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.query.RestCypherQueryEngine;
import org.neo4j.rest.graphdb.query.RestQueryResult;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.address.PhysicalIPAddress;
import com.fujitsu.us.oovn.element.address.VirtualIPAddress;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.VirtualSwitch;
import com.fujitsu.us.oovn.element.link.PhysicalLink;
import com.fujitsu.us.oovn.element.link.VirtualLink;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.fujitsu.us.oovn.factory.ElementFactory;

/**
 * Base class for all the maps
 * 
 * Internally a Neo4j graph holding all the mapping information
 * All query operations are implemented using Neo4j queries
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class MapBase
{
    
    /**
     * @param vsw a VirtualSwitch
     * @return a list of PhysicalSwitches vsw maps to
     */
    public List<PhysicalSwitch> getPhysicalSwitches(VirtualSwitch vsw)
    {
        try(Transaction tx = _graphDb.beginTx())
        {
            RestQueryResult result = query(
                    "MATCH " + vsw.toDBMatch() + "-[:Maps]->(psw) " + 
                    "RETURN psw");
    
            Iterator<Node> it = result.to(Node.class).iterator();
            List<PhysicalSwitch> switches = new LinkedList<PhysicalSwitch>();
            while (it.hasNext())
                switches.add((PhysicalSwitch) ElementFactory.fromNode(it.next()));
    
            return switches;
        }
    }
    
    /**
     * @param psw a PhysicalSwitch
     * @param vno a VNO
     * @return the VirtualSwitch of the vno that maps to maps to psw
     */
    public VirtualSwitch getVirtualSwitch(PhysicalSwitch psw, VNO vno)
    {
        try(Transaction tx = _graphDb.beginTx())
        {
            RestQueryResult result = query(
                    "MATCH " + psw.toDBMatch() + 
                    "<-[:Maps]-(vsw {vnoid:" + vno.getID() + "}) " +
                    "RETURN vsw");
            
            Iterator<Node> it = result.to(Node.class).iterator();
            return it.hasNext() ? (VirtualSwitch) ElementFactory.fromNode(it.next(), vno)
                                : null;
        }
    }
    
    /**
     * @param vPort a VirtualPort
     * @return the PhysicalPort vPort maps to
     */
    public PhysicalPort getPhysicalPort(VirtualPort vPort)
    {
        try(Transaction tx = _graphDb.beginTx())
        {
            RestQueryResult result = query(
                    "MATCH " + vPort.toDBMatch() + 
                    "-[:Maps]->(pPort) " +
                    "RETURN pPort");
            
            Iterator<Node> it = result.to(Node.class).iterator();
            return it.hasNext() ? (PhysicalPort) ElementFactory.fromNode(it.next())
                                : null;
        }
    }
    
    /**
     * @param pPort a PhysicalPort
     * @param vno a VNO
     * @return the VirtualPort of vno that maps to pPort
     */
    public VirtualPort getVirtualPort(PhysicalPort pPort, VNO vno)
    {
        try(Transaction tx = _graphDb.beginTx())
        {
            RestQueryResult result = query(
                    "MATCH " + pPort.toDBMatch() + 
                    "<-[:Maps]-(vPort {vnoid:" + vno.getID() + "}) " +
                    "RETURN vPort");
            
            Iterator<Node> it = result.to(Node.class).iterator();
            return it.hasNext() ? (VirtualPort) ElementFactory.fromNode(it.next(), vno)
                                : null;
        }
    }
    
    /**
     * @param vLink a VirtualLink
     * @return a list of PhysicalLinks that vLink maps to
     */
    public List<PhysicalLink> getPhysicalLinks(VirtualLink vLink)
    {
        try(Transaction tx = _graphDb.beginTx())
        {
            RestQueryResult result = query(
                    "MATCH " + vLink.toDBMatch() + 
                    "-[m:Maps]->(pLink:ZPhysical:Link) " +
                    "RETURN pLink " +
                    "ORDER BY m.order");
            
            Iterator<Node> it = result.to(Node.class).iterator();
            List<PhysicalLink> links = new LinkedList<PhysicalLink>();
            while(it.hasNext())
                links.add((PhysicalLink) ElementFactory.fromNode(it.next()));

            return links;
        }
    }
    
    /**
     * @param pLink a PhysicalLink
     * @param vno   a VNO
     * @return the VirtualLink of the vno that maps to pLink
     */
    public VirtualLink getVirtualLink(PhysicalLink pLink, VNO vno)
    {
        try(Transaction tx = _graphDb.beginTx())
        {
            RestQueryResult result = query(
                    "MATCH " + pLink.toDBMatch() + 
                    "<-[:Maps]-(vLink {vnoid:" + vno.getID() + "}) " +
                    "RETURN vLink");
            
            Iterator<Node> it = result.to(Node.class).iterator();
            return it.hasNext() ? (VirtualLink) ElementFactory.fromNode(it.next(), vno)
                                : null;
        }
    }
    
    public PhysicalIPAddress getPhysicalIPAddress(VirtualIPAddress vIP)
    {
        return null;
    }
    
    public VirtualIPAddress getVirtualIPAddress(PhysicalIPAddress pIP, VNO vno)
    {
        return null;
    }
    
    /**
     * Add a VNO to the map 
     * @param vno
     */
    public void registerVNO(VNO vno)
    {
        if(!vno.isVerified())
            return;
        
        try(Transaction tx = _graphDb.beginTx())
        {
            vno.getNetwork().createInDB(this);
            tx.success();
        }
        
        deactivateVNO(vno);
    }
    
    /**
     * Remove a VNO from the map
     * @param vno
     */
    public void unregisterVNO(VNO vno)
    {
        try(Transaction tx = _graphDb.beginTx())
        {
            // remove all the virtual nodes with the given vno id
            query("MATCH (n:Virtual {vnoid:" + vno.getID() +  "}) " +
                  "OPTIONAL MATCH (n)-[r]-() " + 
                  "DELETE n,r");
            tx.success();
        }
    }
    
    /**
     * Activate a VNO by setting the "activated" property of all the nodes to true
     */
    public void activateVNO(VNO vno)
    {
        try(Transaction tx = _graphDb.beginTx())
        {
            query("MATCH (n:Virtual {vnoid:" + vno.getID() + "}) " +
                  "SET n.activated=true");
            tx.success();
        }
    }
    
    /**
     * Deactivate a VNO by setting the "activated" property of all the nodes to false
     */
    public void deactivateVNO(VNO vno)
    {
        try(Transaction tx = _graphDb.beginTx())
        {
            query("MATCH (n:Virtual {vnoid:" + vno.getID() + "}) " +
                  "SET n.activated=false");
            tx.success();
        }
    }
    
    protected final RestGraphDatabase     _graphDb;
    protected final RestCypherQueryEngine _engine;
    
    public RestQueryResult query(String query) {
//        System.out.println(query);
        return (RestQueryResult) _engine.query(query, null);
    }
        
    protected MapBase()
    {
        _graphDb = new RestGraphDatabase("http://localhost:7474/db/data/");
        _engine  = new RestCypherQueryEngine(_graphDb.getRestAPI());
        registerShutdownHook(_graphDb);
    }
    
    /**
     * Registers a shutdown hook for the Neo4j instance so that it shuts down nicely 
     * when the VM exits (even if you "Ctrl-C" the running application).
     */
    private static void registerShutdownHook(final GraphDatabaseService graphDb)
    {
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        } );
    }
}
