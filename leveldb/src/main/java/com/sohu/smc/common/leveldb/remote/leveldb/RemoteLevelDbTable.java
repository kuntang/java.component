//package com.sohu.smc.common.leveldb.remote.leveldb;
//
//import com.sohu.smc.common.leveldb.remote.*;
//import com.sohu.smc.common.leveldb.sede.*;
//import com.sohu.smc.common.leveldb.util.Util;
//import com.sohu.smc.common.ssdb.client.SsdbConnectionImpl;
//import com.sohu.smc.common.ssdb.client.cmd.Response;
//import com.sohu.smc.common.ssdb.client.pool.SsdbPooledConnectionFactory;
//
//import java.util.*;
//import java.util.Map.Entry;
//
//public class RemoteLevelDbTable<H, R, V> implements RangeTable<H, R, V> {
//    private final SsdbPooledConnectionFactory pooledConnectionFactory;
//    private final Serde<H> hashKeySerde;
//    private final Serde<R> rangeKeySerde;
//    private final Serde<V> valueSerde;
//    private final byte[] tableName;
//
//    public RemoteLevelDbTable(String tableName,SsdbPooledConnectionFactory pooledConnectionFactory, Serde<H> hashKeySerde, Serde<R> rangeKeySerde,
//                              Serde<V> valueSerde) {
//        this.hashKeySerde = hashKeySerde;
//        this.rangeKeySerde = rangeKeySerde;
//        this.valueSerde = valueSerde;
//        this.tableName = tableName.getBytes();
//
//        this.pooledConnectionFactory = pooledConnectionFactory;
//
//    }
//
//    @Override
//    public void put(H hashKey, V value) {
//        SsdbConnectionImpl conn = (SsdbConnectionImpl)pooledConnectionFactory.getConnection();
//        conn.set(Util.combine(tableName, hashKeySerde.toBytes(hashKey)), valueSerde.toBytes(value));
//        pooledConnectionFactory.returnConnection(conn);
//    }
//
//    @Override
//    public void put(H hashKey, R rangeKey, V value) {
//        SsdbConnectionImpl conn = (SsdbConnectionImpl)pooledConnectionFactory.getConnection();
//        conn.hset(Util.combine(tableName, hashKeySerde.toBytes(hashKey)), rangeKeySerde.toBytes(rangeKey), valueSerde.toBytes(value));
//        pooledConnectionFactory.returnConnection(conn);
//    }
//
//    @Override
//    public V get(H hashKey) {
//        SsdbConnectionImpl conn = (SsdbConnectionImpl)pooledConnectionFactory.getConnection();
//        byte[] valueBytes = conn.get(Util.combine(tableName, hashKeySerde.toBytes(hashKey)));
//        pooledConnectionFactory.returnConnection(conn);
//
//        if (valueBytes == null) {
//            return null;
//        }
//
//        return valueSerde.fromBytes(valueBytes);
//    }
//
//    @Override
//    public V get(H hashKey, R rangeKey) {
//        SsdbConnectionImpl conn = (SsdbConnectionImpl)pooledConnectionFactory.getConnection();
//        byte[] valueBytes = conn.hget(Util.combine(tableName, hashKeySerde.toBytes(hashKey)), rangeKeySerde.toBytes(rangeKey));
//        pooledConnectionFactory.returnConnection(conn);
//
//        if (valueBytes == null) {
//            return null;
//        }
//
//        return valueSerde.fromBytes(valueBytes);
//    }
//
//    @Override
//    public TableIterator<H, R, V> range(H hashKey, int limit) {
//        return range(hashKey, null, null, limit);
//    }
//
//    @Override
//    public TableIterator<H, R, V> range(H hashKey, R fromRangeKey, int limit) {
//        return range(hashKey, fromRangeKey, null, limit);
//    }
//
//    @Override
//    public TableIterator<H, R, V> range(final H hashKey, R fromRangeKey, R toRangeKey, int limit) {
//        SsdbConnectionImpl conn = (SsdbConnectionImpl)pooledConnectionFactory.getConnection();
//        final Response response = conn.range("hscan", Util.combine(tableName, hashKeySerde.toBytes(hashKey)), rangeKeySerde.toBytes(fromRangeKey), rangeKeySerde.toBytes(toRangeKey), limit);
//        pooledConnectionFactory.returnConnection(conn);
//
//        final List<byte[]> raw_resp = response.getRaw();
//        int count = raw_resp.size();
//        List<Entry<byte[], byte[]>> entryList = new ArrayList<Entry<byte[], byte[]>>(count >> 2);
//
//        for (int i = 1; i < count; i += 2) {
//            entryList.add(new AbstractMap.SimpleImmutableEntry<byte[],byte[]>(raw_resp.get(i), raw_resp.get(i + 1)));
//        }
//        final Iterator iterator = entryList.iterator();
//
//        return new AutoClosingTableIterator<H, R, V>(new TableIterator<H, R, V>() {
//            @Override
//            public boolean hasNext() {
//                return iterator.hasNext();
//            }
//
//            @Override
//            public TableRow<H, R, V> next() {
//                return new RawTableRow<H, R, V>((Entry<byte[], byte[]>)iterator.next(), hashKey, rangeKeySerde, valueSerde);
//            }
//
//            @Override
//            public void remove() {
//                //iterator.remove();
//            }
//
//            @Override
//            public void close() {
//
//            }
//        });
//    }
//
//    public TableIterator<H, R, V> rangeReverse(final H hashKey, int limit) {
//        return rangeReverse(hashKey, null, null, limit);
//    }
//
//    public TableIterator<H, R, V> rangeReverse(final H hashKey, final R fromRangeKey, int limit) {
//        return rangeReverse(hashKey, fromRangeKey, null, limit);
//    }
//
//    public TableIterator<H, R, V> rangeReverse(final H hashKey, final R fromRangeKey, final R toRangeKey, int limit) {
//        SsdbConnectionImpl conn = (SsdbConnectionImpl)pooledConnectionFactory.getConnection();
//        final Response response = conn.range("hrscan", Util.combine(tableName, hashKeySerde.toBytes(hashKey)), rangeKeySerde.toBytes(fromRangeKey), rangeKeySerde.toBytes(toRangeKey), limit);
//        pooledConnectionFactory.returnConnection(conn);
//
//        final List<byte[]> raw_resp = response.getRaw();
//        int count = raw_resp.size();
//        List<Entry<byte[], byte[]>> entryList = new ArrayList<Entry<byte[], byte[]>>(count >> 2);
//
//        for (int i = 1; i < count; i += 2) {
//            entryList.add(new AbstractMap.SimpleImmutableEntry<byte[],byte[]>(raw_resp.get(i), raw_resp.get(i + 1)));
//        }
//        final Iterator iterator = entryList.iterator();
//
//        return new AutoClosingTableIterator<H, R, V>(new TableIterator<H, R, V>() {
//            @Override
//            public boolean hasNext() {
//                return iterator.hasNext();
//            }
//
//            @Override
//            public TableRow<H, R, V> next() {
//                return new RawTableRow<H, R, V>((Entry<byte[], byte[]>)iterator.next(), hashKey, rangeKeySerde, valueSerde);
//            }
//
//            @Override
//            public void remove() {
//                //iterator.remove();
//            }
//
//            @Override
//            public void close() {
//
//            }
//        });
//    }
//
//    @Override
//    public void delete(H hashKey) {
//        delete(hashKey, null);
//    }
//
//    @Override
//    public void delete(H hashKey, R rangeKey) {
//        SsdbConnectionImpl conn = (SsdbConnectionImpl)pooledConnectionFactory.getConnection();
//        conn.hdel(Util.combine(tableName, hashKeySerde.toBytes(hashKey)), rangeKeySerde.toBytes(rangeKey));
//        pooledConnectionFactory.returnConnection(conn);
//
//    }
//
//    @Override
//    public void close() {
//
//    }
//
//    private static class AutoClosingTableIterator<_H, _R, _V> implements TableIterator<_H, _R, _V> {
//
//        private final TableIterator<_H, _R, _V> delegate;
//        private boolean closed;
//
//        public AutoClosingTableIterator(final TableIterator<_H, _R, _V> delegate) {
//            this.delegate = delegate;
//        }
//
//        @Override
//        public boolean hasNext() {
//            if(closed){
//                return false;
//            }
//            final boolean hasNext = delegate.hasNext();
//            if (!hasNext) {
//                close();
//            }
//            return hasNext;
//        }
//
//        @Override
//        public TableRow<_H, _R, _V> next() {
//            if (closed) {
//                throw new NoSuchElementException();
//            }
//            return delegate.next();
//        }
//
//        @Override
//        public void remove() {
//            delegate.remove();
//        }
//
//        @Override
//        protected void finalize() throws Throwable {
//            super.finalize();
//            close();
//        }
//
//        @Override
//        public void close() {
//            if(!closed){
//                closed = true;
//                delegate.close();
//            }
//        }
//
//    }
//}
