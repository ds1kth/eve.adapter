package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
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
@Service("Message_U1_Handler")
public class Message_U1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        String url = context.makeUrl("/requestInstallInfo");

        context.sendRequest(null, url);

        return 1;
    }
}