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
 * func_1_2
 */
@Slf4j
@Service("Message_V1_Handler")
public class Message_V1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    public Integer serve(MessageHandlerContext context) {
        log.info("[V1] {}", context);
        
        String url = context.makeUrl("/requestModeChange");

        byte[] payload = context.getMessage().getPayload().getBytes();
        byte[] soundSetByte = new byte[2];

        soundSetByte[0] = payload[0];
        soundSetByte[1] = payload[1];
        String str = new String(soundSetByte);
        System.out.println(str);
        int temp = Integer.parseInt(str);
        String soundSet = null;

        if(temp > 0xFF) {
            return 0;
        }
        else if(temp < 0x10) {
            soundSet = "FFFF01" + "0" + Integer.toHexString(temp).toUpperCase();
        }
        else {
            soundSet = "FFFF01" + Integer.toHexString(temp).toUpperCase();
        }

        String mode = "0001"; // 충전기 운영

        Api_B1_Req req = null;

        req = Api_B1_Req.builder()
                .mode(mode)
                .modeType("01")     // 충전기 볼륨 변경
                .soundSet(soundSet)
                .chargeKwh("0")     // 무조건 0
                .build();

        context.sendRequest(req, url);
        return 0;
    }
}

