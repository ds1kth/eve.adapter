package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Message_A1_HandlerTest {

    Message_A1_Handler handler = new Message_A1_Handler();

    @Test
    void A1전문검증_성공케이스(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("12")
                .payloadLength(2)
                .cmd("A1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        int ret = handler.serve(context);

        assertThat(ret).isEqualTo(1);
    }

    @Test
    void A1전문검증_실패케이스_암페어_문자(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("AB")
                .payloadLength(2)
                .cmd("A1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        int ret = handler.serve(context);

        assertThat(ret).isEqualTo(0);
    }

    @Test
    void A1전문검증_실패케이스_암페어_소수(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("12.3")
                .payloadLength(4)
                .cmd("A1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        int ret = handler.serve(context);

        assertThat(ret).isEqualTo(0);
    }
}