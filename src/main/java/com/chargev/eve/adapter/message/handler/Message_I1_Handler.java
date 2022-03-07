package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * API: 3.1 RESET
 * 원격으로 충전 트랙잭션을 시작
 *
 * Spec :
 * 차지비(I1)
 * KT(I1)
 *
 * Test :
 * func_1_16
 */
@Slf4j
@Service("Message_I1_Handler")
public class Message_I1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    private final int RESET_TYPE_LENGTH = 2;
    public Integer serve(MessageHandlerContext context) {
        log.debug("[I1] {}", context);

        byte[] payload = context.getMessage().getPayload().getBytes();
        byte[] vdLength = new byte[RESET_TYPE_LENGTH];
        vdLength[0] = payload[0];
        vdLength[1] = payload[1];

        String str = new String(vdLength);
        str = str.trim();
        int intVD = Integer.parseInt(str);
        String resetType = null;

        if (intVD > 0)
        {
            byte[] temp = new byte[1];
            temp[0] = payload[RESET_TYPE_LENGTH]; // 리셋 대상자

            resetType = new String(temp);
        }
        else
        {
            // public static byte DEF_ASC_FOR = 0x34; // 숫자 4
            resetType = "4";    // H/W Reset (전체 Reset)
        }

        switch (resetType) {
            case "1" :  // 충전기
            case "2" :  // M2M통신단말기
            case "3" :  // RF카드단말기
            case "4" :  // H/W Reset (전체 Reset)
                break;
            default :
                return 0;
        }

        String url = context.makeUrl("/reset", "/0", resetType);
        context.sendRequest(null, url);

        return 0;
    }
}

