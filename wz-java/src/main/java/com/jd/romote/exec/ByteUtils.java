package com.jd.romote.exec;
/**
 * Bytes数组处理工具
 * @author
 */
public class ByteUtils {

    public static int bytes2Int(byte[] b, int start, int len) {
        int sum = 0;
        int end = start + len;
        for (int i = start; i < end; i++) {
            int n = ((int) b[i]) & 0xff;
            n <<= (--len) * 8;
            sum = n + sum;
        }
        return sum;
    }

    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i) & 0xff);
        }
        return b;
    }

    public static String bytes2String(byte[] b, int start, int len) {
        return new String(b, start, len);
    }

    public static byte[] string2Bytes(String str) {
        return str.getBytes();
    }

    public static float bytes2Float(byte[] b, int index) {
        int l = b[index] & 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        return Float.intBitsToFloat(l);
    }

    /**
     * 高位在前
     * @param b
     * @param index
     * @return
     */
    public static float bytes2Float1(byte[] b, int index) {
        int l = b[index + 3] & 0xff;
        l |= ((long) b[index + 2] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 1] << 16);
        l &= 0xffffff;
        l |= ((long) b[index] << 24);
        return Float.intBitsToFloat(l);
    }

    public static byte[] float2Bytes(float x, int len) {
         byte[] b = new byte[len];
        int l = Float.floatToIntBits(x);
        for (int i = 0; i < len; i++) {
            b[i] = new Integer(l).byteValue();
            l = l >> 8;
        }
        return b;
    }

    public static byte[] double2Bytes(double x, int len) {
         byte[] b = new byte[len];
        long l = Double.doubleToLongBits(x);
        for (int i = 0; i < len; i++) {
            b[i] = new Long(l).byteValue();
            l = l >> 8;
        }
        return b;
    }
    /**
     * 高位在前
     * @param b
     * @param index
     * @return
     */
    public static double bytes2Double1(byte[] b, int index) {
        long l = b[index + 7] & 0xff;
        l |= ((long) b[index + 6] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 5] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 4] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[index + 3] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[index + 2] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[index + 1] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[index] << 56);
        return Double.longBitsToDouble(l);
    }

    public static double bytes2Double(byte[] b, int index) {
        long l;
        l = b[index];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[index + 4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[index + 5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[index + 6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[index + 7] << 56);
        return Double.longBitsToDouble(l);
    }

    public static byte[] long2Bytes(long l, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[i] = new Long(l).byteValue();
            l = l >> 8;
        }
        return b;
    }

    public static long bytes2Long(byte[] b, int index) {
        long l;
        l = b[index];
        l &= 0xff;
        l |= ((long) b[index + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[index + 4] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[index + 5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[index + 6] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[index + 7] << 56);
        return l;
    }

    /**
     * 高位在前
     * @param b
     * @param index
     * @return
     */
    public static long bytes2Long1(byte[] b, int index) {
        long l = b[index + 7] & 0xff;
        l |= ((long) b[index + 6] << 8);
        l &= 0xffff;
        l |= ((long) b[index + 5] << 16);
        l &= 0xffffff;
        l |= ((long) b[index + 4] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[index + 3] << 32);
        l &= 0xffffffffffl;
        l |= ((long) b[index + 2] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[index + 1] << 48);
        l &= 0xffffffffffffffl;
        l |= ((long) b[index] << 56);
        return l;
    }

    public static byte[] bytesReplace(byte[] originalBytes, int offset, int len, byte[] replaceBytes) {
        byte[] newBytes = new byte[originalBytes.length + (replaceBytes.length - len)];
        System.arraycopy(originalBytes, 0, newBytes, 0, offset);
        System.arraycopy(replaceBytes, 0, newBytes, offset, replaceBytes.length);
        System.arraycopy(originalBytes, offset + len, newBytes, offset + replaceBytes.length, originalBytes.length - offset - len);
        return newBytes;
    }
}