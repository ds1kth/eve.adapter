package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.springframework.stereotype.Service;

@Service("Message_I1_Handler")
public class Message_I1_Handler implements MessageHandler<MessageHandlerContext, Integer> {

    public Integer serve(MessageHandlerContext context) {
        String resetType = context.getMessage().getPayload();
        resetType = resetType.trim();

        switch (resetType) {
            case "1" :  // 충전기
            case "2" :  // M2M통신단말기
            case "3" :  // RF카드단말기
            case "4" :  // H/W Reset (전체 Reset)
                break;
            default :
                return 0;
        }
        String url = context.getServerDomain() + "/v1/control/" + context.getMessage().getChargerId() + "/reset/" + '0' + resetType;
        context.sendRequest(null, url);

        return 1;
    }
}
