// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   UDPServer.java

package com.sohu.smc.common.http.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sohu.smc.common.http.server.*;
import com.sohu.smc.common.http.server.Controller;
import com.sohu.smc.common.http.server.UdpPipelineFactory;
import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;

// Referenced classes of package com.sohu.smc.common.http.server:
//			Server, UdpPipelineFactory, Controller

public class UDPServer extends com.sohu.smc.common.http.server.Server
{

	protected final InetSocketAddress addr;
	protected final ConnectionlessBootstrap bootstrap;
	protected final DatagramChannelFactory factory;
	protected Channel ch;

	public UDPServer(String host, int port, com.sohu.smc.common.http.server.Controller controller)
	{
		this(new InetSocketAddress(host, port), controller);
	}

	public UDPServer(InetSocketAddress addr, Controller controller)
	{
		this.addr = addr;
		factory = new NioDatagramChannelFactory(Executors.newCachedThreadPool());
		bootstrap = new ConnectionlessBootstrap(factory);
		bootstrap.setOption("receiveBufferSize", Integer.valueOf(0x10000));
		bootstrap.setOption("broadcast", "false");
		bootstrap.setPipelineFactory(new UdpPipelineFactory(controller, false));
	}

	public synchronized void start()
		throws IOException
	{
		ch = bootstrap.bind(addr);
	}

	public synchronized void stop()
	{
		if (ch != null)
			ch.close().awaitUninterruptibly();
		factory.releaseExternalResources();
	}
}
