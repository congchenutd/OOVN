package com.fujitsu.us.oovn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class Server
{
    private final ServerSocketChannel _serverChannel;
    private final Selector            _selector;
    protected     EventHandler        _eventHandler;
 
    public Server(int port, EventHandler eventHandler) throws IOException
    {
        _serverChannel = ServerSocketChannel.open();
        _serverChannel.socket().bind(new InetSocketAddress(port));
        _serverChannel.configureBlocking(false);
        
        _selector = Selector.open();
        _serverChannel.register(_selector, SelectionKey.OP_ACCEPT);
        
        _eventHandler = eventHandler;
    }
    
    public void listen()
    {
        try {
            for(;;)
            {
                _selector.select();
                Iterator<SelectionKey> iter = _selector.selectedKeys().iterator();
                while(iter.hasNext())
                {
                    SelectionKey key = iter.next();
                    iter.remove();
                    _eventHandler.handleEvent(key);
                }
            }
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
    
    public SocketChannel accept() throws IOException {
        return _serverChannel.accept();
    }
    
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