package com.chargev.eve.adapter.socketServer.me;

import com.chargev.eve.adapter.message.HexUtils;
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
    private static final short HEADER_LENGTH = 29; // STX + DATE + station_id + charger_id + cmd + ML(2BYTES);
    private static final byte CHARER_ID_POS = 15;
    private static final byte DATE_POS = 1;
    private static final byte CMD_POS = 25;
    private static final byte PAYLOAD_POS = 29;
    private static final short MAX_BODY_LENGTH = 5000;
    private static final short FOOTER_LENGTH = 1; /*ETX*/
    private static final int DATE_LENGTH = 14;

    @Override
    protected void decode(final ChannelHandlerContext ctx, ByteBuf msgBuf, List<Object> out) {
         //여기서 헤더를 파싱한다.
        int readableBytes = msgBuf.readableBytes();

        logger.debug("decode : {} : readableBytes={}", ctx.channel().remoteAddress(), readableBytes);

        // 수신 데이터 길이가 헤더 길이보다 작으면 리턴하여 더 기다린다.
        if (readableBytes < HEADER_LENGTH) {
            return;
        }

        byte[] data;

        // 시작 바이트(STX) 체크, 오류시 버퍼의 데이터를 읽은 뒤 버린다.
        if (msgBuf.getByte(0) != STX) {
            data = new byte[readableBytes];
            msgBuf.readBytes(data);
            String hexMsg = HexUtils.toHex(data);
            logger.error("decode : {} : STX Error: Discard data : {}", ctx.channel().remoteAddress(), hexMsg);
            return;
        }

        // 헤더의 맨 마지막에 있는 바디길이 필드에서 바디 길이를 구한다.
        byte ML[] = new byte[2];
        ML[0] = msgBuf.getByte(HEADER_LENGTH - 2);  // not increase index
        ML[1] = msgBuf.getByte(HEADER_LENGTH - 1);  // not increase index
        String temp = new String(ML);
        temp = temp.trim();
        int bodyLength = Integer.parseInt(temp);

        logger.debug("decode : {} : headerLength={}, bodyLength={}", ctx.channel().remoteAddress(),
                HEADER_LENGTH, bodyLength);

        // 구한 바디 길이가 허용된 범위를 벗어나면 버퍼의 데이터를 읽은 뒤 버린다.
        if (bodyLength < 0 || bodyLength > MAX_BODY_LENGTH) {
            data = new byte[readableBytes];
            msgBuf.readBytes(data);
            String hexMsg = HexUtils.toHex(data);
            logger.error("decode : {} : Wrong body length {}: Discard data : {}",
                    ctx.channel().remoteAddress(),
                    bodyLength,
                    hexMsg);
            return;
        }

        if(bodyLength == 99) {
            byte tempCmd[] = new byte[2];
            tempCmd[0] = msgBuf.getByte(CMD_POS);
            tempCmd[1] = msgBuf.getByte(CMD_POS + 1);
            String cmd = new String(tempCmd);

            if(cmd.equals("S3") || cmd.equals("S4")){ // S3, S4 전문에만 해당
                byte tempRealML[] = new byte[3];
                tempRealML[0] = msgBuf.getByte(HEADER_LENGTH);
                tempRealML[1] = msgBuf.getByte(HEADER_LENGTH + 1);
                tempRealML[2] = msgBuf.getByte(HEADER_LENGTH + 2);
                temp = new String(tempRealML);
                temp = temp.trim();
                bodyLength = Integer.parseInt(temp);
                bodyLength += 3; // realML
            }
        }

        // 메시지 전체 길이를 계산한다.
        int wholeLength = HEADER_LENGTH + bodyLength + FOOTER_LENGTH;

        // 수신 메시지 길이가 미달이면 리턴하여 더 기다린다.
        if(readableBytes < wholeLength) {
            return;
        }

        // 종료 바이트(ETX) 체크. 오류시 버퍼의 데이터를 읽은 뒤 버린다.
        if (msgBuf.getByte(readableBytes - 1) != ETX) {
            data = new byte[readableBytes];
            msgBuf.readBytes(data);
            String hexMsg = HexUtils.toHex(data);
            logger.error("decode : {} : ETX Error: Discard data : {}",
                    ctx.channel().remoteAddress(),
                    hexMsg);
            return;
        }

        int i;
        byte[] dataFull = new byte[wholeLength];
        msgBuf.readBytes(dataFull);
        String str1 = new String(dataFull);

        // date 확인

        // cmd 확인
        String cmd = parseCmd(dataFull);
        if(isCmdValid(cmd) == false){
            data = new byte[readableBytes];
            msgBuf.readBytes(data);
            String hexMsg = HexUtils.toHex(data);
            logger.error("decode : {} : messageId Error: Discard data : {}",
                    ctx.channel().remoteAddress(),
                    str1);
            return;
        }

        String msg = new String(dataFull);
        logger.info("received packet : {}", msg);
        // 전부다 넘김
        out.add(dataFull);
    }

    public static boolean isCmdValid(String cmd){
        switch (cmd){
            case "A1":
            case "A2": // SMS 전송
            case "S2": // 추가 회원정보 download, 삭제 회원정보 download
            case "B1": // 충전기 점검, 충전기 운영, 운영중지 전문 발송, 점검중 전문 발송, TEST 전문 발송
            case "C1": // 중계서버 충전시작, 충전중단
            case "F1": // 펌웨어 업데이트
            case "S3":
            case "S4":
            case "G1":
            case "G2":
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

    public static String parseDate(byte[] dataByte) {
        byte[] dateBuf;
        dateBuf = new byte[DATE_LENGTH];
        for(int i=0; i<DATE_LENGTH; i++){
            dateBuf[i] = dataByte[DATE_POS + i];
        }
        return new String(dateBuf);
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

    public static String parsePayload(byte[] dataByte, int payloadLength) {        
        byte[] chargerIdBuf;
        chargerIdBuf = new byte[payloadLength];

        for(int i = 0; i < payloadLength; i++){
            chargerIdBuf[i] = dataByte[PAYLOAD_POS + i];
        }
        return new String(chargerIdBuf);
    }

    public static int getPayloadLength(byte[] data) {
        byte[] ML = new byte[2];
        ML[0] = data[HEADER_LENGTH - 2];
        ML[1] = data[HEADER_LENGTH - 1];
        String temp = new String(ML);
        temp = temp.trim();
        int bodyLength = Integer.parseInt(temp);        
        return bodyLength;
    }
}
