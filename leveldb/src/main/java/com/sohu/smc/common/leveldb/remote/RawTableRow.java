package com.sohu.smc.common.leveldb.remote;

import com.sohu.smc.common.leveldb.sede.Serde;

import java.nio.ByteBuffer;
import java.util.Map.Entry;

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
            H hashKey,
            Serde<R> rangeKeySerde,
            Serde<V> valueSerde) {
        // TODO could make sede lazy for a bit of extra speed
        byte[] compoundKeyBytes = rawRow.getKey();
        rangeKey = rangeKeySerde.fromBytes(compoundKeyBytes);

        this.hashKey = hashKey;
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
