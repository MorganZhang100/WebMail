package cs601.webmail.manager;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DecodeManager {

    private static byte ESCAPE_CHAR = '=';

    public static String Base64(String s,String aimCharset) throws UnsupportedEncodingException {
        System.out.println(s);
        return new String(Base64.getDecoder().decode(s),aimCharset);
    }

    public static byte[] decodeQuotedPrintable(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        for (int i = 0; i < bytes.length; i++) {
            int b = bytes[i];
            if (b == ESCAPE_CHAR) {
                try {
                    int u = Character.digit((char) bytes[++i], 16);
                    int l = Character.digit((char) bytes[++i], 16);
                    if (u == -1 || l == -1) {
                        //System.out.println("QP error");
                        //throw new DecoderException("Invalid quoted-printable encoding");
                    }
                    buffer.write((char) ((u << 4) + l));
                } catch (ArrayIndexOutOfBoundsException e) {
                    //System.out.println("QP error");
                    //throw new DecoderException("Invalid quoted-printable encoding");
                }
            } else {
                buffer.write(b);
            }
        }
        return buffer.toByteArray();
    }

    public static String DP(String s,String aimCharset) throws UnsupportedEncodingException {
        String t = new String(decodeQuotedPrintable(s.getBytes()),aimCharset);
        if(aimCharset.equals("GBK")) {
            String iso = new String(t.getBytes("UTF-8"),"ISO-8859-1");
            String utf8 = new String(iso.getBytes("ISO-8859-1"),"UTF-8");

            String f = getUTF8StringFromGBKString(t);
            return f;
        }
        else return t;
    }

    public static String getUTF8StringFromGBKString(String gbkStr) {
        try {
            return new String(getUTF8BytesFromGBKString(gbkStr), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new InternalError();
        }
    }

    public static byte[] getUTF8BytesFromGBKString(String gbkStr) {
        int n = gbkStr.length();
        byte[] utfBytes = new byte[3 * n];
        int k = 0;
        for (int i = 0; i < n; i++) {
            int m = gbkStr.charAt(i);
            if (m < 128 && m >= 0) {
                utfBytes[k++] = (byte) m;
                continue;
            }
            utfBytes[k++] = (byte) (0xe0 | (m >> 12));
            utfBytes[k++] = (byte) (0x80 | ((m >> 6) & 0x3f));
            utfBytes[k++] = (byte) (0x80 | (m & 0x3f));
        }
        if (k < utfBytes.length) {
            byte[] tmp = new byte[k];
            System.arraycopy(utfBytes, 0, tmp, 0, k);
            return tmp;
        }
        return utfBytes;
    }

    public static String getUTF8FromSubject(String s) throws UnsupportedEncodingException {
        Matcher m = resultOfRegExSearch(s, "(?<==\\?)[UTF\\-8gb2312]+(?=\\?)");
        if(m.find()) {
            String charset = m.group();

            m = resultOfRegExSearch(s,"(?<=" + charset + "\\?)[\\S\\s]?(?=\\?)");
            String encoding = null;
            if(m.find()) encoding = m.group();

            String body = null;
            m = resultOfRegExSearch(s,"(?<=" + charset + "\\?" + encoding + "\\?)[\\S\\s]+(?=\\?=)");
            if(m.find()) body = m.group();

            if(encoding.equals("B")) body = DecodeManager.Base64(body,charset);
            else if(encoding.equals("Q")) body = DecodeManager.DP(body,charset);
            return body;
        }
        else return s;
    }

    static Matcher resultOfRegExSearch(String s, String regEx) {
        System.out.println(s + "  " + regEx);
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(s);
        return m;
    }
}
