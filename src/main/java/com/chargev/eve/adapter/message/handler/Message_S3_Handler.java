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
        contentsType = "0x" + contentsType;
        String firmwareVersion = new String(_strFirmwareVersion).trim();

        Api_S3_Req req = null;
        String url = context.makeUrl("/transmit");
        String pathName = new String(_strIPAddress);
        int nameStartPos = pathName.lastIndexOf("/");
        String path = pathName.substring(0, nameStartPos).trim();
        String name = pathName.substring(nameStartPos + 1).trim();

        // path 에서 url 정보는 없애버린다. 
        nameStartPos = path.lastIndexOf("/");
        path = path.substring(nameStartPos + 1).trim();
        
        req = Api_S3_Req.builder()
                .chargerId(context.getMessage().getChargerId())
                .deviceType(teminalType)
                .contentType(contentsType)
                .Version(firmwareVersion)
                .filename(name)
                .filePath(path)
                .build();
        apiS3Req = req;
        context.sendRequest(req, url, context.getMessage().getCmd(), "K1");

        RespMessage respMessage = RespMessage.builder()
                .INS("3S")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 0;
    }
}
