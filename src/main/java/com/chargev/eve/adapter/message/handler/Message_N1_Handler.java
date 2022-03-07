package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_N1_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.springframework.stereotype.Service;

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
@Service("Message_N1_Handler")
public class Message_N1_Handler implements MessageHandler<MessageHandlerContext, Integer> {

    private final int VD_LENGTH = 2;
    private final int ML_LENGTH = 2;
    public Integer serve(MessageHandlerContext context) {
        String url = context.makeUrl("/requestInfoChange");

        byte[] payload = context.getMessage().getPayload().getBytes();
        byte[] keyTemp = new byte[2];
        int pos = VD_LENGTH;
        keyTemp[0] = payload[pos++];
        keyTemp[1] = payload[pos++];

        byte[] valueTemp = new byte[30];

        for(int i = 0; i<30; i++) {
            valueTemp[i] = payload[pos++];
        }

        String key = new String(keyTemp);
        String value = new String(valueTemp);

        int keyInt = Integer.parseInt(key);
        if(keyInt > 10)
            return 0;

        Api_N1_Req req = null;

        req = Api_N1_Req.builder()
                .infoType(key)
                .infoValue(value)
                .build();

        context.sendRequest(req, url);

        return 0;
    }
}
