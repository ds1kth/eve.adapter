package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.message.MessageHandlerContext;
import org.springframework.stereotype.Service;

/**
 * API: 3.8 DownloadPriceInfo
 * 단가 정보를 다운로드 합니다
 *
 * Spec :
 * KT(K2) Only
 *
 * Test :
 * func_
 */
@Service("Message_G2_Handler")
public class Message_G2_Handler {
    public Integer serve(MessageHandlerContext context) {
        String userType = "11"; // 비회원 단가, 무조건
        String url = context.makeUrl("/downloadPriceInfo", "/", userType);
        context.sendRequest(null, url);

        return 0;
    }
}
