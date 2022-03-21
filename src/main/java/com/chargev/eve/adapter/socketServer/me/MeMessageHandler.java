package com.chargev.eve.adapter.socketServer.me;

import javax.annotation.Nonnull;

import com.chargev.eve.adapter.AdapterApplicationProperties;
import com.chargev.eve.adapter.AppContext;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
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

    @Autowired
    MeMessagePreProcessor meMessagePreProcessor;

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
        String date = MeMessageDecoder.parseDate(msgBuf);
        String chargeId = MeMessageDecoder.parseChargerId(msgBuf);
        String cmd = MeMessageDecoder.parseCmd(msgBuf);

        cmd = meMessagePreProcessor.process(cmd);
        int payloadLength = MeMessageDecoder.getPayloadLength(msgBuf);

        String beanName = "Message_" + cmd + "_Handler";

        MessageHandler handler = AppContext.getBean(beanName, MessageHandler.class);
        Message message = Message.builder()
                .chargerId(chargeId)
                .payload(MeMessageDecoder.parsePayload(msgBuf, payloadLength))
                .payloadLength(payloadLength)
                .cmd(cmd).build();

        String serverDomain = "http://"
                + adapterApplicationProperties.getApiServerAddress() 
                + ':'
                + adapterApplicationProperties.getApiServerPort()
                + "/v1/control/"
                + message.getChargerId();
        MessageHandlerContext context = new MessageHandlerContext(message, serverDomain);

        Integer ret = (Integer)handler.serve(context);
        if(ret == 0)
            return;

        // String readMessage = ((ByteBuf) msg).toString(Charset.defaultCharset());
        // //3. 수신된 데이터를 가지고 있는 네티의 바이트 버퍼 객체로 부터 문자열 객체를 읽어온다.
        // System.out.println("수신한 문자열 ["+readMessage +"]");
        // STX + INS + ML + VD + ETX
        // resByte = MathManager.StringToBytes(strSTX + strSendDate + strStationID + strChargerID + strINS + strML + strVD + strETX);

        if(context.getRespMessage() != null){

            String INS = context.getRespMessage().getINS();
            String ML = context.getRespMessage().getML();
            String VD = context.getRespMessage().getVD();

            String returnMsg = "S" + date + chargeId + INS + ML + VD + "E";
            logger.info("[adapter to web]INS : {}, ML : {}, VD : {}", INS, ML, VD);
            logger.info("packet : {}", returnMsg);
            System.out.println(msg);
            ctx.write(returnMsg);
//            ChannelFuture future = ctx.writeAndFlush(test);
//            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx){        
        logger.info("channelReadComplete");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.info("exceptionCaught");
        closeOnFlush(ctx.channel());
    }

    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    private static void closeOnFlush(Channel ch) {
        System.out.println("closeOnFlush");
         if (ch.isActive()) {
             ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
         }
    }
}
