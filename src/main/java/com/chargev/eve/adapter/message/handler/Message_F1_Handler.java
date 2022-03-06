package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_C1_Req;
import com.chargev.eve.adapter.apiClient.api.Api_F1_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * API: 3.18	UpdateFirmware
 * 원격으로 펌웨어를 업데이트 요청합니다.
 *
 * Spec :
 * 차지비(K1)
 * KT(K1)
 *
 * Test :
 * func_1_1
 */
@Service("Message_F1_Handler")
public class Message_F1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {

        String url = context.makeUrl("/UpdateFirmware");
        //context.sendRequest(null, url);

        URI firmwareUrl = null;
        try {
            firmwareUrl = new URI("www.naver.com");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Api_F1_Req req = Api_F1_Req.builder()
                .location(firmwareUrl)
                .build();
        context.sendRequest(req, url);
        return 1;
    }
}
