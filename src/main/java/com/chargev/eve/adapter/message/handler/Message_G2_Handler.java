package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * API: 3.8 DownloadPriceInfo
 * 단가 정보를 다운로드 합니다
 *
 * Spec :
 * KT(K2) Only
 *
 * Test :
 * func_
 */
@Slf4j
@Service("Message_G2_Handler")
public class Message_G2_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        log.debug("[G1] {}", context);

        String userType = "11"; // 비회원 단가, 무조건 // 맞나?
        String url = context.makeUrl("/downloadPriceInfo", "/", userType);
        context.sendRequest(null, url, context.getMessage().getCmd());

        RespMessage respMessage = RespMessage.builder()
                .INS("2G")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }
}
