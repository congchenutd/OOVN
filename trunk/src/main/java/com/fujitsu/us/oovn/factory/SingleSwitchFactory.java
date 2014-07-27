package com.fujitsu.us.oovn.factory;

import org.neo4j.graphdb.Node;

import com.fujitsu.us.oovn.core.VNO;
import com.fujitsu.us.oovn.element.Persistable;
import com.fujitsu.us.oovn.element.address.DPID;
import com.fujitsu.us.oovn.element.datapath.PhysicalSwitch;
import com.fujitsu.us.oovn.element.datapath.SingleSwitch;
import com.fujitsu.us.oovn.element.network.PhysicalNetwork;
import com.fujitsu.us.oovn.element.port.PhysicalPort;
import com.fujitsu.us.oovn.element.port.VirtualPort;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SingleSwitchFactory extends ElementFactory {

    @Override
    public Persistable create(Node node, VNO vno)
    {
        DPID dpid = new DPID(node.getProperty("dpid").toString());
        return vno.getNetwork().getSwitch(dpid);
    }

    @Override
    protected Persistable create(JsonObject json, VNO vno)
    {
        DPID dpid = new DPID(json.get("dpid").getAsString());
        try {
            return vno.getNetwork().getSwitch(dpid);
        }
        catch(Exception ex)
        {
            // find physical switch
            DPID phyID = new DPID(json.get("physical").getAsString());
            PhysicalSwitch psw = PhysicalNetwork.getInstance().getSwitch(phyID);
            
            // create virtual switch
            String name = json.get("name").getAsString();
            SingleSwitch vsw = new SingleSwitch(vno, dpid, name);
            vsw.setPhysicalSwitch(psw);
            
            // add virtual ports
            JsonArray portsJson = json.get("ports").getAsJsonArray();
            
            int index = 0;
            for(JsonElement e: portsJson)
            {
                int number = e.getAsInt();
                PhysicalPort port = psw.getPort(number);
                
                // XXX: is it OK to reuse physical MAC address? OVX does so.
                VirtualPort  vPort = new VirtualPort(++index, port.getMACAddress());
                vPort.setPhysicalPort(port);
                vsw.addPort(vPort);
            }
            
            return vsw;
        }
    }

}
