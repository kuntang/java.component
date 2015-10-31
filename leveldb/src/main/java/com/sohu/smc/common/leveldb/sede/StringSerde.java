package com.sohu.smc.common.leveldb.sede;

import java.io.UnsupportedEncodingException;
import com.sohu.smc.common.leveldb.DbException;

public class StringSerde implements Serde<String> {
  public static final StringSerde get = new StringSerde();

  @Override
  public String fromBytes(byte[] bytes) {
    try {
      return new String(bytes, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new DbException(e);
    }
  }

  @Override
  public byte[] toBytes(String obj) {
      if(obj == null) return "".getBytes();
    try {
      return obj.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new DbException(e);
    }
  }
}
