package com.sohu.smc.common.http.server;

import java.io.*;

/**
 * Created by tangkun.tk on 2014/12/31.
 */
public class FileUtil {

    public static byte[] getFileContent(File file) throws IOException {
        if(!file.exists()){
            return new byte[0];
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream((int)file.length());
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        byte[] values = bos.toByteArray();
        return values;


    }

    public static byte[] getFileContent(String filePath) throws IOException {
        File file = new File(filePath);
        return getFileContent(file);
    }



}
