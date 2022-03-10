package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@Service("Message_G1_Handler")
public class Message_G1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        log.debug("[G1] {}", context);

        String userType;
        String url = context.makeUrl("/downloadPriceInfo");
        context.sendRequest(null, url, context.getMessage().getCmd());

        return 0;
    }
}
