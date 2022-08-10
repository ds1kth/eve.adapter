package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_O2_Req;
import com.chargev.eve.adapter.apiClient.api.Api_O3_Req;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class Message_O3_HandlerTest {
    Message_O3_Handler handler = new Message_O3_Handler();

    @Test
    void updateFirmware_전문검증_성공케이스() {
        Message message = Message.builder()
                .chargerId("3131342003")
//                .payload("128http://211.251.236.84/firmware/SPEEL/B220722A.zip                                                   00012020-10-28T15:18:00Z0002")
//                .chargerId("3131342004")
                .payload("128http://211.251.236.84/firmware/CEC-2303HR-OCPP/ITEPKG03.PKG                                         00012020-10-28T15:18:00Z0000")
                .payloadLength(131)
                .cmd("O3").build();

//         MessageHandlerContext context = new MessageHandlerContext(message, "http://52.79.78.191:8082/v1/control/3131342004");
        MessageHandlerContext context = new MessageHandlerContext(message, "http://211.252.81.169:8082/v1/control/3131342003");
         //        MessageHandlerContext context = new MessageHandlerContext(message, "127.0.0.1/v1/control/3131342004");
        handler.serve(context);

        Api_O3_Req ret = handler.getApiO3Req();
//        assertEquals("http://211.251.236.84/firmware/SPEEL/B220722A.zip", ret.getLocation());
        assertEquals("http://211.251.236.84/CEC-2303HR-OCPP/ITEPKG03.PKG", ret.getLocation());
        assertEquals(1, ret.getRetries());
        assertEquals("2020-10-28T15:18:00Z", ret.getRetrieveDate());
        assertEquals(2, ret.getRetryInterval());

        handler.serve(context);
    }

//    @Test
//    public void urlCallBizHttp()  {
////        log.info("=============================================================================");
////        log.info("URL ==> " + strUrl);
////        log.info("Data ==> " + strReq);
////        log.info("=============================================================================");
//
//        HttpURLConnection conn = null;
//
//        String strUrl = "http://52.79.78.191:8082/v1/control/2009563011/startCharge";
//        String strReq = "{\n" +
//                "    \"extCsId\": \"200869\",\n" +
//                "    \"extCpId\": \"2001\"\n" +
//                "}";
//
//        try {
//            URL url = new URL(strUrl);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(30000);
//            conn.setReadTimeout(30000);
//            conn.setRequestMethod("POST");
//
//            conn.setRequestProperty("Content-Type", "application/json");
//            conn.setRequestProperty("ACCEPT", "*/*");
//            conn.setRequestProperty("Authorization", "aW50ZXJuYWw6ZXZlaW50ZXJuYWw=");
//
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.connect();
//
//            String strData = strReq;
//
//            // 송신 부분
//            OutputStream stream = conn.getOutputStream();
//            stream.write(strData.getBytes(StandardCharsets.UTF_8));
//            stream.close();
//
//            StringBuilder sb = null;
//            BufferedReader br = null;
//
//            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                sb = new StringBuilder();
//                br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
//
//                String rcvData = "";
//                while((rcvData = br.readLine()) != null) {
//                    sb.append(rcvData);
//                }
//            } else {
////                log.error(String.valueOf(conn.getResponseCode()));
////                log.error(String.valueOf(conn.getResponseMessage()));
//
//            }
//
//            conn.disconnect();
//
//
//
//        }
//        catch (Exception e) {
////            log.error("error: ", e);
//
//        }
//    }
}