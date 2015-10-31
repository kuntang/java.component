//package com.sohu.smc.common.local.leveldb;
//
//import com.sohu.smc.common.leveldb.comparator.LexicographicalComparator2;
//import com.sohu.smc.common.leveldb.remote.*;
//import com.sohu.smc.common.leveldb.remote.leveldb.RemoteLevelDb;
//import com.sohu.smc.common.leveldb.sede.IntegerSerde;
//import com.sohu.smc.common.leveldb.sede.StringSerde;
//import com.sohu.smc.common.leveldb.sede.VersionedSerde;
//import com.sohu.smc.common.leveldb.sede.VersionedSerde.Versioned;
//
//import com.sohu.smc.common.ssdb.client.pool.SsdbPooledConnectionFactory;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.nio.ByteBuffer;
//import java.util.Comparator;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public class TestRemoteLevelDb {
//    protected Db ezdb;
//    protected RangeTable<Integer, Integer, Integer> table;
//
//    @Test
//    public void testNulls() {
//        // test nulls
//        assertEquals(null, table.get(1));
//        assertEquals(null, table.get(1, 1));
//        assertTrue(!table.range(1, 0).hasNext());
//        assertTrue(!table.range(1, 2, 0).hasNext());
//        assertTrue(!table.range(1, 1, 2, 0).hasNext());
//        table.delete(1);
//        table.delete(1, 1);
//    }
//
//    @Test
//    public void testPutGetH() {
//        Table<Integer, Integer> table = ezdb.getTable("test-simple", IntegerSerde.get, IntegerSerde.get);
//        table.put(1, 1);
//        assertEquals(new Integer(1), table.get(1));
//        table.put(1, 2);
//        assertEquals(new Integer(2), table.get(1));
//        table.close();
//    }
//
//    @Test
//    public void testPutGetHR() {
//        table.put(1, 1);
//        table.put(1, 1, 3);
//        assertEquals(new Integer(1), table.get(1));
//        assertEquals(new Integer(3), table.get(1, 1));
//        table.put(1, 1, 4);
//        assertEquals(new Integer(4), table.get(1, 1));
//    }
//
//    @Test
//    public void testRangeH() {
//        TableIterator<Integer, Integer, Integer> it = table.range(1, 0);
//        table.put(1, 2);
//        table.put(1, 1, 4);
//        table.put(2, 1, 4);
//        it.close();
//        it = table.range(1, 0);
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, null, 2), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 1, 4), it.next());
//        assertTrue(!it.hasNext());
//        it.close();
//    }
//
//    @Test
//    public void testRangeHR() {
//        table.put(1, 2);
//        table.put(1, 1, 4);
//        TableIterator<Integer, Integer, Integer> it = table.range(1, null, 0);
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, null, 2), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 1, 4), it.next());
//        assertTrue(!it.hasNext());
//        it.close();
//        it = table.range(1, 1);
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 1, 4), it.next());
//        assertTrue(!it.hasNext());
//        table.put(1, 2, 5);
//        table.put(2, 2, 5);
//        it.close();
//        it = table.range(1, 1);
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 1, 4), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 2, 5), it.next());
//        assertTrue(!it.hasNext());
//        it.close();
//        it = table.range(1, null, 0);
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, null, 2), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 1, 4), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 2, 5), it.next());
//        assertTrue(!it.hasNext());
//        it.close();
//    }
//
//    @Test
//    public void testRangeHRR() {
//        table.put(1, 2);
//        table.put(1, 1, 4);
//        TableIterator<Integer, Integer, Integer> it = table.range(1, null, 2);
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, null, 2), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 1, 4), it.next());
//        assertTrue(!it.hasNext());
//        it.close();
//        it = table.range(1, 1, 2);
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 1, 4), it.next());
//        assertTrue(!it.hasNext());
//        table.put(1, 2, 5);
//        table.put(1, 3, 5);
//        it.close();
//        it = table.range(1, 1, 3);
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 1, 4), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 2, 5), it.next());
//        assertTrue(!it.hasNext());
//        it.close();
//    }
//
//    @Test
//    public void testDeleteH() {
//        table.put(1, 1);
//        assertEquals(new Integer(1), table.get(1));
//        table.delete(1);
//        assertEquals(null, table.get(1));
//    }
//
//    @Test
//    public void testDeleteHR() {
//        table.put(1, 1);
//        table.put(1, 1, 2);
//        assertEquals(new Integer(1), table.get(1));
//        assertEquals(new Integer(2), table.get(1, 1));
//        table.delete(1, 1);
//        assertEquals(new Integer(1), table.get(1));
//        assertEquals(null, table.get(1, 1));
//    }
//
//    @Test
//    public void testSortedStrings() {
//        ezdb.deleteTable("test-range-strings");
//        RangeTable<Integer, String, Integer> table = ezdb.getTable("test-range-strings", IntegerSerde.get, StringSerde.get, IntegerSerde.get);
//
//        table.put(1213, "20120102-foo", 1);
//        table.put(1213, "20120102-bar", 2);
//        table.put(1213, "20120101-foo", 3);
//        table.put(1213, "20120104-foo", 4);
//        table.put(1213, "20120103-foo", 5);
//        table.put(1212, "20120102-foo", 1);
//        table.put(1214, "20120102-bar", 2);
//        table.put(1213, 12345678);
//
//        TableIterator<Integer, String, Integer> it = table.range(1213, "20120102-bar", "20120103", 10);
//
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, String, Integer>(1213, "20120102-bar", 2), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, String, Integer>(1213, "20120102-foo", 1), it.next());
//        assertTrue(!it.hasNext());
//        it.close();
//        assertEquals(new Integer(12345678), table.get(1213));
//        table.close();
//    }
//
//    @Test
//    public void testCustomRangeComparator() {
//        RangeTable<Integer, Integer, Integer> table = ezdb.getTable("test-custom-range-comparator", IntegerSerde.get, IntegerSerde.get, IntegerSerde.get, new LexicographicalComparator2(), new Comparator<byte[]>() {
//            // Let's do things in reverse lexicographical order.
//            @Override
//            public int compare(byte[] o1, byte[] o2) {
//                return -1 * ByteBuffer.wrap(o1).compareTo(ByteBuffer.wrap(o2));
//            }
//        });
//
//        table.put(1, 1, 1);
//        table.put(1, 2, 2);
//        table.put(1, 3, 3);
//
//        TableIterator<Integer, Integer, Integer> it = table.range(1, 3);
//
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 3, 3), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 2, 2), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, Integer, Integer>(1, 1, 1), it.next());
//        assertTrue(!it.hasNext());
//        it.close();
//        table.close();
//    }
//
//    @Test
//    public void testVersionedSortedStrings() {
//        ezdb.deleteTable("test-range-strings");
//        RangeTable<Integer, String, Versioned<Integer>> table = ezdb.getTable("test-range-strings", IntegerSerde.get, StringSerde.get, new VersionedSerde<Integer>(IntegerSerde.get));
//
//        table.put(1213, "20120102-foo", new Versioned<Integer>(1, 0));
//        table.put(1213, "20120102-bar", new Versioned<Integer>(2, 0));
//        table.put(1213, "20120102-bar", new Versioned<Integer>(3, 1));
//        table.put(1213, "20120101-foo", new Versioned<Integer>(3, 0));
//        table.put(1213, "20120104-foo", new Versioned<Integer>(4, 0));
//        table.put(1213, "20120103-foo", new Versioned<Integer>(5, 0));
//        table.put(1212, "20120102-foo", new Versioned<Integer>(1, 0));
//        table.put(1214, "20120102-bar", new Versioned<Integer>(2, 0));
//        table.put(1213, new Versioned<Integer>(12345678, 0));
//
//        assertEquals(new Versioned<Integer>(1, 0), table.get(1213, "20120102-foo"));
//        assertEquals(new Versioned<Integer>(3, 1), table.get(1213, "20120102-bar"));
//        assertEquals(new Versioned<Integer>(12345678, 0), table.get(1213));
//
//        TableIterator<Integer, String, Versioned<Integer>> it = table.range(1213, "20120102", "20120103", 10);
//
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, String, Versioned<Integer>>(1213, "20120102-bar", new Versioned<Integer>(3, 1)), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, String, Versioned<Integer>>(1213, "20120102-foo", new Versioned<Integer>(1, 0)), it.next());
//        assertTrue(!it.hasNext());
//        it.close();
//        assertEquals(new Versioned<Integer>(12345678, 0), table.get(1213));
//
//        // check how things work when iterating between null/versioned range keys
//        it = table.range(1213, 10);
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, String, Versioned<Integer>>(1213, "20120101-foo", new Versioned<Integer>(3, 0)), it.next());
//        assertTrue(it.hasNext());
//        assertEquals(new RawTableRow<Integer, String, Versioned<Integer>>(1213, "20120102-bar", new Versioned<Integer>(3, 1)), it.next());
//        // trust that everything works from here on out
//        while (it.hasNext()) {
//            assertEquals(new Integer(1213), it.next().getHashKey());
//        }
//        it.close();
//        table.close();
//    }
//
//    @Before
//    public void before() {
//        final SsdbPooledConnectionFactory socketChannelPooledConnectionFactory = new SsdbPooledConnectionFactory("10.13.81.96",8888, 10, 50);
//
//        ezdb = new RemoteLevelDb(socketChannelPooledConnectionFactory);
//        ezdb.deleteTable("test");
//        table = ezdb.getTable("test", IntegerSerde.get, IntegerSerde.get, IntegerSerde.get);
//    }
//
//    /** Writes an int in a variable-length format.  Writes between one and
//     * five bytes.  Smaller values take fewer bytes.  Negative numbers are not
//     * supported.
//     *
//     */
//    public final byte[] writeVInt(int i){
//        int n = i;
//        int len = 0;
//        while ((n & ~0x7F) != 0) {
//            len++;
//            n >>>= 7;
//        }
//
//        byte[] ret = new byte[len + 1];
//        n = 0;
//        while ((i & ~0x7F) != 0) {
//            ret[n++] = (byte)((i & 0x7f) | 0x80);
//            i >>>= 7;
//        }
//        ret[n++] = (byte)i;
//
//        return ret;
//    }
//
//    /** Reads an int stored in variable-length format.  Reads between one and
//     * five bytes.  Smaller values take fewer bytes.  Negative numbers are not
//     * supported.
//     */
//    public final int readVInt(byte[] bytes) {
//        byte b = bytes[0];
//        int n = 1;
//        int i = b & 0x7F;
//        for (int shift = 7; (b & 0x80) != 0; shift += 7) {
//            b = bytes[n++];
//            i |= (b & 0x7F) << shift;
//        }
//        return i;
//    }
//
//    @Test
//    public void testVint(){
//        int i = 1234555;
//        byte[] b = writeVInt(i);
//        assertEquals(i, readVInt(b));
//    }
//    @After
//    public void after() {
//        table.close();
//        ezdb.deleteTable("test");
//        ezdb.deleteTable("test-simple");
//        ezdb.deleteTable("test-range-strings");
//        ezdb.deleteTable("test-custom-range-comparator");
//        ezdb.deleteTable("test-table-does-not-exist");
//    }
//}
