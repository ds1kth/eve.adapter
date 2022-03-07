package com.chargev.eve.adapter.socketServer.me;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class MeMessageDecoder extends ByteToMessageDecoder {

    private static final Logger logger = LoggerFactory.getLogger(MeMessageDecoder.class);
    private static final byte STX = 'S';
    private static final byte ETX = 'E';
    private static final short HEADER_PLUS_ETX_LENGTH = 28; // STX + DATE + station_id + charger_id + cmd + ETX;
    private static final byte CHARER_ID_POS = 15;
    private static final byte CMD_POS = 25;
    private static final byte PAYLOAD_POS = 27;

    @Override
    protected void decode(final ChannelHandlerContext ctx, ByteBuf msgBuf, List<Object> out) {
         //여기서 헤더를 파싱한다.
        int readableBytes = msgBuf.readableBytes();

        logger.debug("decode : {} : readableBytes={}", ctx.channel().remoteAddress(), readableBytes);

        // 수신 데이터 길이가 헤더 길이보다 작으면 리턴하여 더 기다린다.
        if (readableBytes < HEADER_PLUS_ETX_LENGTH) {
            return;
        }

        byte[] data;
        data = new byte[readableBytes];
        msgBuf.readBytes(data);
        String str1 = new String(data);

        //System.out.println(str1);

        // 시작 바이트(STX) 체크, 오류시 버퍼의 데이터를 읽은 뒤 버린다.
        if (msgBuf.getByte(0) != STX) {
            logger.error("decode : {} : STX Error: Discard data : {}", 
                    ctx.channel().remoteAddress(), str1);
            return;
        }

        // 종료 바이트(ETX) 체크. 오류시 버퍼의 데이터를 읽은 뒤 버린다.
        if (msgBuf.getByte(readableBytes - 1) != ETX) {
            logger.error("decode : {} : ETX Error: Discard data : {}",
                    ctx.channel().remoteAddress(),
                    str1);
            return;
        }
        
        // date 확인


        // cmd 확인
        //String cmd = parseCmd(msgBuf);
        String cmd = parseCmd(data);

        if(isCmdValid(cmd) == false){
            logger.error("decode : {} : messageId Error: Discard data : {}",
                    ctx.channel().remoteAddress(),
                    str1);
            return;
        }

        // 전부다 넘김
        out.add(data);
    }

    public static boolean isCmdValid(String cmd){
        switch (cmd){
            case "A1":
            case "A2": // SMS 전송
            case "S2": // 추가 회원정보 download, 삭제 회원정보 download
            case "B1": // 충전기 점검, 충전기 운영, 운영중지 전문 발송, 점검중 전문 발송, TEST 전문 발송
            case "C1": // 중계서버 충전시작, 충전중단
            case "F1": // 펌웨어 업데이트
            case "G1":
            case "I1": // 소프트 리셋 전문 발송
            case "K1": // 예약전문, 예약취소
            case "N1":
            case "R1": // 하드웨어 리셋 전문 발송
            case "S1": // 정보 전문 발송
            case "U1": // 설치 전문 발송
            case "V1": // 전압 설정case
            case "H2":
                return true;
            default :
                return false;
        }
    }

    public static String parseCmd(ByteBuf msgBuf) {
        byte[] cmdBuf;
        cmdBuf = new byte[2];
        cmdBuf[0] = msgBuf.getByte(CMD_POS);
        cmdBuf[1] = msgBuf.getByte(CMD_POS + 1);
        return new String(cmdBuf);
    }

    public static String parseCmd(byte[] dataByte) {
        byte[] cmdBuf;
        cmdBuf = new byte[2];
        cmdBuf[0] = dataByte[CMD_POS];
        cmdBuf[1] = dataByte[CMD_POS + 1];
        return new String(cmdBuf);
    }

    public static String parseChargerId(byte[] dataByte) {
        byte[] chargerIdBuf;
        chargerIdBuf = new byte[10];
        for(int i = 0; i < 10; i++){
            chargerIdBuf[i] = dataByte[CHARER_ID_POS + i];
        }
        return new String(chargerIdBuf);
    }

    /**
     * 
     * @param dataByte
     * @param dataSize : 헤더 + payload + etx 사이즈
     * @return
     */
    public static String parsePayload(byte[] dataByte, int dataSize) {
        int payloadLength = dataSize - (PAYLOAD_POS + 1);
        byte[] chargerIdBuf;
        chargerIdBuf = new byte[payloadLength];

        for(int i = 0; i < payloadLength; i++){
            chargerIdBuf[i] = dataByte[PAYLOAD_POS + i];
        }
        return new String(chargerIdBuf);
    }
}
