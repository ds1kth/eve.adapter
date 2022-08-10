
package com.chargev.eve.adapter.message.handler;

import com.chargev.eve.adapter.apiClient.api.Api_C1_Common_Start_Req;
import com.chargev.eve.adapter.apiClient.api.Api_C1_Common_Stop_Req;
import com.chargev.eve.adapter.message.Message;
import com.chargev.eve.adapter.message.MessageHandler;
import com.chargev.eve.adapter.message.MessageHandlerContext;
import com.chargev.eve.adapter.message.RespMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * API:
 *
 * KT -
 *   3.27 RequestChargingStart
 *   충전기가 충전을 시작하도록 제어 명령을 전달 한다.
 *   3.28 RequestChargeStop
 *   충전기가 후불 결제를 진행하도록 제어 명령 전달한다.
 *
 * Chargev -
 *   3.29	RequestChargingStartAndStop
 *   충전기 충전 시작, 종료 요청합니다.
 *
 * Spec :
 * 차지비(충전 시작 , 충전 중지 - Q1)
 * KT(충전 시작 - B2), (충전 중지 - E2)
 *
 * Test :
 * func_1_8
 */
@Slf4j
@Service("Message_C1_Handler")
public class Message_C1_Handler implements MessageHandler<MessageHandlerContext, Integer> {
    private final int CARD_NUMBER_LENGTH = 16;
    private final int CHARGING_TIME_LENGTH = 6;
    private final int DB_CODE_LENGTH = 20;
    private final int PORT_LENGTH = 1;
    private final int CHARGING_AMOUNT_LENGTH = 4;
    private final int PAY_METHOD_LENGTH = 1;

    public Integer serve(MessageHandlerContext context) {
        log.debug("[C1] {}", context);

        // if(isOcpp(context) == true) {
        //     return 0;
        // }

        String url = null;
        byte[] payload = context.getMessage().getPayload().getBytes();

        try {
            long vd = context.getMessage().getPayloadLength();
            boolean kt = false;

            // 43 : 중계기 소스에서 값 가져옴.
            // TODO : 43 이하라 하더라도 KT 의 "충전 중지" 전문 일수 있다.
            if (vd > 43) {
                kt = true;
                processKT(context, payload);
            }
            else {
                processChargEV(context, payload);
            }
        }
        catch (Exception e){
            log.error("[Exception] {}", e.getStackTrace()[0]);
            return 0;
        }

        context.setRespMessage(null);
        RespMessage respMessage = RespMessage.builder()
                .INS("1C")
                .ML("5")
                .VD("S    ")
                .build();
        context.setRespMessage(respMessage);
        return 1;
    }

    /**
     *
     * @param context
     * @param payload
     * @throws Exception
     */
    void processKT(MessageHandlerContext context, byte[] payload) throws Exception {
        String url = null;
        boolean kt = true;

        if(isChargingStart(kt, payload) == true) {
            Api_C1_Common_Start_Req req = null;
            url = context.makeUrl("/startCharge");
            req = Api_C1_Common_Start_Req.builder()
                    .mbrCardNo(getCardnubmer(kt, payload))
                    .chargeReqAmount(getCurrent(kt, payload))
                    .chargeTime(getChargingTime(kt, payload))
                    .payMethod(getPayMethod(kt, payload))
                    .chargePrice(getPayMount(kt, payload))
                    .chargerType(getPlugType(kt, payload))
                    .build();
            context.sendRequest(req, url, context.getMessage().getCmd(), "KT(B2)");
        }
        else {
            Api_C1_Common_Stop_Req req = null;
            url = context.makeUrl("/stopCharge");
            req = commonChargeStop(kt, payload);
            context.sendRequest(req, url, context.getMessage().getCmd(), "KT(E2)");
        }
    }

