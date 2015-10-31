package com.sohu.smc;


import com.sohu.smc.common.http.server.Action;
import com.sohu.smc.common.http.server.ActionController;
import com.sohu.smc.common.http.server.HttpServer;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferIndexFinder;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.QueryStringDecoder;
import org.jboss.netty.handler.codec.http.multipart.*;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;


/**
 * Created with IntelliJ IDEA.
 * User: kuntang
 * Date: 14-4-4
 * Time: 下午2:38
 * To change this template use File | Settings | File Templates.
 */
public class CommentNettyHttpServer {
    private final static Logger logger = LoggerFactory.getLogger(CommentNettyHttpServer.class);
   private String host = null;
    public final static String IMAGE_BASE_PATH = "d:\\love_happiness\\image\\";
    public static void main(String[] args) throws Exception{
        System.out.println("hello world...");
        CommentNettyHttpServer server = new CommentNettyHttpServer("0.0.0.0");
        server.start();
    }

    public CommentNettyHttpServer(String host){
        this.host = host;
    }

    public void start() throws IOException {
        ActionController actionController = new ActionController("index");

        actionController.addAction("image/downloadImage",new ReadImageAction());
        // 启动netty服务器
        HttpServer httpServer = new HttpServer(host, 80, actionController, Executors.newCachedThreadPool());
        httpServer.start();
    }

    public class ReadImageAction extends Action {
        @Override
        protected byte[] action(HttpRequest req, HttpResponse resp,String[] uriSplits) throws Exception {
            int result = 502;
            try{
                // todo    这里有路径问题。需慎重考虑....
                String imagePath = IMAGE_BASE_PATH + uriSplits[4]+File.separator+uriSplits[5];
                File file = new File(imagePath);
                if(!file.exists()){
                    return (imagePath+"error...").getBytes();
                }
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


    public class New2Action extends Action {
        @Override
        protected byte[] action(HttpRequest req, HttpResponse resp,String[] uriSplits) throws Exception {
            int result = 502;
            return "netty ok".getBytes();
        }
    }


    public class UploadImageAction extends Action {
        @Override
        protected byte[] action(HttpRequest req, HttpResponse resp,String[] uriSplits) throws Exception {
            ChannelBuffer buffer = req.getContent();
//            byte[] values = buffer.array();
//            String abc = new String(values);
//            System.out.println("abc"+abc);
            test(req,resp);
            System.exit(1);

            int index = buffer.bytesBefore(ChannelBufferIndexFinder.CRLF);

//            HttpPostRequestDecoder dd =

            while(index>0){
                System.out.println("index="+index);
                buffer.skipBytes(index+1);
                index = buffer.bytesBefore(ChannelBufferIndexFinder.CRLF);
            }
            System.out.println("index="+index);

//            String searchStr = "....";
//            ByteSequenceIndexFinder finder = new ByteSequenceIndexFinder(searchStr.getBytes());
//            int startingOffset = finder.findIn(bufferToSearch);

            while (buffer.readableBytes() >= 5) {
                ChannelBuffer tempBuffer = buffer.readBytes(5);
//                System.out.println(tempBuffer.toString(Charset.defaultCharset()));
            }

            String uri = req.getUri();
            List<Map.Entry<String,String>> entrys =  req.getHeaders();
            for(Map.Entry<String,String> entry : entrys){
                String key = entry.getKey();
                String value = entry.getValue();
//                System.out.println(key+":"+value);
            }
            System.out.println(uri);
            if(buffer.capacity() <1){
                return "iamge is null".getBytes();
            }
            FileOutputStream fis = new FileOutputStream(new File("d:\\vvv.txt"));
            System.out.println("buffer capacity: "+buffer.capacity());
            buffer.getBytes(0, fis, buffer.capacity() - 1);
            fis.close();
            return "netty ok".getBytes();
        }
    }

    private static final HttpDataFactory factory =
            new DefaultHttpDataFactory(DefaultHttpDataFactory.MINSIZE);

    public void test(HttpRequest request, HttpResponse responseContent) throws HttpPostRequestDecoder.NotEnoughDataDecoderException {
        QueryStringDecoder decoderQuery = new QueryStringDecoder(request.getUri());
        Map<String, List<String>> uriAttributes = decoderQuery.getParameters();
        for (Map.Entry<String, List<String>> attr: uriAttributes.entrySet()) {
            for (String attrVal: attr.getValue()) {
//                responseContent.append("URI: " + attr.getKey() + '=' + attrVal + "\r\n");
            }
        }
            try {
                HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(factory, request);
                InterfaceHttpData data = decoder.getBodyHttpData("justinput2");
                InterfaceHttpData imageData = decoder.getBodyHttpData("test_image");
                InterfaceHttpData postData = decoder.getBodyHttpData("test_image"); // //
                // 读取从客户端传过来的参数
                String question = "";
                if (postData.getHttpDataType() == InterfaceHttpData.HttpDataType.Attribute) {
                    Attribute attribute = (Attribute) postData;
                    try {
                        question = attribute.getValue();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("q:" + question);

                }

                if (postData.getHttpDataType() == InterfaceHttpData.HttpDataType.FileUpload) {
                    FileUpload a1 = (FileUpload) postData;
                    try {
                        byte[] value = a1.get();
                        FileOutputStream fis = new FileOutputStream(new File("d:\\vvv.jpg"));
                        fis.write(value,0,value.length);
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("q:" + question);

                }

                return;
            } catch (HttpPostRequestDecoder.ErrorDataDecoderException e) {
                e.printStackTrace();
            } catch (HttpPostRequestDecoder.IncompatibleDataDecoderException e) {
                e.printStackTrace();
            }
    }



}
