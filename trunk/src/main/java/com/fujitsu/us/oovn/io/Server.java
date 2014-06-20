package com.fujitsu.us.oovn.io;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Handles connections from switches
 * 
 * Current version does not use multi-thread
 * May need a thread pool in the future
 * 
 * One selector dispatches all the connection requests
 * 
 * A ServerSocketChannel listens to new connections
 * 
 * A event handler decides what to do with a message
 * 
 * @author Cong Chen <Cong.Chen@us.fujitsu.com>
 */
public class Server
{
    /** A socket channel for accepting connections */
    private final ServerSocketChannel _serverChannel;
    
    /** A selector */
    private final Selector _selector;
    
    /** A callback object that handles all the client events */
    protected EventHandler _eventHandler;
 
    /**
     * @param port          the port to listen to
     * @param eventHandler  the callback object
     * @throws IOException
     */
    public Server(int port, EventHandler eventHandler) throws IOException
    {
        _serverChannel = ServerSocketChannel.open();
        _serverChannel.socket().bind(new InetSocketAddress(port));
        _serverChannel.configureBlocking(false);
        
        _selector = Selector.open();
        _serverChannel.register(_selector, SelectionKey.OP_ACCEPT);
        
        _eventHandler = eventHandler;
    }
    
    /**
     * A endless loop for new connections
     * Call this method to start the server
     */
    public void listen()
    {
        try {
            for(;;)
            {
                _selector.select();                   // wait for messages
                Iterator<SelectionKey> iter = _selector.selectedKeys().iterator();
                while(iter.hasNext())
                {
                    SelectionKey key = iter.next();
                    iter.remove();
                    _eventHandler.handleEvent(key);   // callback method
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Accept the pending connection request
     * A controller calls this method when it decides to accept the connection
     * @return A SocketChannel object for the established connection
     * @throws IOException
     */
    public SocketChannel accept() throws IOException {
        return _serverChannel.accept();
    }
    
    /**
     * Register the channel for the operations specified by ops
     * @param switchChannel the channel to be registered
     * @param ops           operations
     */
    public void register(SocketChannel switchChannel, int ops)
    {
        try {
            switchChannel.register(_selector, ops);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}