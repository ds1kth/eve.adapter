package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_O1_Req;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Message_C1_HandlerTest {
    Message_C1_Handler handler = new Message_C1_Handler();

    // @Test
    // void isOcpp_성공케이스1() {

    //     //S202207041208013100012111O2101234567801E
    //     //                             1234567801
    //     Message message = Message.builder()
    //             .chargerId("2009563011")
    //             .payload("00011010010174790089")
    //             .payloadLength(20)
    //             .cmd("O1").build();

    //     MessageHandlerContext context = new MessageHandlerContext(message, "127.0.0.1");

    //     boolean ret = handler.isOcpp(context);

    //     assertEquals(true, ret);

    //     handler.serve(context);
    // }

    // @Test
    // void isOcpp_성공케이스2() {

    //     //S202207041208013100012111O2101234567801E
    //     //                             1234567801
    //     Message message = Message.builder()
    //             .chargerId("2008692001")
    //             .payload("00011010010174790089")
    //             .payloadLength(20)
    //             .cmd("O1").build();

    //     MessageHandlerContext context = new MessageHandlerContext(message, "127.0.0.1");

    //     boolean ret = handler.isOcpp(context);

    //     assertEquals(false, ret);

    //     handler.serve(context);
    // }

    // @Test
    // void isOcpp_성공케이스3() {

    //     //S202205271559172009563011C14300000000000099980900005410632             EE
    //     //               2009563011C14300000000000099980900005410632             EE
    //     //                             1234567890123456789012345678901234567890123
    //     Message message = Message.builder()
    //             .chargerId("2009563011")
    //             .payload("00000000000099980900005410632             E")
    //             .payloadLength(43)
    //             .cmd("C1").build();

    //     MessageHandlerContext context = new MessageHandlerContext(message, "127.0.0.1");

    //     boolean ret = handler.isOcpp(context);

    //     assertEquals(true, ret);

    //     handler.serve(context);
    // }

    // @Test 
    // void findV1(){
    //     String test = "http://211.252.81.169:8082/v1/control/2003412011/stopCharge";
    //     //http://211.252.81.169:8082/v1/control/2003412011/stopCharge
    //     //012345678901234567890123456789012345678901234567890123456789
    //     int ret =  test.indexOf("v1");
    //     assertEquals(27, ret);

    //     ret += 2;
    //     String retStr = test.substring(0, ret);
    //     assertEquals("http://211.252.81.169:8082/v1", retStr);
    // }
}