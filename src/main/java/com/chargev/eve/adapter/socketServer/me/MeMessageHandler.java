package com.chargev.eve.adapter.socketServer.me;

import javax.annotation.Nonnull;

import com.chargev.eve.adapter.AdapterApplicationProperties;
import com.chargev.eve.adapter.AppContext;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ChannelHandler.Sharable
public class MeMessageHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AdapterApplicationProperties adapterApplicationProperties;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //System.out.println("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        //System.out.println("channelInactive");
    }

    @Override
    public void channelRead(final ChannelHandlerContext ctx, @Nonnull final Object msg) {
        final byte[] msgBuf = (byte[]) msg;

        //System.out.println("channelRead");
        //System.out.println(msgBuf.length);

        String str2 = new String(msgBuf);
        String cmd = MeMessageDecoder.parseCmd(msgBuf);
        String beanName = "Message_" + cmd + "_Handler";

        MessageHandler handler = AppContext.getBean(beanName, MessageHandler.class);
        Message message = Message.builder()
                .chargerId(MeMessageDecoder.parseChargerId(msgBuf))
                .payload(MeMessageDecoder.parsePayload(msgBuf, msgBuf.length))
                .cmd(cmd).build();

        String serverDomain = "http://" + adapterApplicationProperties.getApiServerAddress() + ':' + adapterApplicationProperties.getApiServerPort();
        MessageHandlerContext context = new MessageHandlerContext(message, serverDomain);
        handler.serve(context);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        closeOnFlush(ctx.channel());
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    private static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }
}
