package com.chargev.eve.adapter.socketServer.me;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class MeTcpServer implements Runnable {

    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    private final int servicePort;
    private final ChannelInboundHandler channelInboundHandler;

    public MeTcpServer(int servicePort, ChannelInboundHandler handler) {
        this.servicePort = servicePort;
        this.channelInboundHandler = handler;
    }

    @Override
    public void run() {
        startServer();
    }

    public void shutdown() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

    public void startServer() {

        log.info("SERVICE_PORT={}", servicePort);

        // Configure the bootstrap.
        Class clazzChannel;
        if (Epoll.isAvailable()) {
            log.info("use EpollEventLoopGroup");
            bossGroup = new EpollEventLoopGroup(2);
            workerGroup = new EpollEventLoopGroup(16);
            clazzChannel = EpollServerSocketChannel.class;
        } else {
            log.info("use NioEventLoopGroup");
            bossGroup = new NioEventLoopGroup(2);
            workerGroup = new NioEventLoopGroup(16);
            clazzChannel = NioServerSocketChannel.class;
        }

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(clazzChannel)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new MeMessageDecoder(), new MeMessageEncoder(), new MeTcpServerInitializer(channelInboundHandler));
                            //p.addLast(new MeMessageDecoder(), new TestHandler());
                            //p.addLast(new TestHandler(), new MeMessageDecoder());
                            //p.addLast(new MeMessageDecoder());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1024) // 동시에 접속요청을 처리할 수 있는 갯수(동시접속수가 아님)
                    .option(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_LINGER, 0)
                    .childOption(ChannelOption.AUTO_READ, true)
                    .bind(servicePort).sync().channel().closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("FATAL ERROR", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}