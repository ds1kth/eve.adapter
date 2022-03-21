package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_N1_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * API: 3.13 RequestInfoChange
 * 충전기 정보를 변경 요청합니다.
 *
 * Spec :
 * 차지비(N1)
 *
 * Test :
 * func_1_24
 */
@Slf4j
@Service("Message_N1_Handler")
public class Message_N1_Handler implements MessageHandler<MessageHandlerContext, Integer> {

    private final int ML_LENGTH = 2;
    public Integer serve(MessageHandlerContext context) {
        log.debug("[N1] {}", context);

        String url = context.makeUrl("/requestInfoChange");

        int valueLength = context.getMessage().getPayloadLength() - 2;
        byte[] payload = context.getMessage().getPayload().getBytes();
        byte[] keyTemp = new byte[2];
        int pos = 0;
        keyTemp[0] = payload[pos++];
        keyTemp[1] = payload[pos++];

        byte[] valueTemp = new byte[valueLength];

        for(int i = 0; i<valueLength; i++) {
            valueTemp[i] = payload[pos++];
        }

        String key = new String(keyTemp);
        String value = new String(valueTemp, StandardCharsets.UTF_8);

        int keyInt = Integer.parseInt(key);
        if(keyInt > 10)
            return 0;

        Api_N1_Req req = null;

        req = Api_N1_Req.builder()
                .infoType(key)
                .infoValue(value)
                .build();

        context.sendRequest(req, url, context.getMessage().getCmd());

        RespMessage respMessage = RespMessage.builder()
                .INS("1N")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }
}
