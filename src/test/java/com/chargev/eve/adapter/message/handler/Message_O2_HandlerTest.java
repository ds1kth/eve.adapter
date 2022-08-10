package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_K1_Req;
import com.chargev.eve.adapter.apiClient.api.Api_O2_Req;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Message_O2_HandlerTest {
    Message_O2_Handler handler = new Message_O2_Handler();

    @Test
    void remoteTransactionStop_전문검증_성공케이스() {
        //S202207041208013100012111O2101234567801E
        //                             1234567801

        //S202207041208013100012111O2101234567890123401E
        //                           12345678        01E
        //                           1234567890123456
        Message message = Message.builder()
                .chargerId("2009563011")
                .payload("6235751         02")
                .payloadLength(18)
                .cmd("O2").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "http://52.79.78.191:8082/v1/controll/2009563011");
//        MessageHandlerContext context = new MessageHandlerContext(message, "http://52.79.78.191:8082/v1/transaction/2009563011");
//        MessageHandlerContext context = new MessageHandlerContext(message, "http://211.252.81.169:8082/v1/transaction/2008692001");
        handler.serve(context);

        Api_O2_Req ret = handler.getApiO2Req();
        assertEquals(12345678, ret.getTransactionId());
        assertEquals(1, ret.getConnectorId());

        handler.serve(context);
    }
}