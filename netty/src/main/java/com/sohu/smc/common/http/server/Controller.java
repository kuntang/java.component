// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Controller.java

package com.sohu.smc.common.http.server;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

import com.sohu.smc.common.http.server.*;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.handler.codec.http.*;
import org.jboss.netty.util.CharsetUtil;

// Referenced classes of package com.sohu.smc.common.http.server:
//			Action

public abstract class Controller
{

	public final String name;
	protected final Map actions = new HashMap();
    public String indexPageFile;


	public Controller(String name)
	{
		this.name = name;
	}

    public String getIndexPageFile() {
        return indexPageFile;
    }

    public void setIndexPageFile(String indexPageFile) {
        this.indexPageFile = indexPageFile;
    }

    public String getBasePath() {
        return HttpProtocolPageParser.getBasePath();
    }

    public void setBasePath(String basePath) {
        HttpProtocolPageParser.setBasePath(basePath);
    }

    public void printURL()
	{
        for (Iterator iterator = actions.keySet().iterator(); iterator.hasNext();
             System.out.println((new StringBuilder()).append("\t").append((String)iterator.next()).toString()));
	}

	public final byte[] doAction(HttpRequest request, HttpResponse response)throws Exception{

		String firstPath = getEndPoint(request, response);
        // 默认页面处理.
        if(firstPath == null || firstPath.trim().length()<1){
            response.setHeader("Content-Type", "text/html; charset=UTF-8");
            return FileUtil.getFileContent(HttpProtocolPageParser.getBasePath()+"/"+indexPageFile);
        }
        String uri = request.getUri().trim();
        if (uri.indexOf('?') > 0){
            uri = uri.substring(0, uri.indexOf('?'));
        }
        String uriSplits[] = uri.split("/");
        if(HttpProtocolPageParser.checkHtmlPage(uri) && !uri.contains("downloadImage")){
           return HttpProtocolPageParser.getPageBytes(uri,uriSplits,request,response);
        }

		if (uriSplits.length < 3)
		{
			invalidRequestFormat(request, response);
			return "invalid request".getBytes();
		}

		if (!name.equals(uriSplits[1]))
		{
			invalidRequestFormat(request, response);
			return "".getBytes();
		}

        // 读取图片
        String actionStr = uri;
        if(uriSplits.length>3 &&"downloadImage".equals(uriSplits[3])){
            actionStr = "/index/image/downloadImage";
        }

        // 下载excel
        if("downloadExcel".equals(uriSplits[2])){
            actionStr = "/index/downloadExcel/"+uriSplits[3];
        }


//        // 上传图片
//        if("uploadImage".equals(uriSplits[2])){
//            actionStr = "/image/uploadImage";
//        }
        Action action = (Action)actions.get(actionStr);

        if (action == null)
        {
            unknownAction(actionStr, response);
            return "invalid action..".getBytes();
        } else
        {
            return action.action(request, response,uriSplits);
        }
	}

	private String getEndPoint(HttpRequest request, HttpResponse response)
		throws Exception
	{
		String uri = request.getUri();
		if (uri.length() < 1)
		{
			invalidRequestFormat(request, response);
			return "";
		}

		int questionmark = uri.indexOf('?', 1);
		int slash = uri.indexOf('/', 1);
		int pos;
		if (questionmark > 0)
		{
			if (slash > 0)
				pos = questionmark >= slash ? slash : questionmark;
			else
				pos = questionmark;
		} else
		{
			pos = slash <= 0 ? uri.length() : slash;
		}
		return uri.substring(1, pos);
	}

	private final void invalidRequestFormat(HttpRequest request, HttpResponse response)
		throws IOException
	{
		response.setStatus(HttpResponseStatus.valueOf(400));
		response.setContent(ChannelBuffers.copiedBuffer((new StringBuilder()).append("Incorrectly formatted request: ").append(request.getUri()).toString(), CharsetUtil.UTF_8));
	}

	private final void unknownAction(String action, HttpResponse response)
		throws IOException
	{
		response.setStatus(HttpResponseStatus.valueOf(404));
		response.setContent(ChannelBuffers.copiedBuffer((new StringBuilder()).append("Unknown action: ").append(action).toString(), CharsetUtil.UTF_8));
	}
}
