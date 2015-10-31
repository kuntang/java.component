// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   ActionController.java

package com.sohu.smc.common.http.server;

import com.sohu.smc.common.http.server.*;

import java.util.Map;

// Referenced classes of package com.sohu.smc.common.http.server:
//			Controller, Action

public class ActionController extends Controller
{

	public ActionController(String name)
	{
		super(name);
	}

	public void addAction(String path, com.sohu.smc.common.http.server.Action act)
	{
        Object key = name.startsWith("/") ? "" : ((Object) ((new StringBuilder()).append("/").append(name).append("/").append(path).toString()));
        System.out.println("action key="+key);
        actions.put(key, act);
	}


}
