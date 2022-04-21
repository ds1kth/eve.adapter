package com.chargev.eve.adapter.message.handler;

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
    private final int _firmware_idx_size = 20;
    private final int _strIPAddress_size = 150;
    private final int _strFirmwareVersion_size = 50;

    public Integer serve(MessageHandlerContext context) {
        log.debug("[S3] {}", context);

        int ml = context.getMessage().getPayloadLength();
        byte[] payload = context.getMessage().getPayload().getBytes();

        byte[] _firmware_idx = new byte[_firmware_idx_size];
        byte[] _strIPAddress = new byte[_strIPAddress_size];
        byte[] _strFirmwareVersion = new byte[_strFirmwareVersion_size];
        int pos;
        byte[] startStopTemp = new byte[1];

        if(ml == 21 || ml == 99) {
            if(ml == 99) {
                pos = 4;    // real ml + start/stop
                startStopTemp[0] = payload[3];
            }
            else {
                pos = 1;
                startStopTemp[0] = payload[0];
            }
            do {
                for(int i = 0; i<_firmware_idx_size; i++) {
                    _firmware_idx[i] = payload[pos++];
                }
                if(ml == 21)
                    break;

                for(int i = 0; i<_strIPAddress_size; i++) {
                    _strIPAddress[i] = payload[pos++];
                }
                for(int i = 0; i<_strFirmwareVersion_size; i++) {
                    _strFirmwareVersion[i] = payload[pos++];
                }
            }while(false);
        }
        else {
            // error
            return 0;
        }

        String startStop = new String(startStopTemp);
//        String value = new String(valueTemp, StandardCharsets.UTF_8);
//
//        String url = context.getServerUrl() + "/requestInstallInfo";
//        context.sendRequest(null, url, context.getMessage().getCmd());

        RespMessage respMessage = RespMessage.builder()
                .INS("3S")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 0;
    }
}