    /**
     *
     * @param context
     * @param payload
     * @throws Exception
     */
    void processChargEV(MessageHandlerContext context, byte[] payload) throws Exception {
        String url = null;
        boolean kt = false;

        if(isChargingStart(kt, payload) == true) {
            Api_C1_Common_Start_Req req = null;
            url = context.makeUrl("/startCharge");
            req = Api_C1_Common_Start_Req.builder()
                    .mbrCardNo(getCardnubmer(kt, payload))
                    .tid(getTid(kt, payload))   // DB 고유정보 코드
                    .chargingCommand(getChargingCommand(kt, payload))
                    .chargeTime(getChargingTime(kt, payload))
                    .build();
            context.sendRequest(req, url, context.getMessage().getCmd(), "ChargEV(Q1)");
        }
        else {
            Api_C1_Common_Stop_Req req = null;
            url = context.makeUrl("/stopCharge");
            req = commonChargeStop(kt, payload);
            context.sendRequest(req, url, context.getMessage().getCmd(), "ChargEV(Q1)");
        }
    }

    private Api_C1_Common_Stop_Req commonChargeStop(boolean kt, byte[] payload) {
        Api_C1_Common_Stop_Req req = null;
        req = Api_C1_Common_Stop_Req.builder()
                .mbrCardNo(getCardnubmer(kt, payload))
                .chargeTime(getChargingTime(kt, payload))
                .tid(getTid(kt, payload))   // DB 고유정보 코드
                .payMethod("00")
                .chargingCommand(getChargingCommand(kt, payload))
                .build();

        return req;
    }

    private long getVd(byte[] payload) throws IllegalArgumentException {
        byte[] vd = new byte[2];
        vd[0] = payload[0];
        vd[1] = payload[1];
        return bytesToLong(vd);
    }

    private String getCardnubmer(boolean kt, byte[] payload) throws IllegalArgumentException {
        byte[] cardnubmer = new byte[16];
        int start = 0;

        for (int i = 0; i < 16; i++, start++) {
            cardnubmer[i] = payload[start];
        }

        String str = new String(cardnubmer);

        return str;
    }

    private String getChargingTime(boolean kt, byte[] payload) throws IllegalArgumentException {
        byte[] chargingTime = new byte[6];
        int start = CARD_NUMBER_LENGTH;

        for (int i = 0; i < 6; i++, start++) {
            chargingTime[i] = payload[start];
        }

        String str = new String(chargingTime);

        return str;
    }

    private String getCurrent(boolean kt, byte[] payload) throws IllegalArgumentException {
        byte[] current = new byte[4];
        int start = CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH + PORT_LENGTH + CHARGING_TIME_LENGTH;
        if (kt == true) {
            for (int i = 0; i < 4; i++, start++) {
                current[i] = payload[start];
            }
        }

        String str = new String(current);

        return str;
    }

    private String getPayMethod(boolean kt, byte[] payload) throws IllegalArgumentException {
        int start = CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH + PORT_LENGTH + CHARGING_TIME_LENGTH + CHARGING_AMOUNT_LENGTH;
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
        int start = CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH;
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
            // 확인 필요
            case "1" :  // AC3상
                out = "04";
                break;
            case "2" :  // DC차데모
                out = "01";
                break;
            case "3" :  // DC 콤보
                out = "05";
                break;
            case "L" :  // AC완속
            case "S" :  // AC완속
            case "R" :  // AC완속
                out = "02";
                break;
            default:
                throw new IllegalArgumentException();
        }
        return out;
    }

    private String getChargingCommand(boolean kt, byte[] payload) throws IllegalArgumentException {
        int start = CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH;
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
            case "1" :  // AC3상
            case "S" :  // AC완속
            case "R" :  // AC완속
                out = "01";
                break;
            case "2" :  // DC차데모
            case "L" :  // 왼쪽
                out = "02";
                break;            
            case "3" :  // DC combo
                out = "04";
                break;
            case "E" :  // 종료
                out = "E";
                break;
            case "I" :  // 즉시 종료
                out = "I";
                break;                    
            default:
                throw new IllegalArgumentException();
        }
        return out;
    }

