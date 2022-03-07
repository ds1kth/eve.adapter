package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_B1_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * API: 3.11	RequestModeChange
 * 충전기 모드 변경 요청합니다.
 *
 * Spec :
 * 차지비(C1)
 * KT(C1)
 *
 * Test :
 * func_1_3
 */
@Slf4j
@Service("Message_B1_Handler")
public class Message_B1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        log.debug("[B1] {}", context);
        String url = context.makeUrl("/requestModeChange");
        byte[] payload = context.getMessage().getPayload().getBytes();
//        byte[] ML = new byte[2];
//        ML[0] = payload[0];
        byte modeByte = payload[2];
        String mode = null;

        String str = new String(payload);
        /*
            byte 0 :
            byte 1 : space
            byte 2 : R(충전기 운영), S(충전기 운영중지), C(충전기 점검중), T(충전기 TEST)
         */
        switch (modeByte){
            case 'R' :
                mode = "0001"; // 충전기 운영
                break;

            case 'S' :
                mode = "0002"; // 충전기 운영중지
                break;

            case 'C' :
                mode = "0016"; // 충전기 점검중
                break;

            case 'T' :
                mode = "0032"; // 충전기 TEST
                break;

            default :
                return 1;
        }

        Api_B1_Req req = null;

        req = Api_B1_Req.builder()
                .mode(mode)
                .modeType("0")    // 무조건 0
                .soundSet("0")      // 무조건 0
                .chargeKwh("0")   // 무조건 0
                .build();

        context.sendRequest(req, url);

        return 0;
    }
}

