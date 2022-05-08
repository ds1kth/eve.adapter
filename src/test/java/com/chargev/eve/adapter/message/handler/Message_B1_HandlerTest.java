package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Message_B1_HandlerTest {

    Message_B1_Handler handler = new Message_B1_Handler();

    @Test
    void B1전문검증_성공케이스_모드R(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("R")
                .payloadLength(1)
                .cmd("B1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        int ret = handler.serve(context);

        assertThat(ret).isEqualTo(1);
    }

    @Test
    void B1전문검증_성공케이스_모드S(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("S")
                .payloadLength(1)
                .cmd("B1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        int ret = handler.serve(context);

        assertThat(ret).isEqualTo(1);
    }

    @Test
    void B1전문검증_성공케이스_모드C(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("C")
                .payloadLength(1)
                .cmd("B1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        int ret = handler.serve(context);

        assertThat(ret).isEqualTo(1);
    }

    @Test
    void B1전문검증_성공케이스_모드T(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("T")
                .payloadLength(1)
                .cmd("B1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        int ret = handler.serve(context);

        assertThat(ret).isEqualTo(1);
    }

    @Test
    void B1전문검증_성공케이스_실패(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("O")
                .payloadLength(1)
                .cmd("B1").build();

        MessageHandlerContext context = new MessageHandlerContext(message,"192.168.0.1");
        int ret = handler.serve(context);

        assertThat(ret).isEqualTo(0);
    }
}