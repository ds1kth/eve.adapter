package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_S3_Req;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class Message_S3_HandlerTest {

    Message_S3_Handler handler = new Message_S3_Handler();

    @Test
    void test1() {
        String input = "a/b/c/d/e";
        int ret = input.lastIndexOf("/");
        assertEquals(7, ret);
    }

//    @Test
//    void S3전문검증_성공케이스_ml사이즈_99(){
//       Message message = Message.builder()
//               .chargerId("3100012111")
//               .payload("205S101Ywww.naver.com/parkjunoh/test/firware.bin--------------------------------------------------------------------------------------------------------------fileVersion1234567890abcdefghijkmlnolprstuvwxyz!@#")
//               .payloadLength(208)
//               .cmd("S3").build();
//
//       MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
//       handler.serve(context);
//       Api_S3_Req ret = handler.getApiS3Req();
//       String ExpectedPath = "www.naver.com/parkjunoh/test";
//       String ExpectedName = "firware.bin--------------------------------------------------------------------------------------------------------------";
//       String slash = "/";
//
//       assertEquals("1", ret.getDeviceType());
//       assertEquals("0x01", ret.getContentType());
//       assertEquals("fileVersion1234567890abcdefghijkmlnolprstuvwxyz!@#", ret.getVersion());
//       assertEquals(ExpectedPath, ret.getFilePath());
//
//       assertEquals(150, ExpectedPath.length() + slash.length() + ExpectedName.length());
//       assertEquals(150, ret.getFilePath().length() + slash.length() + ret.getFilename().length());
//       assertEquals(ExpectedName, ret.getFilename());
//    }

//    @Test
//    void S3전문검증_성공케이스_ml사이즈_99_공백포함(){
//        Message message = Message.builder()
//                .chargerId("3100012111")
//                .payload("205S101Yhttp://211.251.236.84/firmware/E:\\CHARGER\\update\\HB7K/EVSESlow.exe                                                                                    I1610121                                          ")
//                .payloadLength(208)
//                .cmd("S3").build();
//
//        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
//        handler.serve(context);
//        Api_S3_Req ret = handler.getApiS3Req();
//        String ExpectedPath = "http://211.251.236.84/firmware/E:\\CHARGER\\update\\HB7K";
//        String ExpectedName = "EVSESlow.exe";
//        String slash = "/";
//
//        assertEquals("1", ret.getDeviceType());
//        assertEquals("0x01", ret.getContentType());
//        assertEquals("I1610121", ret.getVersion());
//        assertEquals(ExpectedPath, ret.getFilePath());
//
////        assertEquals(150, ExpectedPath.length() + slash.length() + ExpectedName.length());
////        assertEquals(150, ret.getFilePath().length() + slash.length() + ret.getFilename().length());
//        assertEquals(ExpectedName, ret.getFilename());
//    }

//    @Test
//    void S3전문검증_성공케이스_ml사이즈_99_공백포함_1(){
//        Message message = Message.builder()
//                .chargerId("3100012111")
//                .payload("205T101Yhttp://211.251.236.84/firmware/HB7K/EVSESlow.exe                                                                                                      I1610121                                          ")
//                .payloadLength(208)
//                .cmd("S3").build();
//
//        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
//        handler.serve(context);
//        Api_S3_Req ret = handler.getApiS3Req();
//        String ExpectedPath = "HB7K";
//        String ExpectedName = "EVSESlow.exe";
//        String slash = "/";
//
//        assertEquals("1", ret.getDeviceType());
//        assertEquals("0x01", ret.getContentType());
//        assertEquals("I1610121", ret.getVersion());
//        assertEquals(ExpectedPath, ret.getFilePath());
//
////        assertEquals(150, ExpectedPath.length() + slash.length() + ExpectedName.length());
////        assertEquals(150, ret.getFilePath().length() + slash.length() + ret.getFilename().length());
//        assertEquals(ExpectedName, ret.getFilename());
//    }

    @Test
    void S3전문검증_성공케이스_ml사이즈_99_공백제거_K2전문용(){
        Message message = Message.builder()
                .chargerId("3100012111")
                .payload("205T101Yhttp://211.251.236.84/firmware/HB7K/EVSESlow.exe                                                                                                      I1610121                                          ")
                .payloadLength(208)
                .cmd("S3").build();

        MessageHandlerContext context = new MessageHandlerContext(message, "192.168.0.1");
        handler.serve(context);
        Api_S3_Req ret = handler.getApiS3Req();
        String ExpectedPath = "http://211.251.236.84/firmware/HB7K/EVSESlow.exe";
        String ExpectedName = "EVSESlow.exe";
//        String slash = "/";

        assertEquals("CHARGER", ret.getDeviceType());
        assertEquals("FIRMWARE", ret.getContentType());
        assertEquals("I1610121", ret.getVersion());
        assertEquals(ExpectedPath, ret.getFilePath());

//        assertEquals(150, ExpectedPath.length() + slash.length() + ExpectedName.length());
//        assertEquals(150, ret.getFilePath().length() + slash.length() + ret.getFilename().length());
//        assertEquals(ExpectedName, ret.getFilename());
    }
}