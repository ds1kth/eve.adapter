package com.chargev.eve.adapter.socketServer.me;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

/**
 *
 */
public class MeMessageEncoder extends MessageToByteEncoder<String> {

    private final Charset charset = Charset.forName("UTF-8");

    @Override
    public void encode(ChannelHandlerContext ctx, String msgBuf, ByteBuf out) throws Exception {
        //System.out.println("encode");
        out.writeCharSequence(msgBuf, charset);
    }
}
