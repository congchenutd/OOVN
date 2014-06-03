package com.fujitsu.us.oovn;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import org.openflow.io.OFMessageAsyncStream;
import org.openflow.protocol.OFEchoReply;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFPacketOut;
import org.openflow.protocol.OFPort;
import org.openflow.protocol.OFType;
import org.openflow.protocol.action.OFAction;
import org.openflow.protocol.action.OFActionOutput;

public class HubController extends Controller
{
    public HubController(int port) throws IOException {
        super(port);
    }
    
    @Override
    protected void handleAcceptEvent(SelectionKey key) throws IOException
    {
        // ask the server to accept the connection, and register the selector for READ
        SocketChannel switchChannel = _server.accept();
        switchChannel.configureBlocking(false);
        _server.register(switchChannel, SelectionKey.OP_READ);

        // create a switch object
        OFSwitch sw = new OFSwitch(switchChannel, _factory);
        _channel2Switch.put(switchChannel, sw);
        
        // greet the switch
        System.out.println("Got new connection from " + sw);
        List<OFMessage> messages = new ArrayList<OFMessage>();
        messages.add(_factory.getMessage(OFType.HELLO));
        messages.add(_factory.getMessage(OFType.FEATURES_REQUEST));
        sw.getStream().write(messages);

        // register for WRITE
        int ops = SelectionKey.OP_READ;
        if (sw.getStream().needsFlush())
            ops |= SelectionKey.OP_WRITE;

        _server.register(switchChannel, ops);
    }

    @Override
    protected void handleSwitchEvent(SelectionKey key)
    {
        SocketChannel channel = (SocketChannel) key.channel();
        OFSwitch sw = _channel2Switch.get(channel);
        OFMessageAsyncStream stream = sw.getStream();
        try
        {
            if(key.isReadable())
            {
                List<OFMessage> messages = stream.read();
                if (messages == null)
                {
                    key.cancel();
                    _channel2Switch.remove(channel);
                    return;
                }

                for (OFMessage message : messages)
                {
                    switch (message.getType())
                    {
                        case PACKET_IN:
                            handlePacketIn(sw, (OFPacketIn) message);
                            break;
                        case HELLO:
                            System.out.println("GOT HELLO from " + sw);
                            break;
                        case ECHO_REQUEST:
                            OFEchoReply reply = (OFEchoReply) stream.getMessageFactory()
                                                                        .getMessage(OFType.ECHO_REPLY);
                            reply.setXid(message.getXid());
                            stream.write(reply);
                            break;
                        default:
                            System.out.println("Unhandled OF message: " +
                                                message.getType() + " from " +
                                                channel.socket().getInetAddress());
                    }
                }
            }
            if (key.isWritable()) {
                stream.flush();
            }

            // Only register for R OR W, not both, to avoid stream deadlock
            key.interestOps(stream.needsFlush() ? SelectionKey.OP_WRITE 
                                                : SelectionKey.OP_READ);
        }
        catch (IOException e)
        {
            // if we have an exception, disconnect the switch
            key.cancel();
            _channel2Switch.remove(channel);
        }
    }
    
    @Override
    protected void handlePacketIn(OFSwitch sw, OFPacketIn packetIn)
    {
        // Send a packet out
        OFPacketOut packetOut = new OFPacketOut();
        packetOut.setBufferId(packetIn.getBufferId());
        packetOut.setInPort(packetIn.getInPort());

        // set actions
        OFActionOutput action = new OFActionOutput();
        action.setMaxLength((short) 0);
        action.setPort(OFPort.OFPP_FLOOD.getValue());
        List<OFAction> actions = new ArrayList<OFAction>();
        actions.add(action);
        packetOut.setActions(actions);
        packetOut.setActionsLength((short) OFActionOutput.MINIMUM_LENGTH);

        try {
            sw.getStream().write(packetOut);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args)
    {
        try
        {
            HubController controller = new HubController(6634);
            controller.run();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

}

