package com.fujitsu.us.oovn;

import java.io.IOException;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import org.openflow.io.OFMessageAsyncStream;
import org.openflow.protocol.factory.OFMessageFactory;

/**
 * Encapsulate a virtual switch, which corresponds to a physical switch
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 *
 */
public class OFSwitch
{
    /** redundant, _stream has this object, but no getter for it */
    protected SocketChannel        _channel;
    
    /** An encapsulation of a SocketChannel for communication */
    protected OFMessageAsyncStream _stream;

    public OFSwitch(SocketChannel channel, OFMessageFactory factory) throws IOException
    {
        _channel = channel;
        _stream  = new OFMessageAsyncStream(channel, factory);
    }
    
    public SocketChannel getChannel() {
        return _channel;
    }
    
    public OFMessageAsyncStream getStream() {
        return _stream;
    }
    
    @Override
    public String toString()
    {
        Socket socket = _channel.socket();
        return socket.getInetAddress() + ":" + socket.getPort();
    }
}