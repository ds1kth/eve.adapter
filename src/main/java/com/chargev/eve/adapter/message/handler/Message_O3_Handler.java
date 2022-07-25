package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_O2_Req;
import com.chargev.eve.adapter.apiClient.api.Api_O3_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;

@Slf4j
@Service("Message_O3_Handler")
public class Message_O3_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    private final int LOCATION_LENGTH = 100;
    private final int RETRIES_LENGTH = 4;
    private final int RETRIEVE_DATE_LENGTH = 20;
    private final int RETRY_INTERVAL_LENGTH = 4;

    private Api_O3_Req req = null;
    public Api_O3_Req getApiO3Req() {
        return req;
    }

    public Integer serve(MessageHandlerContext context) {
        log.debug("[O3] {}", context);

        byte[] payload = context.getMessage().getPayload().getBytes();

        byte[] location = new byte[LOCATION_LENGTH];
        byte[] retries = new byte[RETRIES_LENGTH];
        byte[] retrieveDate = new byte[RETRIEVE_DATE_LENGTH];
        byte[] retryInterval = new byte[RETRY_INTERVAL_LENGTH];

        int i=0;
        for(; i<LOCATION_LENGTH; i++){
            location[i] = payload[i];
        }

        for(int k = 0; k<RETRIES_LENGTH; i++, k++){
            retries[k] = payload[i];
        }

        for(int k = 0; k<RETRIEVE_DATE_LENGTH; i++, k++){
            retrieveDate[k] = payload[i];
        }

        for(int k = 0; k<RETRY_INTERVAL_LENGTH; i++, k++){
            retryInterval[k] = payload[i];
        }

        String locationStr = new String(location);
        String retriesStr = new String(retries);
        String retrieveDateStr = new String(retrieveDate);
        String retryIntervalStr = new String(retryInterval);

        try {
            Integer retriesInt = Integer.parseInt(retriesStr);
            Integer retryIntervalInt = Integer.parseInt(retryIntervalStr);

//            URI uri = new URI(locationStr);

            req = Api_O3_Req.builder()
//                    .location(uri)
                    .location(locationStr.trim())
                    .retries(retriesInt)
                    .retrieveDate(retrieveDateStr.trim())
                    .retryInterval(retryIntervalInt)
                    .build();

            String url = context.makeUrl("/UpdateFirmware");
            context.sendRequest(req, url, context.getMessage().getCmd());
        } catch (Exception e) {
            log.error("[Exception] {}", e.getStackTrace()[0]);
            return 0;
        }

        RespMessage respMessage = RespMessage.builder()
                .INS("3O")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }
}
