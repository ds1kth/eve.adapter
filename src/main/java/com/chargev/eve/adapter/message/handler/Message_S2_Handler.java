package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.springframework.stereotype.Service;

/**
 * 충전기의 설치 정보 조회합니다
 */
@Service("Message_S2_Handler")
public class Message_S2_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        String url = context.getServerUrl() + "/requestInstallInfo";
        context.sendRequest(null, url);

        return 1;
    }
}
