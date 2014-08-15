package com.fujitsu.us.oovn.core.io;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class Server
{
    private final int port;

    public Server(int port) {
        this.port = port;
    }

    public void run() throws Exception
    {
        EventLoopGroup bossGroup   = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try
        {
            ServerBootstrap bs = new ServerBootstrap();
            bs.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInitializer<SocketChannel>() {
                 @Override
                 public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast("framer",
                                         new DelimiterBasedFrameDecoder(8192,
                                                                        Delimiters.lineDelimiter()));
                        pipeline.addLast("decoder", new StringDecoder());
                        pipeline.addLast("encoder", new StringEncoder());
                        pipeline.addLast("handler", new SwitchChannelHandler());
                 }
             })
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.TCP_NODELAY,  true)
             .childOption(ChannelOption.SO_KEEPALIVE, true);

            bs.bind(port).sync().channel().closeFuture().sync();
        } 
        finally
        {
            workerGroup.shutdownGracefully();
            bossGroup  .shutdownGracefully();
        }
    }
}


class SwitchChannelHandler extends ChannelHandlerAdapter
{
    
}
