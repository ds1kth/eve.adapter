package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_O1_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * API: 3.35	RemoteStartTransaction
 *
 * Spec :
 * 차지비(O1)
 *
 * Test :
 * func_RemoteStopTransaction
 */
@Slf4j
@Service("Message_O1_Handler")
public class Message_O1_Handler implements MessageHandler<MessageHandlerContext, Integer> {

        private final int CHARGING_PROFILE_LENGTH = 2;        
        private final int CONNECTOR_ID_LENGTH = 2;
        private final int ID_TAG_LENGTH = 16;
    
        private Api_O1_Req req = null;
        public Api_O1_Req getApiO1Req() {
            return req;
        }
    
        public Integer serve(MessageHandlerContext context) {
            log.debug("[O1] {}", context);
            
            byte[] payload = context.getMessage().getPayload().getBytes();
    
            byte[] chargingProfile = new byte[CHARGING_PROFILE_LENGTH];
            byte[] connectorId = new byte[CONNECTOR_ID_LENGTH];
            byte[] idTag = new byte[ID_TAG_LENGTH];
    
            int i=0;
            for(; i<CHARGING_PROFILE_LENGTH; i++){
                chargingProfile[i] = payload[i];
            }
    
            for(int k = 0; k<CONNECTOR_ID_LENGTH; i++, k++){
                connectorId[k] = payload[i];
            }
    
            for(int k = 0; k<ID_TAG_LENGTH; i++, k++){
                idTag[k] = payload[i];
            }

            String chargingProfileStr = new String(chargingProfile);
            String connectorIdStr = new String(connectorId);
            String idTagStr = new String(idTag);
            
            
            try {
                Integer chargingProfileInt = Integer.parseInt(chargingProfileStr);
                Integer connectorIdInt = Integer.parseInt(connectorIdStr);
    
    //            Api_O1_Req req = null;
                req = Api_O1_Req.builder()
//                        .chargingProfile(getChargingProfile(chargingProfileInt))
                        .connectorId(connectorIdInt)
                        .idTag(idTagStr)
                        .build();
    
                String url = context.makeUrl("/start");
                context.sendRequest(req, url, context.getMessage().getCmd());
            } catch (NumberFormatException e) {
                log.error("[Exception] {}", e.getStackTrace()[0]);
                return 0;
            }
    
            RespMessage respMessage = RespMessage.builder()
                    .INS("1O")
                    .ML("5")
                    .VD("S    ")
                    .build();
            context.setRespMessage(respMessage);
            return 1;
    }

    private String getChargingProfile(int profileInt) {
        String profileStr;

        switch(profileInt) {
            case 0 :
                profileStr = "chargingProfileId";
                break;
            case 1 :
                profileStr = "transactionId";
                break;
            case 2 :
                profileStr = "stackLevel";
                break;
            case 3 :
                profileStr = "chargingProfilePurpose";
                break;
            case 4 :
                profileStr = "chargingProfileKind";
                break;
            case 5 :
                profileStr = "recurrencyKind";
                break;
            case 6 :
                profileStr = "validFrom";
                break;
            case 7 :
                profileStr = "validTo";
                break;                
            default :
                profileStr = "chargingSchedule";
                break;                                                                                                
        }

        return profileStr;
    }
}
