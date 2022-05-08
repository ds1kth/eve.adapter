package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_F1_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * API: 3.18	UpdateFirmware
 * 원격으로 펌웨어를 업데이트 요청합니다.
 *
 * Spec :
 * 차지비(K1)
 * KT(K1)
 *
 * Test :
 * func_1_1
 */
@Slf4j
@Service("Message_F1_Handler")
public class Message_F1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    private final int _firmware_idx_size = 20;

    public Integer serve(MessageHandlerContext context) {
        log.debug("[F1] {}", context);
        String url = context.makeUrl("/UpdateFirmware");

        byte[] payload = context.getMessage().getPayload().getBytes();
        byte[] _firmware_idx = new byte[_firmware_idx_size];
        int pos = 0;
        for(int i = 0; i<_firmware_idx_size; i++) {
            _firmware_idx[i] = payload[pos++];
        }
        String uriStr = new String(_firmware_idx);

        URI firmwareUrl = null;

        try {
            firmwareUrl = new URI(uriStr);
        } catch (URISyntaxException e) {
            //e.printStackTrace();
            log.error("[Exception] {}", e.getStackTrace()[0]);
        }
        Api_F1_Req req = Api_F1_Req.builder()
                .location(firmwareUrl)
                .build();

        context.sendRequest(req, url, context.getMessage().getCmd());

        // NAK 전송?
        RespMessage respMessage = RespMessage.builder()
                .INS("1F")
                .ML("5")
                .VD("E0006")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }
}
