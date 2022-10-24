package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * API: 3.14 RequestPaid
 * 충전기 유료/무료 전환을 제어합니다.
 *
 * Spec :
 * KT(H2)
 *
 * Test :
 * func_
 */
@Slf4j
@Service("Message_H2_Handler")
public class Message_H2_Handler implements MessageHandler<MessageHandlerContext, Integer> {

    public Integer serve(MessageHandlerContext context) {
        log.debug("[H2] {}", context);

        byte[] payload = context.getMessage().getPayload().getBytes();
        byte[] temp = new byte[1];
        temp[0] = payload[0];
        String freeOrNotStr = new String(temp);
        String freeOrNot = null;
        if(freeOrNotStr.equals("Y")) {  
            freeOrNot = "01";   // 유료
        }
        else {
            freeOrNot = "02";   // 무료
        }

        String url = context.makeUrl("/requestPaid/" + freeOrNot);

        context.sendRequest(null, url, context.getMessage().getCmd());

        RespMessage respMessage = RespMessage.builder()
                .INS("2H")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }
}
