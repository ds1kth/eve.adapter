package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_O1_Req;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Message_G1_HandlerTest {
    Message_G1_Handler handler = new Message_G1_Handler();

    @Test
    void 단가정보내리기_전문검증_성공케이스() {

        //S202207041208013100012111O2101234567801E
        //                             1234567801
        Message message = Message.builder()
                .chargerId("3100992101")
//                .chargerId("3131342004")
//                .payload("00021010010174790089")
//                .payloadLength(20)
                .cmd("G1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "http://211.252.81.169:8082/v1/control/3100992101");
        handler.serve(context);


        handler.serve(context);
    }


    void sendG1(String chargerId) {
        Message message = Message.builder()
                .chargerId(chargerId)
//                .chargerId("3131342004")
//                .payload("00021010010174790089")
//                .payloadLength(20)
                .cmd("G1").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "http://211.252.81.169:8082/v1/control/" + chargerId);
        handler.serve(context);
    }

    @Test
    void 단가정보내리기_15개() {
        int cnt = 0;
        System.out.println(++cnt);
        sendG1("2009823051");
        System.out.println(++cnt);
        sendG1("2009823041");
        System.out.println(++cnt);
        sendG1("2009823031");
        System.out.println(++cnt);
        sendG1("2009823021");
        System.out.println(++cnt);
        sendG1("2009823011");
        System.out.println(++cnt);
        sendG1("2009822101");
        System.out.println(++cnt);
        sendG1("2009822091");
        System.out.println(++cnt);
        sendG1("2009822081");
        System.out.println(++cnt);
        sendG1("2009822071");
        System.out.println(++cnt);
        sendG1("2009822061");
        System.out.println(++cnt);
        sendG1("2009822051");
        System.out.println(++cnt);
        sendG1("2009822041");
        System.out.println(++cnt);
        sendG1("2009822031");
        System.out.println(++cnt);
        sendG1("2009822021");
        System.out.println(++cnt);
        sendG1("2009822011");
    }
}