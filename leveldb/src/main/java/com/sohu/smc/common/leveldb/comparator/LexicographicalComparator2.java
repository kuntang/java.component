package com.sohu.smc.common.leveldb.comparator;

import java.nio.ByteBuffer;
import java.util.Comparator;

/**
 * A comparator that compares bytes using lexicographical ordering.
 * 
 * @author criccomini
 * 
 */
public class LexicographicalComparator2 implements Comparator<byte[]> {
  public static final LexicographicalComparator2 get = new LexicographicalComparator2();

  @Override
  public int compare(byte[] bytes1, byte[] bytes2) {
    return ByteBuffer.wrap(bytes1).compareTo(ByteBuffer.wrap(bytes2));
  }
}
