package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_K1_Req;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Message_I1_HandlerTest {
    Message_I1_Handler handler = new Message_I1_Handler();

    @Test
    void reset_전문검증_성공케이스(){
        //S202206072257115101382004K165195101007246411220220603001300202206030913005492147             CE
        //                             195101007246411220220603001300202206030913005492147             C
        //S202206101333123100462103R10 E
        Message message = Message.builder()
                .chargerId("3131342004")
                .payload("10 ")
                .payloadLength(3)
                .cmd("I1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "http://211.252.81.169:8082/v1/control/3131342004");
        //MessageHandlerContext context = new MessageHandlerContext(message, "http://52.79.78.191:8082/v1/control/3131342004");
        handler.serve(context);
    }
}