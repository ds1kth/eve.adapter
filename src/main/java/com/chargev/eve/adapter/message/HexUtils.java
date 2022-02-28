package com.chargev.eve.adapter.message;

import javax.annotation.Nonnull;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

public class HexUtils {
    private static final char[] HEX_CODE = "0123456789ABCDEF".toCharArray();

    private HexUtils() {
    }

    public static String toHex(String s) {
        return toHex(s.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 바이트 배열을 16진수 표현 문자열로 변환
     * 예) 0x022019 ---> "022019"
     *
     * @param data 변환할 바이트 배열
     * @return 변환된 16진수 표현 문자열
     */
    public static String toHex(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(HEX_CODE[(b >> 4) & 0xF]);
            r.append(HEX_CODE[(b & 0xF)]);
        }
        return r.toString();
    }

    public static String toHex8(int num) {
        return String.format("%02X", num);
    }

    public static String toHex16(int num) {
        return String.format("%04X", num);
    }

    public static String toHex32(long num) {
        return String.format("%08X", num);
    }

    public static String toHex64(long num) {
        return String.format("%016X", num);
    }

    /**
     * 16진 문자열을 utf-8 코드 페이지에 해당하는 문자열로 변경한다.
     * 널문자(0x00)는 공백으로 치환한다.
     *
     * @param hex 16진문자열 (utf-8)
     * @return 문자열
     */
    public static String hexToString(@Nonnull String hex) {
        String str;
        str = nullCharactersToSpace(new String(hexToBytes(hex), StandardCharsets.UTF_8));
        return str;
    }

    /**
     * 16진 문자열을 byte 배열로 변환한다. 두자리 16진수를 묶어서 byte로 변환한다
     *
     * @param hex 16진 문자열
     * @return byte 배열
     */
    public static byte[] hexToBytes(@Nonnull String hex) {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < hex.length(); i += 2) {
            String b = hex.substring(i, i + 2);
            bytes[i / 2] = (byte) Integer.parseInt(b, 16);
        }
        return bytes;
    }

    /**
     * 16진 문자열을 ms949 코드 페이지에 해당하는 문자열로 변경한다.
     * 전문 수신 문자열 값은 ms949 문자셋으로 간주(euc-kr, cp949는 '똠', '뷁' 같은 확장완성형 문자를 표현할 수 없다)
     * 널문자(0x00)는 공백으로 치환한다.
     *
     * @param hex 16진문자열 (ms949)
     * @return 문자열
     */
    public static String hexMsgToString(String hex) {
        if (hex == null) {
            throw new IllegalArgumentException();
        }
        String str;
        try {
            str = nullCharactersToSpace(new String(hexToBytes(hex), "MS949"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            str = hexToString(hex);
        }
        return str;
    }

    /**
     * 문자열에 널문자(0x00) 이 포함된 경우 그 값을 공백으로 치환한 문자열을 반납한다.
     *
     * @param str 원본 문자열
     * @return 널문자가 공백으로 치환된 문자열
     */
    private static String nullCharactersToSpace(String str) {
        if (!hasNullCharacter(str)) {
            return str;
        }
        char[] buffer = new char[str.length()];
        int index = 0;
        for (char c : str.toCharArray()) {
            buffer[index++] = c == 0 ? ' ' : c;
        }
        return new String(buffer);
    }

    /**
     * 문자열에 널문자(0x00)이 포함되어 있는지 확인한다.
     *
     * @param str 체크할 문자열
     * @return 널문자가 포함되어 있으면 true
     */
    private static boolean hasNullCharacter(String str) {
        for (char c : str.toCharArray()) {
            if (c == 0) {
                return true;
            }
        }
        return false;
    }
}
