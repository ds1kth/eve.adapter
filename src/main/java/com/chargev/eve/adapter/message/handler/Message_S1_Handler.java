package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.springframework.stereotype.Service;

@Service("Message_S1_Handler")
public class Message_S1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        String url = context.getServerUrl() + "/requestChargerStatus";
        context.sendRequest(null, url);

        return 0;
    }
}
