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
        req = Api_K1_Req.builder()
                .bytChargerId(context.getMessage().getChargerId())
                .bytUserUID(getCardnubmer(payload))
                .bytStartDate(getStartDate(payload))
                .bytEndDate(getEndDate(payload))
                .bytDBUniq(getDBUniq(payload))
                .bytResCan(getResCan(payload))
                .build();

        String url = context.getServerUrl() + "/UpdateFirmware";
        context.sendRequest(req, url, context.getMessage().getCmd());

        context.setRespMessage(null);
        RespMessage respMessage = RespMessage.builder()
                .INS("1K")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }

    private String getCardnubmer(byte[] payload) throws IllegalArgumentException {
        byte[] cardnubmer = new byte[16];
        int start = 0;

        for (int i = 0; i < 16; i++, start++) {
            cardnubmer[i] = payload[start];
        }

        String str = new String(cardnubmer);

        return str;
    }

    private String getStartDate(byte[] payload) throws IllegalArgumentException {
        int start = CARD_NUMBER_LENGTH;
        byte[] startDate = new byte[START_DATE_LENGTH];

        for (int i = 0; i < START_DATE_LENGTH; i++, start++) {
            startDate[i] = payload[start];
        }

        String str = new String(startDate);

        return str;
    }

    private String getEndDate(byte[] payload) throws IllegalArgumentException {
        int start = CARD_NUMBER_LENGTH + START_DATE_LENGTH;
        byte[] endDate = new byte[END_DATE_LENGTH];

        for (int i = 0; i < END_DATE_LENGTH; i++, start++) {
            endDate[i] = payload[start];
        }

        String str = new String(endDate);

        return str;
    }

    private String getDBUniq(byte[] payload) throws IllegalArgumentException {
        int start = CARD_NUMBER_LENGTH + START_DATE_LENGTH + END_DATE_LENGTH;
        byte[] DBUniq = new byte[DB_UNIQ_LENGTH];

        for (int i = 0; i < DB_UNIQ_LENGTH; i++, start++) {
            DBUniq[i] = payload[start];
        }

        String str = new String(DBUniq);

        return str;
    }

    private String getResCan(byte[] payload) throws IllegalArgumentException {
        int start = CARD_NUMBER_LENGTH + START_DATE_LENGTH + END_DATE_LENGTH + DB_UNIQ_LENGTH;
        byte[] resCan = new byte[CANCEL_LENGTH];

        for (int i = 0; i < CANCEL_LENGTH; i++, start++) {
            resCan[i] = payload[start];
        }

        String str = new String(resCan);

        return str;
    }
}
