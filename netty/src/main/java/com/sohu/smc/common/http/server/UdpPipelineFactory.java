// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UdpPipelineFactory.java

package com.sohu.smc.common.http.server;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;

// Referenced classes of package com.sohu.smc.common.http.server:
//			Controller

public class UdpPipelineFactory
	implements ChannelPipelineFactory
{

	protected final com.sohu.smc.common.http.server.Controller controller;
	private final boolean isStream;

	public UdpPipelineFactory(com.sohu.smc.common.http.server.Controller controller, boolean isStream)
	{
		this.controller = controller;
		this.isStream = isStream;
	}

	public ChannelPipeline getPipeline()
		throws Exception
	{
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder", new StringDecoder());
		pipeline.addLast("encoder", new StringEncoder());
		return pipeline;
	}
}
