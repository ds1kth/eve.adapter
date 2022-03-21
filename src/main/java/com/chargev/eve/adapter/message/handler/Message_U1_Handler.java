package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * API:3.28 RequestInstallInfo
 *  충전기의 설치 정보 조회합니다.
 *
 * Spec :
 * 차지비(U1)
 * KT(U1)
 *
 * Test :
 * func_1_19
 */
@Slf4j
@Service("Message_U1_Handler")
public class Message_U1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        log.debug("[U1] {}", context);

        String url = context.makeUrl("/requestInstallInfo");

        context.sendRequest(null, url, context.getMessage().getCmd());

        RespMessage respMessage = RespMessage.builder()
                .INS("1U")
                .ML("5")
                .VD("E0006")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }
}
