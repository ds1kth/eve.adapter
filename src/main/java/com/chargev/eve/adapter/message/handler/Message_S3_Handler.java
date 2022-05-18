package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_C1_Common_Start_Req;
import com.chargev.eve.adapter.apiClient.api.Api_S3_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * file:///C:/01_chargev/02_home_page/day/2022/month_04/12/day12_3.md
 */
@Slf4j
@Service("Message_S3_Handler")
public class Message_S3_Handler implements MessageHandler<MessageHandlerContext, Integer> {    
    private final int _strIPAddress_size = 150;
    private final int _strFirmwareVersion_size = 50;

    private Api_S3_Req apiS3Req;

    public Api_S3_Req getApiS3Req() {
        return apiS3Req;
    }

    public Integer serve(MessageHandlerContext context) {
        log.debug("[S3] {}", context);

        // int ml = context.getMessage().getPayloadLength();
        byte[] payload = context.getMessage().getPayload().getBytes();

        byte[] startStopTemp = new byte[1];
        byte[] teminalTypeTemp = new byte[1];
        byte[] contentsTypeTemp = new byte[2];
        byte[] _bytMakerChargerTemp = new byte[1];
        byte[] _strIPAddress = new byte[_strIPAddress_size];
        byte[] _strFirmwareVersion = new byte[_strFirmwareVersion_size];
        
        int pos = 3;

        startStopTemp[0] = payload[pos++];
        teminalTypeTemp[0] = payload[pos++];        
        contentsTypeTemp[0] = payload[pos++];
        contentsTypeTemp[1] = payload[pos++];
        _bytMakerChargerTemp[0] = payload[pos++];

        for(int i = 0; i<_strIPAddress_size; i++) {
            _strIPAddress[i] = payload[pos++];
        }
        for(int i = 0; i<_strFirmwareVersion_size; i++) {
            _strFirmwareVersion[i] = payload[pos++];
        }

        String teminalType = new String(teminalTypeTemp);
        String startStop = new String(startStopTemp);
        String contentsType = new String(contentsTypeTemp);
//        contentsType = "0x" + contentsType;

        String teminalTypeStr = " ";
        switch(teminalType) {
            case "1":
                teminalTypeStr = "CHARGER";
                break;
            case "2":
                teminalTypeStr = "M2M";
                break;
            case "3":
                teminalTypeStr = "RF_READER";
                break;
            case "4":
                teminalTypeStr = "IC_CARD_READER";
                break;
            default :
                log.debug("[S3] {}", "wrong teminalType parameter");
                return 0;
        }

        String contentsTypeStr = " ";
        switch(contentsType) {
            case "01" :
                contentsTypeStr = "FIRMWARE";
                break;
            case "02" :
                contentsTypeStr = "IMAGE";
                break;
            case "03" :
                contentsTypeStr = "SOUND";
                break;
            case "04" :
                contentsTypeStr = "TEXT";
                break;
            case "05" :
                contentsTypeStr = "MEMBER_ALL";
                break;
            case "06" :
                contentsTypeStr = "MEMBER_APPEND";
                break;
            case "07" :
                contentsTypeStr = "MEMBER_REMOVE";
                break;
            case "08" :
                contentsTypeStr = "RESERVATION";
                break;
            default :
                log.debug("[S3] {}", "wrong contentsType parameter");
                return 0;
        }

        String firmwareVersion = new String(_strFirmwareVersion).trim();

        Api_S3_Req req = null;
        String url = context.makeUrl("/sendFile");
        String pathName = new String(_strIPAddress).trim();
        int nameStartPos = pathName.lastIndexOf("/");
        String path = pathName.substring(0, nameStartPos).trim();
        String name = pathName.substring(nameStartPos + 1).trim();

        // path 에서 url 정보는 없애버린다. 
//        nameStartPos = path.lastIndexOf("/");
//        path = path.substring(nameStartPos + 1).trim();
        
        req = Api_S3_Req.builder()
                .chargerId(context.getMessage().getChargerId())
                .deviceType(teminalTypeStr)
                .contentType(contentsTypeStr)
                .Version(firmwareVersion)
                .fileName("01")
                .filePath(pathName)
                .build();
        apiS3Req = req;
        context.sendRequest(req, url, context.getMessage().getCmd(), "K2");

        RespMessage respMessage = RespMessage.builder()
                .INS("3S")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 0;
    }
}
