package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service("Message_S1_Handler")
public class Message_S1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        log.debug("[S1] {}", context);

        String url = context.getServerUrl() + "/requestChargerStatus";
        context.sendRequest(null, url, context.getMessage().getCmd());

        return 0;
    }
}
