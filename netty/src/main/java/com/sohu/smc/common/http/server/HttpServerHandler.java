// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space
// Source File Name:   HttpServerHandler.java

package com.sohu.smc.common.http.server;

import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Date;
import java.util.concurrent.Executor;

import com.sohu.smc.common.http.server.*;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

// Referenced classes of package com.sohu.smc.common.http.server:
//			Controller

public class HttpServerHandler extends SimpleChannelUpstreamHandler
{
    private static final Date last_modify = new Date(System.currentTimeMillis());
    class HandlerRunnable
            implements Runnable
    {
        // 最后修改时间


        private MessageEvent e;
        HttpRequest request;
        com.sohu.smc.common.http.server.Controller controller;
        private Executor threadPool;


        public void run()
        {
            try
            {
                boolean keepAlive = HttpHeaders.isKeepAlive(request);
                HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
                byte[] ret = controller.doAction(request, response);
                //response.setContent(ChannelBuffers.copiedBuffer(ret, CharsetUtil.getEncoder(Charset.forName("UTF-8")).charset()));
                response.setContent(ChannelBuffers.copiedBuffer(ret));
                if( !response.containsHeader("Content-Type") ){
                    response.setHeader("Content-Type", "text/plain; charset=UTF-8");
                }
                response.setHeader("Connection","keep-alive");
                response.setHeader("Date",new Date());
                response.setHeader("Last-Modified",last_modify);
                if (keepAlive)
                    response.setHeader("Content-Length", Integer.valueOf(response.getContent().readableBytes()));
                ChannelFuture future = this.e.getChannel().write(response);
                if (!keepAlive)
                    future.addListener(ChannelFutureListener.CLOSE);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        public HandlerRunnable(MessageEvent e, HttpRequest request, com.sohu.smc.common.http.server.Controller controller)
        {

            super();
            this.e = e;
            this.request = request;
            this.controller = controller;

        }
    }


    protected final com.sohu.smc.common.http.server.Controller controller;
    protected final String CONTENT_TYPE_V = "text/plain; charset=GBK";
    private final Executor executor;

    public HttpServerHandler(com.sohu.smc.common.http.server.Controller controller, Executor executor)
    {
        this.controller = controller;
        this.executor = executor;
    }

    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent ev)
    {
        ev.getCause().printStackTrace();
        try
        {
            ev.getChannel().close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e)
            throws Exception
    {
        HttpRequest request = (HttpRequest)e.getMessage();
        if (request == null)
            return;
        try
        {
            executor.execute(new HandlerRunnable(e, request, controller));
        }
        catch (Exception expt)
        {
            expt.printStackTrace();
        }
    }


    // 304 处理
    public static final boolean checkLastModify(HttpRequest request,HttpResponse response){
        if( !request.containsHeader("If-Modified-Since") ){
            return false;
        }
        String ifModifiedSince = request.getHeader("If-Modified-Since");
        Date date = new Date(ifModifiedSince);
        if(date.getTime()<last_modify.getTime()){
            return false;       // 需要请求新的资源来替代老的资源
        }else{
            response.setStatus(new HttpResponseStatus(304,"Not Modified"));     // 直接返回
            return true;
        }

    }

}
