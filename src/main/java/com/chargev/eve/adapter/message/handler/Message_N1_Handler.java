package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_N1_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.springframework.stereotype.Service;

@Service("Message_N1_Handler")
public class Message_N1_Handler implements MessageHandler<MessageHandlerContext, Integer> {

    public Integer serve(MessageHandlerContext context) {
        String url = context.getServerDomain() + "/v1/control/{" + context.getMessage().getChargerId() + "}/requestInfoChange";

        Api_N1_Req req = null;

        req = Api_N1_Req.builder()
                .infoType("aaa")
                .infoValue("bbb")
                .build();

        context.sendRequest(req, url);

        return 1;
    }
}
