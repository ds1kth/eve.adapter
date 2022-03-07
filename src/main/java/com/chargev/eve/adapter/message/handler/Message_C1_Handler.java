
package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_C1_Req;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * API: 3.24 RequestChargingStart
 * 충전기가 충전을 시작하도록 제어 명령을 전달 한다.
 *
 * Spec :
 * 차지비(Q1)
 * KT(B2)
 *
 * Test :
 * func_1_8
 */
@Slf4j
@Service("Message_C1_Handler")
public class Message_C1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    private final int VD_LENGTH = 2;
    private final int CARD_NUMBER_LENGTH = 16;
    private final int CHARGING_TIME_LENGTH = 6;
    private final int DB_CODE_LENGTH = 20;
    private final int PORT_LENGTH = 1;
    private final int CHARGING_AMOUNT_LENGTH = 4;
    private final int PAY_METHOD_LENGTH = 1;

    public Integer serve(MessageHandlerContext context) {
        log.debug("[C1] {}", context);

        String url = context.makeUrl("/requestChargingStart");
        byte[] payload = context.getMessage().getPayload().getBytes();

        try {
            long vd = getVd(payload);
            boolean kt = false;

            // 43 : 중계기 소스에서 값 가져옴
            if(vd > 43){
                kt = true;
            }

            Api_C1_Req req = Api_C1_Req.builder()
                    .mbrCardNo(getCardnubmer(kt, payload))
                    .charge(getCurrent(kt, payload))
                    .chargeTime(getChargingTime(kt, payload))
                    .paymentType(getPayMethod(kt, payload))
                    .pay(getPayMount(kt, payload))
                    .plugType(getPlugType(kt, payload))
                    .build();
            context.sendRequest(req, url);
        }catch (IllegalArgumentException e) {
            // 인자가 맞지 않으면 api 를 호출하지 않는다.
            //e.printStackTrace();
            log.error("[Exception] {}", e.getStackTrace()[0]);
        }

        return 0;
    }

    private long getVd(byte[] payload) throws IllegalArgumentException {
        byte[] vd = new byte[2];
        vd[0] = payload[0];
        vd[1] = payload[1];
        return bytesToLong(vd);
    }

    private String getCardnubmer(boolean kt, byte[] payload) throws IllegalArgumentException {
        byte[] cardnubmer = new byte[16];
        int start = VD_LENGTH;

        for (int i = 0; i < 16; i++, start++) {
            cardnubmer[i] = payload[start];
        }

        String str = new String(cardnubmer);

        return str;
    }

    private String getChargingTime(boolean kt, byte[] payload) throws IllegalArgumentException {
        byte[] chargingTime = new byte[6];
        int start = VD_LENGTH + CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH + PORT_LENGTH;
        if (kt == true) {
            for (int i = 0; i < 6; i++, start++) {
                chargingTime[i] = payload[start];
            }
        }

        String str = new String(chargingTime);

        return str;
    }

    private String getCurrent(boolean kt, byte[] payload) throws IllegalArgumentException {
        byte[] current = new byte[4];
        int start = VD_LENGTH + CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH + PORT_LENGTH + CHARGING_TIME_LENGTH;
        if (kt == true) {
            for (int i = 0; i < 4; i++, start++) {
                current[i] = payload[start];
            }
        }

        String str = new String(current);

        return str;
    }

    private String getPayMethod(boolean kt, byte[] payload) throws IllegalArgumentException {
        int start = VD_LENGTH + CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH + PORT_LENGTH + CHARGING_TIME_LENGTH + CHARGING_AMOUNT_LENGTH;
        byte[] payMethod = new byte[1];
        payMethod[0] = payload[start];
        String str = new String(payMethod);

        //결제방법(충전기)  HEX 1   0x01 : 선불 결제 0x02 : 후불 과금 0x04 : 법인 결제(로밍) 0x05 : SaaS 결제
        switch(str) {
            case "1":
            case "2":
            case "4":
            case "5":
                break;
            default:
                throw new IllegalArgumentException();
        }
        return "0" + str;
    }

    private String getPlugType(boolean kt, byte[] payload) throws IllegalArgumentException {
        int start = VD_LENGTH + CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH;
        byte[] plugType = new byte[1];
        plugType[0] = payload[start];
        String str = new String(plugType);
        String out;
/*      KT 중계기 C# 소스
        (0x01: DC차데모, 0x04: AC3상,0x05:DC콤보 0x02: AC완속)
        public const string DEF_CHARGING_WEB_AC3PHASE = "1"; // Start, 급속
        public const string DEF_CHARGING_WEB_DCCHADEMO = "2"; // Start, 급속
        public const string DEF_CHARGING_WEB_DCCOMBO = "3"; // Start, 급속
        public const string DEF_CHARGING_WEB_LEFT = "L"; // 왼쪽 사용자 케이블, Btype, DC차데모
        public const string DEF_CHARGING_WEB_START = "S"; // 시작
        public const string DEF_CHARGING_WEB_RIGHT = "R"; // 오른쪽 5핀 케이블, Ctype, AC3상
 */
        switch(str){
            case "1" :  // ac 급속
                out = "01";
                break;
            case "2" :  // AC완속
                out = "02";
                break;
            case "4" :  // AC3상
                out = "04";
                break;
            case "5" :  // DC 콤보
                out = "05";
                break;
//            case "L" :  // 차데모 급속
//                out = "05";
//                break;
//            case "S" :  // 차데모 급속
//                out = "01";
//                break;
//            case "R" :  // 차데모 급속
//                out = "01";
//                break;
            default:
                throw new IllegalArgumentException();
        }
        return out;
    }

//    private long getPayMount(boolean kt, byte[] payload) throws IllegalArgumentException{
//        byte[] payAmount = new byte[10];
//        int start = VD_LENGTH + CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH + PORT_LENGTH + CHARGING_TIME_LENGTH + CHARGING_AMOUNT_LENGTH + PAY_METHOD_LENGTH;
//        if (kt == true) {
//            for (int i = 0; i < 10; i++, start++) {
//                payAmount[i] = payload[start];
//            }
//        }
//        return bytesToLong(payAmount);
//    }

    private String getPayMount(boolean kt, byte[] payload) throws IllegalArgumentException{
        byte[] payAmount = new byte[10];
        int start = VD_LENGTH + CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH + PORT_LENGTH + CHARGING_TIME_LENGTH + CHARGING_AMOUNT_LENGTH + PAY_METHOD_LENGTH;
        if (kt == true) {
            for (int i = 0; i < 10; i++, start++) {
                payAmount[i] = payload[start];
            }
        }
        String str = new String(payAmount);
        return str;
    }

    public long bytesToLong(byte[] bytes) throws IllegalArgumentException{
        String str = new String(bytes);
        return Long.parseLong(str);
    }
}


