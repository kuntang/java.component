package com.sohu.smc.common.leveldb.local.leveldb;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.iq80.leveldb.Options;
import com.sohu.smc.common.leveldb.local.Db;
import com.sohu.smc.common.leveldb.DbException;
import com.sohu.smc.common.leveldb.local.RangeTable;
import com.sohu.smc.common.leveldb.local.Table;
import com.sohu.smc.common.leveldb.comparator.LexicographicalComparator2;
import com.sohu.smc.common.leveldb.sede.ByteSerde;
import com.sohu.smc.common.leveldb.sede.Serde;

/**
 * An implementation of Db that uses LevelDb tables to persist data. Each
 * "table" is just a LevelDB database persisted as a subdirectory inside of
 * EzLevelDb's root.
 * 
 * @author criccomini
 * 
 */
public class LocalLevelDb implements Db {
  private final File root;
  private final Map<String, RangeTable<?, ?, ?>> cache;
  private final LocalLevelDbFactory factory;

  public LocalLevelDb(File root) {
    this(root, new LocalLevelDbJniFactory());
  }

  public LocalLevelDb(File root, LocalLevelDbFactory factory) {
    this.root = root;
    this.factory = factory;
    this.cache = new HashMap<String, RangeTable<?, ?, ?>>();
  }

  @Override
  public void deleteTable(String tableName) {
    try {
      synchronized (cache) {
        cache.remove(tableName);
        factory.destroy(getFile(tableName), new Options());
      }
    } catch (IOException e) {
      throw new DbException(e);
    }
  }

  @Override
  public <H, V> Table<H, V> getTable(String tableName, Serde<H> hashKeySerde, Serde<V> valueSerde) {
    return getTable(tableName, hashKeySerde, ByteSerde.get, valueSerde);
  }

  @Override
  public <H, R, V> RangeTable<H, R, V> getTable(String tableName, Serde<H> hashKeySerde, Serde<R> rangeKeySerde, Serde<V> valueSerde) {
    return getTable(tableName, hashKeySerde, rangeKeySerde, valueSerde, new LexicographicalComparator2(), new LexicographicalComparator2());
  }

  @SuppressWarnings("unchecked")
  @Override
  public <H, R, V> RangeTable<H, R, V> getTable(String tableName, Serde<H> hashKeySerde, Serde<R> rangeKeySerde, Serde<V> valueSerde, Comparator<byte[]> hashKeyComparator, Comparator<byte[]> rangeKeyComparator) {
    synchronized (cache) {
      RangeTable<?, ?, ?> table = cache.get(tableName);

      if (table == null) {
        table = new LocalLevelDbTable<H, R, V>(new File(root, tableName), factory, hashKeySerde, rangeKeySerde, valueSerde, hashKeyComparator, rangeKeyComparator);
        cache.put(tableName, table);
      }

      return (RangeTable<H, R, V>) table;
    }
  }

  /**
   * A helper method used to convert a table name to the location on disk where
   * this LevelDB database will be persisted.
   * 
   * @param tableName
   *          The logical name of the table.
   * @return The physical location of the directory where this table should be
   *         persisted.
   */
  private File getFile(String tableName) {
    return new File(root, tableName);
  }
}
