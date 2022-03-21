package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 충전기의 설치 정보 조회합니다
 */
@Slf4j
@Service("Message_S2_Handler")
public class Message_S2_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        log.debug("[S2] {}", context);

        String url = context.getServerUrl() + "/requestInstallInfo";
        context.sendRequest(null, url, context.getMessage().getCmd());

        RespMessage respMessage = RespMessage.builder()
                .INS("2S")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 0;
    }
}
