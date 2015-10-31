package com.sohu.smc.common.http.server;

import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by tangkun.tk on 2015/1/1.
 */
public class HttpProtocolPageParser {

    // 网页相关的html,js,css文件基准路径.
    private  static String basePath;
    public static ConcurrentMap<String,byte[]>  fileMap = new ConcurrentHashMap<String, byte[]>();
    public static ConcurrentMap<String,Long>  fileModifyTimeMap = new ConcurrentHashMap<String, Long>();
    private static byte[] emptyByteArray = new byte[0];
    private static AtomicLong counter = new AtomicLong(0);

    private static final ScheduledExecutorService scheduler = Executors
            .newScheduledThreadPool(5);

    static {
        long delay = 20 * 1000L;
        scheduler.scheduleAtFixedRate(new FlushHtmlFileTask(), 0, delay, TimeUnit.MILLISECONDS);
    }

    public static class FlushHtmlFileTask implements Runnable{
        @Override
        public void run() {
            File file = new File(basePath);
            recursionHtmlFile("",file);
        }
    }

    public static void recursionHtmlFile(String path,File file){
        if(file == null){
            return;
        }
        String name = null;
        if(!path.contains("/")){
            name = "";
        }else{
            name = path+file.getName();
        }

        if(file.isDirectory()){
            File files[] = file.listFiles();
            for(File item : files){
                recursionHtmlFile(name + "/", item);
            }
        }else {
            System.out.println("name:  "+name);
            if(fileModifyTimeMap.containsKey(name)){
                File targetFile = new File(basePath+name);
                if(fileModifyTimeMap.get(name) != targetFile.lastModified()) {
                    byte[] value = new byte[0];
                    try {
                        value = FileUtil.getFileContent(targetFile);
                        if(value != null || value.length > 10){
                            fileMap.put(name,value);
                            fileModifyTimeMap.put(name,file.lastModified());
                            System.out.println("刷新文件"+name);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
            return;
        }
    }


    public static boolean checkHtmlPage(String uri){
        if(uri.endsWith(".html") || uri.endsWith(".css") ||uri.endsWith(".js")
                || uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith("gif") ||
                uri.endsWith(".otf") || uri.endsWith(".eot") || uri.endsWith(".svg") || uri.endsWith(".ttf") || uri.endsWith(".woff")){
            return true;
        }
        return false;
    }

    public static byte[]  getPageBytes(String uri,String[] uriSplits,HttpRequest request,HttpResponse response) throws IOException {
        if(uri.endsWith(".html") ){
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
            return getPageByteByName(uriSplits,request,response);
        }

        if( uri.endsWith(".css") ){
            response.setHeader("Content-Type", "text/css");
            return getPageByteByName(uriSplits,request,response);
        }

        if( uri.endsWith(".js") ){
            response.setHeader("Content-Type", "application/javascript");
            return getPageByteByName(uriSplits,request,response);
        }

        if( uri.endsWith(".jpg") ){
            response.setHeader("Content-Type", "image/jpeg");
            return getPageByteByName(uriSplits,request,response);
        }

        if( uri.endsWith(".png") || uri.endsWith("gif")){
            response.setHeader("Content-Type", "image/jpeg");
            return getPageByteByName(uriSplits,request,response);
        }

        if(uri.endsWith(".otf") || uri.endsWith(".eot") || uri.endsWith(".svg") || uri.endsWith(".ttf") || uri.endsWith(".woff")){
            response.setHeader("Content-Type","application/font-woff");
            return getPageByteByName(uriSplits,request,response);
        }

        return (uri+"page not exist.").getBytes();
    }


    private static byte[] getPageByteByName(String[] uriSplits,HttpRequest request,HttpResponse response) throws IOException {
        // 设置缓存时间和过期时间 3个月
        long expires = 3*30*24*60*60*1000L;
        response.setHeader("Cache-Control","private, max-age="+expires);
        long time = System.currentTimeMillis()+expires;
        response.setHeader("Expires",new Date(time));

        if(HttpServerHandler.checkLastModify(request, response)){
            return emptyByteArray;
        }

        String endStr = "";
        if(uriSplits.length>1){
            for(int i=1;i<uriSplits.length;i++){
                endStr += "/"+uriSplits[i];
            }
        }

        if(fileMap.containsKey(endStr)){
            System.out.println("获取缓存中的web文件"+endStr);
            return fileMap.get(endStr);
        }else{
//            File file = new File();
            File file = new File(basePath+endStr);
            System.out.println("获取磁盘中的web文件"+endStr);
            byte[] value = FileUtil.getFileContent(file);
            if(value != null || value.length > 10){
                fileMap.put(endStr,value);
                fileModifyTimeMap.put(endStr,file.lastModified());
            }
            return value;
        }
    }



    public static String getBasePath() {
        return basePath;
    }

    public static void setBasePath(String basePath) {
        HttpProtocolPageParser.basePath = basePath;
    }
}
