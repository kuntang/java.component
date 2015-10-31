package com.sohu.smc.common.leveldb.util;

import com.sohu.smc.common.leveldb.sede.Serde;

import java.util.Comparator;

public class Util {

    /** Writes an int in a variable-length format.  Writes between one and
     * five bytes.  Smaller values take fewer bytes.  Negative numbers are not
     * supported.
     *
     */
    public static byte[] writeVInt(int i){
        int n = i;
        int len = 0;
        while ((n & ~0x7F) != 0) {
            len++;
            n >>>= 7;
        }

        byte[] ret = new byte[len + 1];
        n = 0;
        while ((i & ~0x7F) != 0) {
            ret[n++] = (byte)((i & 0x7f) | 0x80);
            i >>>= 7;
        }
        ret[n++] = (byte)i;

        return ret;
    }

    /** Reads an int stored in variable-length format.  Reads between one and
     * five bytes.  Smaller values take fewer bytes.  Negative numbers are not
     * supported.
    */
    public static int readVInt(byte[] bytes){
        return readVInt(bytes, 0);
    }
    public static int readVInt(byte[] bytes, int start) {
        byte b = bytes[start];
        int n = start + 1;
        int i = b & 0x7F;
        for (int shift = 7; (b & 0x80) != 0; shift += 7) {
            b = bytes[n++];
            i |= (b & 0x7F) << shift;
        }
        return i;
    }

    public static <H, R> byte[] combine(Serde<H> hashKeySerde, Serde<R> rangeKeySerde, H hashKey, R rangeKey) {
        byte[] rangeBytes = new byte[0];

        if (rangeKey != null) {
            rangeBytes = rangeKeySerde.toBytes(rangeKey);
        }

        return combine(hashKeySerde.toBytes(hashKey), rangeBytes);
    }

    /**
     * Utility function to combine a hash key and range key. Hash/range key pairs
     * are expected to be persisted in the following byte format:
     *
     * <pre>
     * [4 byte hash key length]
     * [arbitrary hash key bytes]
     * [4 byte range key length]
     * [arbitrary range key bytes]
     * </pre>
     *
     * @param hashKeyBytes
     *          Are the hash key's bytes.
     * @param rangeKeyBytes
     *          Are the range key's bytes.
     * @return Returns a byte array defined by the format above.
     */
    public static byte[] combine(byte[] hashKeyBytes, byte[] rangeKeyBytes) {
        byte[] result_hash = writeVInt(hashKeyBytes.length);
        byte[] result_rangeKey = writeVInt(rangeKeyBytes.length);
        byte[] result = new byte[result_hash.length + result_rangeKey.length + hashKeyBytes.length + rangeKeyBytes.length];
        System.arraycopy(result_hash, 0, result, 0, result_hash.length);
        System.arraycopy(hashKeyBytes, 0, result, result_hash.length, hashKeyBytes.length);
        System.arraycopy(result_rangeKey, 0, result, result_hash.length + hashKeyBytes.length, result_rangeKey.length);
        System.arraycopy(rangeKeyBytes, 0, result, result_hash.length + result_rangeKey.length + hashKeyBytes.length, rangeKeyBytes.length);
        return result;
    }

    public static int compareKeys(
            Comparator<byte[]> hashKeyComparator,
            Comparator<byte[]> rangeKeyComparator,
            byte[] k1,
            byte[] k2) {
        // First hash key
        int k1hashKeyBytesLength = Util.readVInt(k1);
        int pos = Util.writeVInt(k1hashKeyBytesLength).length;
        byte[] k1HashKeyBytes = new byte[k1hashKeyBytesLength];
        System.arraycopy(k1, pos, k1HashKeyBytes, 0, k1hashKeyBytesLength);

        // Second hash key
        int k2hashKeyBytesLength = Util.readVInt(k2);
        int pos2 = Util.writeVInt(k2hashKeyBytesLength).length;
        byte[] k2HashKeyBytes = new byte[k2hashKeyBytesLength];
        System.arraycopy(k2, pos2, k2HashKeyBytes, 0, k2hashKeyBytesLength);


        int hashComparison = hashKeyComparator.compare(k1HashKeyBytes, k2HashKeyBytes);

        if (rangeKeyComparator != null && hashComparison == 0) {
            // First range key
            pos += k1hashKeyBytesLength;
            int k1RangeKeyLength = Util.readVInt(k1, pos);
            pos += Util.writeVInt(k1RangeKeyLength).length ;
            byte[] k1RangeKeyBytes = new byte[k1RangeKeyLength];
            System.arraycopy(k1, pos, k1RangeKeyBytes, 0, k1RangeKeyLength);

            // Second range key
            pos2 += k2hashKeyBytesLength;
            int k2RangeKeyLength = Util.readVInt(k2, pos2);
            pos2 += Util.writeVInt(k2RangeKeyLength).length ;
            byte[] k2RangeKeyBytes = new byte[k2RangeKeyLength];
            System.arraycopy(k2, pos2, k2RangeKeyBytes, 0, k2RangeKeyLength);

            return rangeKeyComparator.compare(k1RangeKeyBytes, k2RangeKeyBytes);
        }

        return hashComparison;
    }
}
