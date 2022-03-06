package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
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
@Service("Message_K1_Handler")
public class Message_K1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        String url = context.getServerUrl() + "/UpdateFirmware";
        context.sendRequest(null, url);

        return 1;
    }
}