//    private long getPayMount(boolean kt, byte[] payload) throws IllegalArgumentException{
//        byte[] payAmount = new byte[10];
//        int start = CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH + PORT_LENGTH + CHARGING_TIME_LENGTH + CHARGING_AMOUNT_LENGTH + PAY_METHOD_LENGTH;
//        if (kt == true) {
//            for (int i = 0; i < 10; i++, start++) {
//                payAmount[i] = payload[start];
//            }
//        }
//        return bytesToLong(payAmount);
//    }

    private String getPayMount(boolean kt, byte[] payload) throws IllegalArgumentException{
        byte[] payAmount = new byte[10];
        int start = CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH + PORT_LENGTH + CHARGING_TIME_LENGTH + CHARGING_AMOUNT_LENGTH + PAY_METHOD_LENGTH;
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

     private boolean isChargingStart(boolean kt, byte[] payload) {
         int start = CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH + DB_CODE_LENGTH;
         byte[] plugType = new byte[1];
         plugType[0] = payload[start];
         String str = new String(plugType);

         switch(str){
             case "I" :  // 즉시중지
             case "E" :  // 종료
                return false;
             default:
                 return true;
         }
     }

     private String getTid(boolean kt, byte[] payload) throws IllegalArgumentException{
         byte[] temp = new byte[16];
         int start = CARD_NUMBER_LENGTH + CHARGING_TIME_LENGTH;

         for (int i = 0; i < 16; i++, start++) {
             temp[i] = payload[start];
         }

         String tid = new String(temp);
         return tid;
     }

    //  boolean isOcpp(MessageHandlerContext context) {
    //     switch(context.getMessage().getChargerId()) {
    //         case "2009563011" :
    //         case "2009563012" :
    //         case "2009573011" :
    //         case "2009573012" :
    //         case "2009583011" :
    //         case "2009583012" :
    //         case "2009593011" :
    //         case "2009593012" :
    //         case "2009603011" :
    //         case "2009603012" :
    //         case "2009613011" :
    //         case "2009613012" :

    //             byte[] payload = context.getMessage().getPayload().getBytes();
    //             String cardNumber = getCardnubmer(false, payload);
    //             String urlTemp = context.makeUrl();
    //             int pos = urlTemp.indexOf("v1") + 2;
    //             String _url = urlTemp.substring(0, pos);

    //             if(isChargingStart(false, payload) == true) {
    //                 String _payload = "0001" + cardNumber;
    //                 Message message = Message.builder()
    //                     .chargerId(context.getMessage().getChargerId())
    //                     .payload(_payload)
    //                     .payloadLength(20)
    //                     .cmd("O1").build();
    //                 _url += "/transaction/" + context.getMessage().getChargerId() + "/start";
    //                 MessageHandlerContext _context = new MessageHandlerContext(message, _url);
    //                 Message_O1_Handler handler = new Message_O1_Handler();
    //                 handler.serve(_context);
    //             }
    //             else {
    //                 String _tid = getTid(false, payload).trim();
    //                 int tidLen = _tid.length();
    //                 int rest = 8 - tidLen;
    //                 String tidStr = "";
    //                 for(int i=0; i < rest; i++) {
    //                     tidStr += "0";
    //                 }
    //                 tidStr += _tid;
    //                 tidStr += "01";

    //                 Message message = Message.builder()
    //                     .chargerId(context.getMessage().getChargerId())
    //                     .payload(tidStr)
    //                     .payloadLength(10)
    //                     .cmd("O2").build();
    //                 _url += "/transaction/" + context.getMessage().getChargerId() + "/stop";
    //                 MessageHandlerContext _context = new MessageHandlerContext(message, _url);
    //                 Message_O2_Handler handler = new Message_O2_Handler();
    //                 handler.serve(_context);
    //             }

    //             return true;
    //         default :
    //             return false;
    //     }
    //  }
}


