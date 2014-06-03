package com.fujitsu.us.oovn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

public class LearningSwitchController extends Controller
{

    public LearningSwitchController(int port) throws IOException {
        super(port);
    }
    
    @Override
    protected void handlePacketIn(OFSwitch sw, OFPacketIn packetIn)
    {
        // Build the Match
        OFMatch match = new OFMatch();
        match.loadFromPacket(packetIn.getPacketData(), packetIn.getInPort());
        byte[] dlDst = match.getDataLayerDestination();
        Integer dlDstKey = Arrays.hashCode(dlDst);
        byte[] dlSrc = match.getDataLayerSource();
        Integer dlSrcKey = Arrays.hashCode(dlSrc);
        int bufferId = packetIn.getBufferId();

        // if the src is not multicast, learn it
        Map<Integer, Short> macTable = sw.getMacTable();
        if ((dlSrc[0] & 0x1) == 0)
        {
            if (!macTable.containsKey(dlSrcKey) ||                        // no entry
                !macTable.get(dlSrcKey).equals(packetIn.getInPort())) {   // wrong port
                macTable.put(dlSrcKey, packetIn.getInPort());             // update entry
            }
        }

        Short outPort = null;
        // if the destination is not multicast, look it up
        if ((dlDst[0] & 0x1) == 0) {
            outPort = macTable.get(dlDstKey);
        }

        // push a flow mod if we know where the packet should be going
        if (outPort != null)
        {
            OFFlowMod flowMod = (OFFlowMod) _factory.getMessage(OFType.FLOW_MOD);
            flowMod.setBufferId(bufferId);
            flowMod.setCommand((short) 0);
            flowMod.setCookie(0);
            flowMod.setFlags((short) 0);
            flowMod.setHardTimeout((short) 0);
            flowMod.setIdleTimeout((short) 5);
            match.setInputPort(packetIn.getInPort());
            match.setWildcards(0);
            flowMod.setMatch(match);
            flowMod.setOutPort(OFPort.OFPP_NONE.getValue());
            flowMod.setPriority((short) 0);
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
        }

        // Send a packet out
        if (outPort == null || packetIn.getBufferId() == 0xffffffff)
        {
            OFPacketOut packetOut = new OFPacketOut();
            packetOut.setBufferId(bufferId);
            packetOut.setInPort(packetIn.getInPort());

            // set actions
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
        }
    }
    
    public static void main(String[] args)
    {
        try
        {
            LearningSwitchController controller = new LearningSwitchController(6634);
            controller.run();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

}
