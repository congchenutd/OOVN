package com.fujitsu.us.oovn;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.openflow.protocol.OFPacketIn;
import org.openflow.protocol.factory.BasicFactory;

public abstract class Controller implements EventHandler
{
    protected BasicFactory _factory;
    protected Server       _server;
    protected Map<SocketChannel, OFSwitch> _channel2Switch;   // switch objects
    
    public Controller(int port) throws IOException
    {
        _factory        = new BasicFactory();
        _server         = new Server(port, this);
        _channel2Switch = new ConcurrentHashMap<SocketChannel, OFSwitch>();
    }
    
    public void run()
    {
        _server.listen();
    }
    
    @Override
    public void handleEvent(SelectionKey key) throws IOException
    {
        if(key.isAcceptable())
            handleAcceptEvent(key);
        else
            handleSwitchEvent(key);
    }
    
    abstract protected void handleAcceptEvent(SelectionKey key) throws IOException;
    abstract protected void handleSwitchEvent(SelectionKey key);
    abstract protected void handlePacketIn(OFSwitch sw, OFPacketIn packetIn);
}