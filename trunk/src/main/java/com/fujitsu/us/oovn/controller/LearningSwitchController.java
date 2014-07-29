package com.fujitsu.us.oovn.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openflow.protocol.OFFlowMod;
import org.openflow.protocol.OFMatch;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFPacketOut;
import org.openflow.protocol.OFPort;
import org.openflow.protocol.OFType;
import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionOutput;
import org.openflow.util.U16;

import com.fujitsu.us.oovn.element.address.IPAddress;

/**
 * Making a switch a learning switch
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class LearningSwitchController extends Controller implements Runnable
{
    private final Map<IPAddress, Integer> _groups;
    private boolean _start = false;
    
    /**
     * @param port
     * @throws IOException
     */
    public LearningSwitchController(int port) throws IOException {
        super(port);
        _groups = new HashMap<IPAddress, Integer>();
    }
    
    public void addToGroup(IPAddress ip, int vnoID) {
        _groups.put(ip, vnoID);
    }
    
    private boolean sameGroup(IPAddress ip1, IPAddress ip2)
    {
        if(!_groups.containsKey(ip1) || !_groups.containsKey(ip2))
            return false;
        int vnoID1 = _groups.get(ip1);
        int vnoID2 = _groups.get(ip2);
        return vnoID1 == vnoID2;
    }
    
    public void start() {
        _start = true;
    }
    
    public void stop() {
        _start = false;
    }
    
    
    @Override
    protected void handlePacketIn(OFSwitch sw, OFPacketIn packetIn)
    {
        if(!_start)
            return;
        
        // Build a Match object
        OFMatch match = new OFMatch();
        match.loadFromPacket(packetIn.getPacketData(), packetIn.getInPort());
        
        // get the src and dst ips
        int srcIP = match.getNetworkSource();
        int dstIP = match.getNetworkDestination();
        if(!sameGroup(new IPAddress(srcIP), new IPAddress(dstIP)))
            return;
        
        byte[] dlDst = match.getDataLayerDestination();
        Integer dlDstKey = Arrays.hashCode(dlDst);
        byte[] dlSrc = match.getDataLayerSource();
        Integer dlSrcKey = Arrays.hashCode(dlSrc);
        int bufferId = packetIn.getBufferId();

        // DEBUG:
//        System.out.println("PacketIn from stream " + sw.getStream() + " " + match.getNetworkSource() + 
//                                   "->" + match.getNetworkDestination());
        
        // if the src is not multicast, learn it
        Map<Integer, Short> macTable = sw.getMacTable();
        if ((dlSrc[0] & 0x1) == 0)
        {
            if (!macTable.containsKey(dlSrcKey) ||                        // no entry
                !macTable.get(dlSrcKey).equals(packetIn.getInPort())) {   // wrong port
                macTable.put(dlSrcKey, packetIn.getInPort());             // update entry
            }
            
//            System.out.println("MacTable set: " + match.getNetworkSource() + ":" + packetIn.getInPort());
        }

        Short outPort = null;
        // if the destination is not multicast, look it up
        if ((dlDst[0] & 0x1) == 0) {
            outPort = macTable.get(dlDstKey);
        }

        // modify the switch's flow table with a flow mod message
        // if we know where the packet should be going
        if(outPort != null)
        {
            OFFlowMod flowMod = (OFFlowMod) _factory.getMessage(OFType.FLOW_MOD);
            flowMod.setBufferId(bufferId);
            flowMod.setCommand((short) 0);
            flowMod.setCookie(0);
            flowMod.setFlags((short) 0);
            flowMod.setHardTimeout((short) 5);
            flowMod.setIdleTimeout((short) 5);
            match.setInputPort(packetIn.getInPort());
            match.setWildcards(0);
            flowMod.setMatch(match);
            flowMod.setOutPort(OFPort.OFPP_NONE.getValue());
            flowMod.setPriority((short) 0);
            
            // add a forwarding action
            OFActionOutput action = new OFActionOutput();
            action.setMaxLength((short) 0);
            action.setPort(outPort);
            List<OFAction> actions = new ArrayList<OFAction>();
            actions.add(action);
            flowMod.setActions(actions);
            flowMod.setLength(U16.t(OFFlowMod.MINIMUM_LENGTH + 
                                    OFActionOutput.MINIMUM_LENGTH));
            try {
                sw.getStream().write(flowMod);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
//            System.out.println("FLowMod sent to: " + sw.getStream() + " " +
//                                match.getNetworkSource() + "->" + match.getNetworkDestination() + 
//                                " goes to " + outPort);
        }

        // Forward the packet by sending a packet out message to the switch
        if(outPort == null || packetIn.getBufferId() == 0xffffffff)
        {
            OFPacketOut packetOut = new OFPacketOut();
            packetOut.setBufferId(bufferId);
            packetOut.setInPort(packetIn.getInPort());

            // add a forward action
            OFActionOutput action = new OFActionOutput();
            action.setMaxLength((short) 0);
            action.setPort(outPort == null ? OFPort.OFPP_FLOOD.getValue() 
                                           : outPort);
            List<OFAction> actions = new ArrayList<OFAction>();
            actions.add(action);
            packetOut.setActions(actions);
            packetOut.setActionsLength((short) OFActionOutput.MINIMUM_LENGTH);

            // set data if needed
            if (bufferId == 0xffffffff)
            {
                byte[] packetData = packetIn.getPacketData();
                packetOut.setLength(U16.t(OFPacketOut.MINIMUM_LENGTH + 
                                          packetOut.getActionsLength() + 
                                          packetData.length));
                packetOut.setPacketData(packetData);
            } else {
                packetOut.setLength(U16.t(OFPacketOut.MINIMUM_LENGTH + 
                                          packetOut.getActionsLength()));
            }
            try {
                sw.getStream().write(packetOut);
            } catch (IOException e) {
                e.printStackTrace();
            }
            
//            System.out.println("PacketOut sent to " + sw.getStream() + " " +
//                                action.getPort());
        }
    }
    
//    public static void main(String[] args)
//    {
//        try
//        {
//            LearningSwitchController controller = new LearningSwitchController(6633);
//            controller.addToGroup(new IPAddress("10.0.0.1"), 1);
//            controller.addToGroup(new IPAddress("10.0.0.2"), 1);
//            controller.addToGroup(new IPAddress("10.0.0.3"), 1);
//            controller.addToGroup(new IPAddress("10.0.0.4"), 2);
//            controller.addToGroup(new IPAddress("10.0.0.5"), 2);
//            controller.run();
//        }
//        catch(IOException e) {
//            e.printStackTrace();
//        }
//    }

}
