package com.sohu.smc.common.leveldb.local;

/**
 * Created with IntelliJ IDEA.
 * User: huixiao200068
 * Date: 13-12-22
 * Time: 下午1:48
 * To change this template use File | Settings | File Templates.
 */
public class BinUtil {

   public static void putDoubleLongAndByte(byte[] bb,long topicId,long rid_x,byte mid){
       bb[0] = (byte) (topicId >> 56);
       bb[1] = (byte) (topicId >> 48);
       bb[2] = (byte) (topicId >> 40);
       bb[3] = (byte) (topicId >> 32);
       bb[4] = (byte) (topicId >> 24);
       bb[5] = (byte) (topicId >> 16);
       bb[6] = (byte) (topicId >> 8);
       bb[7] = (byte) (topicId >> 0);
       bb[8]=mid;
       bb[9] = (byte) (rid_x >> 56);
       bb[10] = (byte) (rid_x >> 48);
       bb[11] = (byte) (rid_x >> 40);
       bb[12] = (byte) (rid_x >> 32);
       bb[13] = (byte) (rid_x >> 24);
       bb[14] = (byte) (rid_x >> 16);
       bb[15] = (byte) (rid_x >> 8);
       bb[16] = (byte) (rid_x >> 0);
   }

    public static void putInt(byte[] bb, int x, int index) {
        bb[index + 0] = (byte) (x >> 24);
        bb[index + 1] = (byte) (x >> 16);
        bb[index + 2] = (byte) (x >> 8);
        bb[index + 3] = (byte) (x >> 0);
    }

    public static void putInt(byte[] bb, byte prefix, int x, int index) {
        bb[index + 0] = prefix;
        bb[index + 1] = (byte) (x >> 24);
        bb[index + 2] = (byte) (x >> 16);
        bb[index + 3] = (byte) (x >> 8);
        bb[index + 4] = (byte) (x >> 0);
    }

    public static int getInt(byte[] bb, int index) {
        return (int) ((((bb[index + 0] & 0xff) << 24)
                | ((bb[index + 1] & 0xff) << 16)
                | ((bb[index + 2] & 0xff) << 8) | ((bb[index + 3] & 0xff) << 0)));
    }

    public static byte getByte(byte[] bb, int index) {
        return (byte) ((bb[index] & 0xff) << 0);
    }

    public static void putByte(byte[] bb,byte b,int index) {
         bb[ index+0 ] = b;
    }


    public static void putLong(byte[] bb, long x, int index) {
        bb[index + 0] = (byte) (x >> 56);
        bb[index + 1] = (byte) (x >> 48);
        bb[index + 2] = (byte) (x >> 40);
        bb[index + 3] = (byte) (x >> 32);
        bb[index + 4] = (byte) (x >> 24);
        bb[index + 5] = (byte) (x >> 16);
        bb[index + 6] = (byte) (x >> 8);
        bb[index + 7] = (byte) (x >> 0);
    }

    public static void putLong(byte[] bb, byte tableIndex, long x, int index) {

        bb[index + 0] = tableIndex;
        bb[index + 1] = (byte) (x >> 56);
        bb[index + 2] = (byte) (x >> 48);
        bb[index + 3] = (byte) (x >> 40);
        bb[index + 4] = (byte) (x >> 32);
        bb[index + 5] = (byte) (x >> 24);
        bb[index + 6] = (byte) (x >> 16);
        bb[index + 7] = (byte) (x >> 8);
        bb[index + 8] = (byte) (x >> 0);
    }

    public static long getLong(byte[] bb, int index) {
        return ((((long) bb[index + 0] & 0xff) << 56)
                | (((long) bb[index + 1] & 0xff) << 48)
                | (((long) bb[index + 2] & 0xff) << 40)
                | (((long) bb[index + 3] & 0xff) << 32)
                | (((long) bb[index + 4] & 0xff) << 24)
                | (((long) bb[index + 5] & 0xff) << 16)
                | (((long) bb[index + 6] & 0xff) << 8) | (((long) bb[index + 7] & 0xff) << 0));
    }

    public static boolean isEqual(byte[] a1,byte[] a2){
        if(a1==null || a2 == null){
            return false;
        }
        // byte数组比较
        boolean result = true;
        for (int i = 0; i < a1.length && i<a2.length; i++) {
            if (a1[i] != a2[i]) {
                result = false;
                break;
            }
        }
        return result;
    }


    /**
     * 微博字段定义:
     * <p/>
     * Fileds(Total 32bits)                                         Usage           Offset  Occupied    Value
     * 0000 0000 0000 0000 0000 0000 0000 0000                              (bits)
     * 图片类型      0       2           0图片 1视频
     * 是新闻         2       1           0否 1是
     * <p/>
     * <p/>
     * flag是int型变量, 最多可扩展至32位, 目前占用3位
     * <p/>
     * 设置状态方法:
     *
     * @param flag  状态值
     * @param idx   状态的起始位置
     * @param bits  状态占用位数
     * @param value 设置值
     * @return flag  运算后的状态值
     */
    public static int setFlag(int flag, int idx, int bits, int value) {
        int value_of_bits = (1 << bits) - 1;
        return (flag & ~(value_of_bits << idx) | (-1 & (value << idx)));
    }

    public static int getFlag(int flag, int idx, int bits) {
        long value_of_bits = (1 << bits) - 1;
        return (int) ((flag >> idx) & value_of_bits);
    }

    /**
     * test if the bit at the specific position is 1
     *
     * @param b   byte data
     * @param pos position started with 0, and less than 8
     * @return bit value,0 or 1
     */
    public static int test(byte b, int pos) {
        byte mask = 1;
        return ((mask << pos) & b) >> pos;
    }

    /**
     * set the specific bit to 1
     *
     * @param b   byte data
     * @param pos position started with 0, and less than 8
     * @return byte data
     */
    public static byte set(byte b, int pos) {
        byte mask = 1;
        b |= mask << pos;
        return b;
    }

    /**
     * clear the specific bit to 0
     *
     * @param b   byte value
     * @param pos position started with 0, and less than 8
     * @return byte data
     */
    public static byte clear(byte b, int pos) {
        byte mask = 1;
        b &= ~(mask << pos);
        return b;
    }
}
