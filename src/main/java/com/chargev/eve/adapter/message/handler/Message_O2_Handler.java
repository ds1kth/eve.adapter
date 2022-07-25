package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_K1_Req;
import com.chargev.eve.adapter.apiClient.api.Api_O2_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 3.41	RemoteStopTransaction
 *
 * Spec :
 *
 *
 * Test :
 * func_RemoteStopTransaction
 */
@Slf4j
@Service("Message_O2_Handler")
public class Message_O2_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    private final int TRANSACTION_ID_LENGTH = 8;
    private final int CONNECTOR_ID_LENGTH = 2;

    private Api_O2_Req req = null;
    public Api_O2_Req getApiO2Req() {
        return req;
    }

    public Integer serve(MessageHandlerContext context) {
        log.debug("[O2] {}", context);
        
        byte[] payload = context.getMessage().getPayload().getBytes();

        byte[] transactionId = new byte[TRANSACTION_ID_LENGTH];
        byte[] connectorId = new byte[CONNECTOR_ID_LENGTH];

        int i=0;
        for(; i<TRANSACTION_ID_LENGTH; i++){
            transactionId[i] = payload[i];
        }

        for(int k = 0; k<CONNECTOR_ID_LENGTH; i++, k++){
            connectorId[k] = payload[i];
        }

        String transactionStr = new String(transactionId);
        String connectorIdStr = new String(connectorId);
        
        try {
            Integer transactionInt = Integer.parseInt(transactionStr);
            Integer connectorIdInt = Integer.parseInt(connectorIdStr);

//            Api_O2_Req req = null;
            req = Api_O2_Req.builder()
                    .transactionId(transactionInt)
                    .connectorId(connectorIdInt)
                    .build();

            String url = context.makeUrl("/" + transactionInt + "/stop");
            context.sendRequest(req, url, context.getMessage().getCmd());
        } catch (NumberFormatException e) {
            log.error("[Exception] {}", e.getStackTrace()[0]);
            return 0;
        }

        RespMessage respMessage = RespMessage.builder()
                .INS("2O")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }
}