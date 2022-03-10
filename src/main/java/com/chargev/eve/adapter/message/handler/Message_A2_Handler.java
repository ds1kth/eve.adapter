package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 
 * 
 * 
 * SMS 전송
 */
@Slf4j
@Service("Message_A2_Handler")
public class Message_A2_Handler implements MessageHandler<MessageHandlerContext, Integer> {

    public Integer serve(MessageHandlerContext context) {
        log.debug("[A2] {}", context);
        // nothing to do
//        String url = context.getServerUrl() + "/requestChargerStatus";
//        context.sendRequest(null, url);

        return 0;
    }
}
