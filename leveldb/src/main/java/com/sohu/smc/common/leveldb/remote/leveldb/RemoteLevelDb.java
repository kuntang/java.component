//package com.sohu.smc.common.leveldb.remote.leveldb;
//
//import com.sohu.smc.common.leveldb.DbException;
//import com.sohu.smc.common.leveldb.comparator.LexicographicalComparator2;
//import com.sohu.smc.common.leveldb.remote.Db;
//
//import com.sohu.smc.common.leveldb.remote.RangeTable;
//import com.sohu.smc.common.leveldb.remote.Table;
//import com.sohu.smc.common.leveldb.sede.ByteSerde;
//import com.sohu.smc.common.leveldb.sede.Serde;
//import com.sohu.smc.common.ssdb.client.pool.SsdbPooledConnectionFactory;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * An implementation of Db that uses LevelDb tables to persist data. Each
// * "table" is just a LevelDB database persisted as a subdirectory inside of
// * EzLevelDb's root.
// *
// * @author criccomini
// *
// */
//public class RemoteLevelDb implements Db {
//  private final Map<String, RangeTable<?, ?, ?>> cache;
//  private final SsdbPooledConnectionFactory pooledConnectionFactory;
//
//  public RemoteLevelDb(SsdbPooledConnectionFactory pooledConnectionFactory) {
//    this.pooledConnectionFactory = pooledConnectionFactory;
//    this.cache = new HashMap<String, RangeTable<?, ?, ?>>();
//  }
//
//  @Override
//  public void deleteTable(String tableName) {
//    try {
//      synchronized (cache) {
//        cache.remove(tableName);
//
//      }
//    } catch (Exception e) {
//      throw new DbException(e);
//    }
//  }
//
//  @Override
//  public <H, V> Table<H, V> getTable(String tableName, Serde<H> hashKeySerde, Serde<V> valueSerde) {
//    return getTable(tableName, hashKeySerde, ByteSerde.get, valueSerde);
//  }
//
//  @Override
//  public <H, R, V> RangeTable<H, R, V> getTable(String tableName, Serde<H> hashKeySerde, Serde<R> rangeKeySerde, Serde<V> valueSerde) {
//    return getTable(tableName, hashKeySerde, rangeKeySerde, valueSerde, new LexicographicalComparator2(), new LexicographicalComparator2());
//  }
//
//  @SuppressWarnings("unchecked")
//  @Override
//  public <H, R, V> RangeTable<H, R, V> getTable(String tableName, Serde<H> hashKeySerde, Serde<R> rangeKeySerde, Serde<V> valueSerde, Comparator<byte[]> hashKeyComparator, Comparator<byte[]> rangeKeyComparator) {
//    synchronized (cache) {
//      RangeTable<?, ?, ?> table = cache.get(tableName);
//
//      if (table == null) {
//        table = new RemoteLevelDbTable<H, R, V>(tableName, pooledConnectionFactory, hashKeySerde, rangeKeySerde, valueSerde);
//        cache.put(tableName, table);
//      }
//
//      return (RangeTable<H, R, V>) table;
//    }
//  }
//
//
//}
