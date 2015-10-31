// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   HttpPipelineFactory.java

package com.sohu.smc.common.http.server;

import java.util.concurrent.ExecutorService;

import com.sohu.smc.common.http.server.*;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;

// Referenced classes of package com.sohu.smc.common.http.server:
//			HttpServerHandler, Controller

public class HttpPipelineFactory
	implements ChannelPipelineFactory
{

	protected final com.sohu.smc.common.http.server.Controller controller;
	private final boolean isStream;
	private ExecutorService threadPool;

	public HttpPipelineFactory(com.sohu.smc.common.http.server.Controller controller, boolean isStream, ExecutorService threadPool)
	{
		this.controller = controller;
		this.isStream = isStream;
		this.threadPool = threadPool;
	}

	public ChannelPipeline getPipeline()
		throws Exception
	{
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder", new HttpRequestDecoder());
        // body发送的最大长度,1M改为16M
		pipeline.addLast("aggregator", new HttpChunkAggregator(0x1000000));
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("handler", new HttpServerHandler(controller, threadPool));
		return pipeline;
	}
}
