package com.fujitsu.us.oovn;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.Map;

import org.openflow.io.OFMessageAsyncStream;
import org.openflow.protocol.factory.OFMessageFactory;
import org.openflow.util.LRULinkedHashMap;

public class OFSwitch
{
    protected SocketChannel        _channel;    // redundant, _stream has this object, but no getter for it
    protected OFMessageAsyncStream _stream;
    protected Map<Integer, Short>  _macTable;   // mac -> port

    public OFSwitch(SocketChannel channel, OFMessageFactory factory) throws IOException
    {
        _channel = channel;
        _stream  = new OFMessageAsyncStream(channel, factory);
        _macTable = new LRULinkedHashMap<Integer, Short>(64001, 64000);
    }
    
    public SocketChannel getChannel() {
        return _channel;
    }
    
    public OFMessageAsyncStream getStream() {
        return _stream;
    }
    
    public Map<Integer, Short> getMacTable() {
        return _macTable;
    }
    
    @Override
    public String toString()
    {
        Socket socket = _channel.socket();
        return socket.getInetAddress() + ":" + socket.getPort();
    }
}