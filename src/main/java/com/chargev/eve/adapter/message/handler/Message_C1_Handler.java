
package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.springframework.stereotype.Service;

@Service("Message_C1_Handler")
public class Message_C1_Handler implements MessageHandler<MessageHandlerContext, Integer> {

    public Integer serve(MessageHandlerContext context) {
        String url = context.getServerDomain() + "/v1/control/" + context.getMessage().getChargerId() + "/requestMode";
        context.sendRequest(null, url);
        return 1;
    }
}