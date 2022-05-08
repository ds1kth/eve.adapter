package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Message_S3_HandlerTest {

    Message_S3_Handler handler = new Message_S3_Handler();

    @Test
    void S3전문검증_성공케이스_ml사이즈_21(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("S1234567890!@#$%^&*()|")
                .payloadLength(21)
                .cmd("S3").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        int ret = handler.serve(context);

        assertThat(ret).isEqualTo(1);
    }
}