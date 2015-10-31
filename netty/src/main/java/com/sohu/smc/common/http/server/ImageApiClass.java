package com.sohu.smc.common.http.server;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.multipart.DefaultHttpDataFactory;
import org.jboss.netty.handler.codec.http.multipart.HttpDataFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by tangkun.tk on 2015/1/2.
 * 图片服务器
 */
public class ImageApiClass {

    private final static Logger logger = LoggerFactory.getLogger(ImageApiClass.class);
    private static final HttpDataFactory factory =
            new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);
    private final static String imageCategory = "image";
    private final static String downloadImage = "downloadImage";
    public final static String IMAGE_BASE_PATH = "d:\\love_happiness\\image\\";

    public static class ReadImageAction extends Action {
        @Override
        protected byte[] action(HttpRequest req, HttpResponse resp,String[] uriSplits) throws Exception {
            int result = 502;
            if(uriSplits == null || uriSplits.length<3){
                System.out.println("error.......");

            }
            try{
                String imagePath = IMAGE_BASE_PATH + uriSplits[3]+ File.separator+uriSplits[4];
                System.out.println("-----------------------");
                File file = new File(imagePath);
                if(!file.exists()){
                    return (imagePath+"error...").getBytes();
                }
                System.out.println("------------------------");
                ByteArrayOutputStream bos = new ByteArrayOutputStream((int)file.length());
                FileInputStream fis = new FileInputStream(file);
                byte[] b = new byte[1000];
                int n;
                while ((n = fis.read(b)) != -1) {
                    bos.write(b, 0, n);
                }
                fis.close();
                bos.close();
                byte[] values = bos.toByteArray();
                ChannelBuffer buffer  = ChannelBuffers.wrappedBuffer(values);
                resp.setContent(buffer);
                resp.setChunked(false);
                return values;
            }catch (Exception e){
                logger.error("new action error: ",e);
                e.printStackTrace();
            }
            return null;
        }
    }


    private static class  ImagePath{
        /*绝对路径*/
        private String apath;
        /*相对路径*/
        private String bpath;

        public ImagePath(String apath,String bpath){
            this.apath = apath;
            this.bpath = bpath;
        }
        public String getApath() {
            return apath;
        }
        public void setApath(String apath) {
            this.apath = apath;
        }
        public String getBpath() {
            return bpath;
        }
        public void setBpath(String bpath) {
            this.bpath = bpath;
        }
    }

//
//    public static String genImageUrl(String bpath){
////        return "http://"+Constant.IP+Constant.IMAGE_SERVER_PORT+File.separator+"index/"+imageCategory+File.separator+downloadImage+File.separator+bpath;
//        return "http://"+Constant.IP+Constant.IMAGE_SERVER_PORT+"/index/"+imageCategory+"/"+downloadImage+"/"+bpath;
//
//    }

}
