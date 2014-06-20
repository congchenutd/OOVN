package com.fujitsu.us.oovn.controller;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openflow.io.OFMessageAsyncStream;
import org.openflow.protocol.OFEchoReply;
import org.openflow.protocol.OFMessage;
import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.OFType;
import org.openflow.protocol.factory.BasicFactory;

import com.fujitsu.us.oovn.io.EventHandler;
import com.fujitsu.us.oovn.io.Server;

/**
 * Base class for controllers
 * Implements EventHandler, a callback for Server
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public abstract class Controller implements EventHandler
{
    /** for generating OFMessage */
    protected BasicFactory _factory;
    
    /** for accepting connections and messages */
    protected Server       _server;
    
    /** Virtual switches */
    protected Map<SocketChannel, OFSwitch> _channel2Switch;
    
    
    /**
     * @param port  the port to work on
     * @throws IOException
     */
    public Controller(int port) throws IOException
    {
        _factory        = new BasicFactory();
        _server         = new Server(port, this);
        _channel2Switch = new ConcurrentHashMap<SocketChannel, OFSwitch>();
    }
    
    /**
     * Start the controller
     */
    public void run() {
        _server.listen();
    }

    /**
     * Handles event wrapped by key
     * @param key   A SelectionKey representing the event
     */
    @Override
    public void handleEvent(SelectionKey key) throws IOException
    {
        if(key.isAcceptable())
            handleAcceptEvent(key);   // new connection event
        else
            handleSwitchEvent(key);
    }
    
    /**
     * Handles new connection event
     * @param key   A SelectionKey representing the event
     * @throws IOException
     */
    protected void handleAcceptEvent(SelectionKey key) throws IOException
    {
        // ask the server to accept the connection, and register the channel for READ
        SocketChannel switchChannel = _server.accept();
        switchChannel.configureBlocking(false);

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

    
    /**
     * Handles other events from the switches
     * @param key
     */
    protected void handleSwitchEvent(SelectionKey key)
    {
        SocketChannel channel = (SocketChannel) key.channel();
        OFSwitch sw = _channel2Switch.get(channel);
        OFMessageAsyncStream stream = sw.getStream();
        try
        {
            // read events from the switches
            if(key.isReadable())
            {
                List<OFMessage> messages = stream.read();
                if(messages == null)
                {
                    key.cancel();
                    _channel2Switch.remove(channel);
                    return;
                }

                for(OFMessage message : messages)
                {
                    switch(message.getType())
                    {
                        case PACKET_IN:
                            // leave to subclasses
                            handlePacketIn(sw, (OFPacketIn) message);
                            break;
                            
                            // simple events are handled here
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
            
            // write
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
    
    
    /**
     * Complex events are left for subclasses
     * @param sw        switch
     * @param packetIn  PacketIn message
     */
    abstract protected void handlePacketIn(OFSwitch sw, OFPacketIn packetIn);
}