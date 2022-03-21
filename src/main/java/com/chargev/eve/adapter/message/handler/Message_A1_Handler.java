package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_B1_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

/**
 * API: 3.11	RequestModeChange
 * 충전기 모드 변경 요청합니다.
 *
 * Spec :
 * 차지비(C1)
 * KT(C1)
 *
 * Test :
 * func_1_2
 */
@Slf4j
@Service("Message_A1_Handler")
public class Message_A1_Handler implements MessageHandler<MessageHandlerContext, Integer> {

    public Integer serve(MessageHandlerContext context) {
        log.debug("[A1] {}", context);
        String url = context.makeUrl("/requestModeChange");

        byte[] payload = context.getMessage().getPayload().getBytes();
        int ml = context.getMessage().getPayloadLength();
        byte[] ampereByte = new byte[ml];
        for(int i=0; i<ml; i++){
            ampereByte[i] = payload[i];
        }

        String ampere = new String(ampereByte);

        try {
            Integer ampereInt = Integer.parseInt(ampere);
        } catch (NumberFormatException e) {
            log.error("[Exception] {}", e.getStackTrace()[0]);
            return 0;
        }

        String mode = "0001"; // 충전기 운영
        Api_B1_Req req = null;

        req = Api_B1_Req.builder()
                .mode(mode)
                .modeType("02")         // 충전기 충전량 변경
                .soundSet("FFFFFFFF")   // 쓰레기값
                .chargeKwh(ampere)
                .build();

        context.sendRequest(req, url, context.getMessage().getCmd());

        RespMessage respMessage = RespMessage.builder()
                .INS("1A")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }
}
