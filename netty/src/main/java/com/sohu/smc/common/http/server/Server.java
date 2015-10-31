// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Server.java

package com.sohu.smc.common.http.server;

import java.io.IOException;

public abstract class Server
{

	public Server()
	{
	}

	public abstract void start()
		throws IOException;

	public abstract void stop();
}
