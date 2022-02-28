package com.chargev.eve.adapter.socketServer.me;

import io.netty.channel.ChannelInboundHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class MeTcpServerInitializer extends ChannelInitializer<SocketChannel> {

    private final ChannelInboundHandler channelInboundHandler;

    public MeTcpServerInitializer(ChannelInboundHandler channelInboundHandler) {
        this.channelInboundHandler = channelInboundHandler;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        //System.out.println("initChannel");
        ch.pipeline().addLast(
                //new SndLoggingHandler(LogLevel.INFO),
                new ReadTimeoutHandler(2400), // 60*40 = 40 min.
                channelInboundHandler);
    }
}