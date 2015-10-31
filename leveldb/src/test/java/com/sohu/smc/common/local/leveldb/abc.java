package com.sohu.smc.common.local.leveldb;
import com.sohu.smc.common.ipc.io.Writable;
import com.sohu.smc.common.leveldb.local.BinUtil;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import java.io.*;
import java.util.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;

/**
 * Created with IntelliJ IDEA.
 * User: tangkun
 * Date: 13-12-22
 * Time: 上午10:57
 * 本地leveldb操作类
 */
public class abc {


    private final static Options options = new Options();
    private static DB db;

    private static Comparator<? super Long> descSort = new Comparator<Long>() {
        @Override
        public int compare(Long o1, Long o2) {
            return o2 > o1 ? 1 : -1;
        }
    };

    public static void init(String levelDbPath) {
        options.createIfMissing(true);
        //不使用压缩
        options.compressionType(CompressionType.NONE);
        //缓存500m
        options.cacheSize(1024 * 1024 * 1000);
        options.writeBufferSize(1024 * 1024 * 50);

        try {
            // db = factory.open(new.newapi File("/opt/scm/leveldb/comment.db"), options);
//              db = factory.open(new File("D:\\leveldb\\new_comment.db"), options);
            db = factory.open(new File(levelDbPath), options);
        } catch (Exception e) {

            System.exit(0);
        }

}

    public static Integer getInt(String key) {
        byte[] bytes = db.get(bytes(key));
        if(bytes == null) {
            return null;
        }
        return BinUtil.getInt(bytes, 0);
    }

    public static void putLong(String key, long value) {
        byte[] bytes = new byte[8];
        BinUtil.putLong(bytes, value, 0);
        db.put(bytes(key), bytes);
    }

    /**
     *  查找某条评论,找到返回rid,找不到返回-1
     * @param eventId
     * @param rid
     * @return
     */
    public static long getLong(long eventId,long rid){
        byte[] keyBytes = new byte[16];
        BinUtil.putLong(keyBytes, eventId, 0);
        BinUtil.putLong(keyBytes,rid,8);
        byte[] bytes = db.get(keyBytes);
        return bytes!=null?BinUtil.getLong(bytes,0):-1;
    }


    public static long getLong(String key) {
        byte[] bytes = db.get(bytes(key));
        if(bytes == null) {
            return 0;
        }
        return BinUtil.getLong(bytes, 0);
    }

    public static void putString(String key, String value) {
        if(value != null) {
            db.put(bytes(key), bytes(value));
        }
    }

    public static String getString(String key) {
        byte[] bytes = db.get(bytes(key));
        if(bytes == null) {
            return null;
        }
        return asString(db.get(bytes(key)));
    }

    public static long[]  set2Array(Set<Long> list){
        int size = list.size();
        long[] ret = new long[size];
        int j=0;
        for(long rid : list){
            ret[j++] = rid;
        }
        return ret;
    }

    public static void putWritable(String key, Writable value) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        try {
            value.write(out);
        }catch (IOException e) {

        }finally {
            try {
                if(out != null) out.close();
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
        db.put(bytes(key), baos.toByteArray());
    }

    public static Writable getWritable(String key, Class c) {
        byte[] bytes = db.get(bytes(key));
        if(bytes == null) {
            return null;
        }
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        DataInputStream datain = new DataInputStream(in);

        String className = c.getSimpleName();
        Writable obj = null;

        try {
            if(className.equals("Comment")) {

            } else if(className.equals("EventStatis")) {

            } else if(className.equals("LiveComment")){

            }else if(className.equals("UserInfo")){
                obj = UserInfo.read(datain);
            }else{

            }
        }catch (Exception e) {

        }finally {
            try{
                if(datain != null) datain.close();
            } catch (IOException e) {

            }
        }

        return obj;
    }


    public static void deleteString(String key) {
        db.delete(bytes(key));
    }


    public static void main(String[] args) {
        abc.init("D:\\sohu_code\\level_db");
        putLong("abc",123);
        long abc  = getLong("abc");
        System.out.println(abc);

        UserInfo info = new UserInfo();
        info.setGender(1);
        info.setSignList("sasa");
        info.setHeadUrl("http://sucimg.itc.cn/avatarimg/wapUpload_1386366049886_c175");
        info.setNickName("玉良言AAA");
        info.setPid(5763088046270451715L);
        info.setUserId("47855ba5be3fe647a395cadfe54a2116@t.qq.sohu.com");
        String levelDbPath =null;
        if(System.getProperty("os.name").toLowerCase().contains("windows")){
//            levelDbPath = "D:\\sohu_code\\level_db";
        }else{
//            levelDbPath = "/opt/scm/leveldb/live_comment.db";
        }
        putWritable("kkey",info);
        UserInfo ai = (UserInfo)getWritable("kkey",UserInfo.class);
        System.out.println(ai.getHeadUrl());

        System.out.println();
    }

}
