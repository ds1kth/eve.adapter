package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Message_S4_HandlerTest {
    Message_S4_Handler handler = new Message_S4_Handler();

    @Test
    void S3전문검증_성공케이스_ml사이즈_224(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("221S567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234567890" +
                    "1234")
                .payloadLength(99)
                .cmd("S4").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        int ret = handler.serve(context);

        assertThat(ret).isEqualTo(1);
    }
}