package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_O1_Req;
import com.chargev.eve.adapter.apiClient.api.Api_O2_Req;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Message_O1_HandlerTest {
    Message_O1_Handler handler = new Message_O1_Handler();

    @Test
    void remoteTransactionStart_전문검증_성공케이스() {

        //S202207041208013100012111O2101234567801E
        //                             1234567801
        Message message = Message.builder()
                .chargerId("2009563011")
//                .chargerId("3131342004")
                .payload("00021010010174790089")
                .payloadLength(20)
                .cmd("O1").build();

//        MessageHandlerContext context = new MessageHandlerContext(message, "http://52.79.78.191:8082/v1/transaction/2009563011");
        MessageHandlerContext context = new MessageHandlerContext(message, "http://211.252.81.169:8082/v1/transaction/2009563011");
        handler.serve(context);

        Api_O1_Req ret = handler.getApiO1Req();
//        assertEquals("chargingProfileId", ret.getChargingProfile());
        assertEquals(1, ret.getConnectorId());
        assertEquals("1010010174790089", ret.getIdTag());

        handler.serve(context);
    }
}