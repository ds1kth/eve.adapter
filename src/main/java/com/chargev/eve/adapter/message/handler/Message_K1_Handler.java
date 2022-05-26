package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_C1_Common_Stop_Req;
import com.chargev.eve.adapter.apiClient.api.Api_K1_Req;
import com.chargev.eve.adapter.apiClient.api.Api_S3_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * API:
 *
 *
 * Spec :
 * 차지비(K1)
 * KT(K1)
 *
 * Test :
 * func_
 */
@Slf4j
@Service("Message_K1_Handler")
public class Message_K1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    private final int CARD_NUMBER_LENGTH = 16;
    private final int START_DATE_LENGTH = 14;
    private final int END_DATE_LENGTH = 14;
    private final int DB_UNIQ_LENGTH = 20;
    private final int CANCEL_LENGTH = 1;

    private Api_K1_Req req = null;
    public Api_K1_Req getApiK1Req() {
        return req;
    }

    public Integer serve(MessageHandlerContext context) {
        log.debug("[K1] {}", context);
        byte[] payload = context.getMessage().getPayload().getBytes();

        // 예약 하기
        String teminalTypeStr = "CHARGER";
        String contentsTypeStr = "RESERVATION";
        Api_K1_Req req = null;
        String url = context.makeUrl("/transmit");
        
        req = Api_K1_Req.builder()
                .chargerId(context.getMessage().getChargerId())
                .deviceType(teminalTypeStr)
                .contentType(contentsTypeStr)
                .Version("00000000")
                .fileName("1")
                .filePath("1")
                .build();
                
        Api_K1_Req Api_K1_Req = req;
        this.req = req;
        context.sendRequest(req, url, context.getMessage().getCmd(), "K1");

        RespMessage respMessage = RespMessage.builder()
                .INS("1K")
                .ML("5 ")
                .VD("S0000")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }
}
