package com.sohu.smc.common.leveldb.local;

import java.nio.ByteBuffer;
import java.util.Map.Entry;
import com.sohu.smc.common.leveldb.sede.Serde;
import com.sohu.smc.common.leveldb.util.Util;

public class RawTableRow<H, R, V> implements TableRow<H, R, V> {
    private final H hashKey;
    private final R rangeKey;
    private final V value;

    public RawTableRow(H hashKey, R rangeKey, V value) {
        this.hashKey = hashKey;
        this.rangeKey = rangeKey;
        this.value = value;
    }

    public RawTableRow(
            Entry<byte[], byte[]> rawRow,
            Serde<H> hashKeySerde,
            Serde<R> rangeKeySerde,
            Serde<V> valueSerde) {
        // TODO could make sede lazy for a bit of extra speed
        byte[] compoundKeyBytes = rawRow.getKey();

        int hashKeyBytesLength = Util.readVInt(compoundKeyBytes);
        int pos = Util.writeVInt(hashKeyBytesLength).length;
        byte[] hashKeyBytes = new byte[hashKeyBytesLength];
        System.arraycopy(compoundKeyBytes, pos, hashKeyBytes, 0, hashKeyBytesLength);

        pos = pos + hashKeyBytesLength;
        int rangeKeyBytesLength = Util.readVInt(compoundKeyBytes, pos);

        if (rangeKeyBytesLength > 0) {
            byte[] rangeKeyBytes = new byte[rangeKeyBytesLength];
            pos += Util.writeVInt(rangeKeyBytesLength).length;
            System.arraycopy(compoundKeyBytes, pos, rangeKeyBytes, 0, rangeKeyBytesLength);
            rangeKey = rangeKeySerde.fromBytes(rangeKeyBytes);
        } else {
            rangeKey = null;
        }

        hashKey = hashKeySerde.fromBytes(hashKeyBytes);
        value = valueSerde.fromBytes(rawRow.getValue());
    }

    @Override
    public H getHashKey() {
        return hashKey;
    }

    @Override
    public R getRangeKey() {
        return rangeKey;
    }

    @Override
    public V getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((hashKey == null) ? 0 : hashKey.hashCode());
        result = prime * result + ((rangeKey == null) ? 0 : rangeKey.hashCode());
        result = prime * result + ((value == null) ? 0 : value.hashCode());
        return result;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RawTableRow other = (RawTableRow) obj;
        if (hashKey == null) {
            if (other.hashKey != null)
                return false;
        } else if (!hashKey.equals(other.hashKey))
            return false;
        if (rangeKey == null) {
            if (other.rangeKey != null)
                return false;
        } else if (!rangeKey.equals(other.rangeKey))
            return false;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "LevelDbHashRangeTableRow [hashKey=" + hashKey + ", rangeKey=" + rangeKey + ", value=" + value + "]";
    }
}
