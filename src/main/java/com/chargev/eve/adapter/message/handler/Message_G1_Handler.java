package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.springframework.stereotype.Service;

/**
 * API: 3.8 DownloadPriceInfo
 * 단가 정보를 다운로드 합니다
 *
 * Spec :
 * 차지비(K1) Only
 *
 * Test :
 * func_
 */
@Service("Message_G1_Handler")
public class Message_G1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        String userType;
        String url = context.makeUrl("/downloadPriceInfo");
        context.sendRequest(null, url);

        return 1;
    }
}
