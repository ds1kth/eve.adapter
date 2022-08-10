package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_K1_Req;
import com.chargev.eve.adapter.apiClient.api.Api_S3_Req;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Message_K1_HandlerTest {

    Message_K1_Handler handler = new Message_K1_Handler();

    // @Test
    // void K1전문검증_성공케이스_ML_86(){
    //     Message message = Message.builder()
    //             .chargerId("3100012111")
    //             .payload("1010010006643191202205201250002022052013500000000000000005324050R000100000010000000000")
    //             .payloadLength(86)
    //             .cmd("K1").build();

    //     MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
    //     handler.serve(context);

    //     Api_K1_Req ret = handler.getApiK1Req();
    //     String ExpectedUserUID = "1010010006643191";
    //     String ExpectedStartDate = "20220520125000";
    //     String ExpectedEndDate = "20220520135000";
    //     String ExpectedDBUniq = "00000000000005324050";
    //     String ExpectedResCan = "R";

    //     assertEquals(ExpectedUserUID, ret.getBytUserUID());
    //     assertEquals(ExpectedStartDate, ret.getBytStartDate());
    //     assertEquals(ExpectedEndDate, ret.getBytEndDate());
    //     assertEquals(ExpectedDBUniq, ret.getBytDBUniq());
    //     assertEquals(ExpectedResCan, ret.getBytResCan());
    // }

    // @Test
    // void K1전문검증_성공케이스_ML_65(){
    //     Message message = Message.builder()
    //             .chargerId("3100012111")
    //             .payload("101001000664319120220520113000202205201230005323407             R")
    //             .payloadLength(65)
    //             .cmd("K1").build();

    //     MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
    //     handler.serve(context);

    //     Api_K1_Req ret = handler.getApiK1Req();
    //     String ExpectedUserUID = "1010010006643191";
    //     String ExpectedStartDate = "20220520113000";
    //     String ExpectedEndDate = "20220520123000";
    //     String ExpectedDBUniq = "5323407             ";
    //     String ExpectedResCan = "R";

    //     assertEquals("3100012111", ret.getBytChargerId());
    //     assertEquals(ExpectedUserUID, ret.getBytUserUID());
    //     assertEquals(ExpectedStartDate, ret.getBytStartDate());
    //     assertEquals(ExpectedEndDate, ret.getBytEndDate());
    //     assertEquals(ExpectedDBUniq, ret.getBytDBUniq());
    //     assertEquals(ExpectedResCan, ret.getBytResCan());
    // }    

    @Test
    void K1전문검증_성공케이스(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("1010010006643191202205201250002022052013500000000000000005324050R000100000010000000000")
                .payloadLength(86)
                .cmd("K1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        handler.serve(context);

        Api_K1_Req ret = handler.getApiK1Req();
        String ExpectedChargerId = "3100012111";
        String ExpectedContentType = "RESERVATION";

        assertEquals(ExpectedChargerId, ret.getChargerId());
        assertEquals(ExpectedContentType, ret.getContentType());
    }

    @Test
    void K1전문검증_취소_성공케이스(){
        //S202206072257115101382004K165195101007246411220220603001300202206030913005492147             CE
        //                             195101007246411220220603001300202206030913005492147
        //S202207151042592003422018K165101001018465658520220715100016202207151500166064317             CE
        //                             101001018465658520220715100016202207151500166064317             CE
        Message message = Message.builder()
                .chargerId("2003422018")
                .payload("101001018465658520220715100016202207151500166064317             CE")
                .payloadLength(65)
                .cmd("K1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "http://52.79.78.191:8082/v1/content");
        handler.serve(context);

        Api_K1_Req ret = handler.getApiK1Req();
        String ExpectedChargerId = "2003422018";
        String ExpectedContentType = "not_reservation";

        assertEquals(ExpectedChargerId, ret.getChargerId());
        assertEquals(ExpectedContentType, ret.getContentType());
    }
}