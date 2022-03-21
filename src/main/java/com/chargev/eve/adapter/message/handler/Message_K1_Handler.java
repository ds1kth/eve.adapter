package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * API:
 *
 *
 * Spec :
 * 차지비(K1)
 * KT(K1)
 *
 * Test :
 * func_
 */
@Slf4j
@Service("Message_K1_Handler")
public class Message_K1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        log.debug("[K1] {}", context);

        String url = context.getServerUrl() + "/UpdateFirmware";
        context.sendRequest(null, url, context.getMessage().getCmd());

        context.setRespMessage(null);
        RespMessage respMessage = RespMessage.builder()
                .INS("1K")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }
}
