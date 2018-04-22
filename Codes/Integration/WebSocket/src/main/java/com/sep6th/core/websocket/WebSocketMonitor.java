package com.sep6th.core.websocket;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 *  
 *  该类只是为了让服务端主动给客户端发信息的辅助类，
 *  实际开发中，可忽略。
 */

public class WebSocketMonitor implements Runnable  {

	@Override
	public void run() {
		WebSocketServe serve = new WebSocketServe();
		serve.sendMsg("当前时间:" + new Date());
	}
	
	/**
	 * 定时间隔发送
	 */
	public void sendMsg() {
        ScheduledExecutorService newScheduledThreadPool = Executors.newSingleThreadScheduledExecutor();
        newScheduledThreadPool.scheduleWithFixedDelay(new WebSocketMonitor(), 20, 5, TimeUnit.SECONDS);

	}
	
}
