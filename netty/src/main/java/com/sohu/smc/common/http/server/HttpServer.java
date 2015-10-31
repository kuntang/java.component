// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   HttpServer.java

package com.sohu.smc.common.http.server;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sohu.smc.common.http.server.*;
import com.sohu.smc.common.http.server.HttpPipelineFactory;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

// Referenced classes of package com.sohu.smc.common.http.server:
//			Server, HttpPipelineFactory, Controller

public class HttpServer extends Server
{

	protected final InetSocketAddress addr;
	protected final ServerBootstrap bootstrap;
	protected final NioServerSocketChannelFactory factory;
	protected Channel ch;
	private com.sohu.smc.common.http.server.Controller controller;
	public static final String NETTY_TCP_sendBufferSize_OPTION = "sendBufferSize";
	public static final int DEFAULT_TCP_sendBufferSize_VALUE = 0x10000;

	public HttpServer(String host, int port, com.sohu.smc.common.http.server.Controller controller, ExecutorService threadPool)
	{
		this(new InetSocketAddress(host, port), controller, threadPool);
	}

	public HttpServer(InetSocketAddress addr, com.sohu.smc.common.http.server.Controller controller, ExecutorService threadPool)
	{
		this.addr = addr;
		factory = new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
		this.controller = controller;
		bootstrap = new ServerBootstrap(factory);
		bootstrap.setOption("reuseAddress", Boolean.valueOf(true));
		bootstrap.setOption("child.tcpNoDelay", Boolean.valueOf(true));
		bootstrap.setOption("child.keepAlive", Boolean.valueOf(true));
//		bootstrap.setOption("child.sendBufferSize", Integer.valueOf(0x10000));
//		bootstrap.setOption("child.sendBufferSize", -1);
		bootstrap.setPipelineFactory(new HttpPipelineFactory(controller, false, threadPool));
	}

	public synchronized void start()
		throws IOException
	{
		ch = bootstrap.bind(addr);
		System.out.println("Http Server Start ... ");
		controller.printURL();
	}

	public synchronized void stop()
	{
		if (ch != null)
			ch.close().awaitUninterruptibly();
		factory.releaseExternalResources();
	}
}
