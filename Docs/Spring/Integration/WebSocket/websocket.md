# websocket

## WebSocket是什么？
WebSocket 是基于TCP的一种网络通信协议。它实现了浏览器与服务器全双工通信——允许服务器主动发送信息给客户端。

## 为什么需要WebSocket ？
HTTP 协议是一种无状态的、无连接的、单向的应用层协议。它采用了请求/响应模型。通信请求只能由客户端发起，服务端对请求做出应答处理，无法实现服务器主动向客户端发起消息。注定了如果服务器有连续的状态变化，客户端要获知就非常麻烦。大多数 Web 应用程序将通过频繁的异步AJAX请求实现长轮询。轮询的效率低，非常浪费资源。

## WebSocket如何工作？
Web浏览器和服务器都必须实现 WebSocket协议来建立和维护连接。由于WebSocket连接长期存在，与典型的HTTP连接不同，对服务器有重要的影响。

## WebSocket服务端
### 采用javax.websocket实现
#### 添加maven依赖

```xml
<dependency>
	<groupId>javax.websocket</groupId>
	<artifactId>javax.websocket-api</artifactId>
	<version>1.1</version>
	<scope>provided</scope>
</dependency>

```

## 定义websocket服务器端


```java
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/** 
 * The Apache License 2.0
 * Copyright (c) 2018 sep6th
 * 
 * @ServerEndpoint 注解是一个类层次的注解，
 * 	它的功能主要是将目前的类定义成一个websocket服务器端,
 * 	注解的值将被用于监听用户连接的终端访问URL地址,
 * 	客户端可以通过这个URL来连接到WebSocket服务器端
 */
@ServerEndpoint("/websocket")
public class WebSocketServe {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
    //若要实现服务端与单一客户端通信的话，可以使用Map(ConcurrentMap)来存放，其中Key可以为用户标识
    public static CopyOnWriteArraySet<WebSocketServe> webSocketSet = new CopyOnWriteArraySet<WebSocketServe>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。
     * 		  session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("来自客户端的消息:" + message);
        //群发消息
        for (WebSocketServe item : webSocketSet) {
            try {
                item.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 发送消息
     */
    public void sendMessage(String msg) throws IOException {
        this.session.getBasicRemote().sendText(msg);

    }
    
    /**
     * 自定义消息发送
     */
    public void sendMsg(String msg) {
        for (WebSocketServe item : webSocketSet) {
            try {
                item.sendMessage(msg);
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
	}

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
    	WebSocketServe.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
    	WebSocketServe.onlineCount--;
    }

}
```

## WebSocket客户端


```html
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<body>
    Welcome<br/><input id="text" type="text"/>
    <button onclick="send()">发送消息</button>
    <hr/>
    <button onclick="closeWebSocket()">关闭WebSocket连接</button>
    <hr/>
    <div id="message"></div>
    <table id="tb" class="altrowstable">
		<th align="center" colspan="9">实时信息监控</th>
	</table>
</body>

<script type="text/javascript">
    var websocket = null;
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://localhost:8090/websocket");
    }
    else {
        alert('当前浏览器 Not support websocket')
    }
    //连接发生错误的回调方法
    websocket.onerror = function () {
        setMessageInnerHTML("WebSocket连接发生错误");
    };
    //连接成功建立的回调方法
    websocket.onopen = function () {
        setMessageInnerHTML("WebSocket连接成功");
    }
    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        setMessageInnerHTML(event.data);
    }
    //连接关闭的回调方法
    websocket.onclose = function () {
        setMessageInnerHTML("WebSocket连接关闭");
    }
    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        closeWebSocket();
    }
    //将消息显示在网页上
    function setMessageInnerHTML(innerHTML) {
    	var msg=innerHTML.split(" - ")
		
		var table=document.getElementById("tb");
		 var row;
		 row=table.insertRow(1);
		for(var i=0;i<msg.length;i++){
			 var cell = row.insertCell(i);
		 	 cell.appendChild(document.createTextNode(msg[i]));
		}
		if(table.rows.length>50){
			table.deleteRow(table.rows.length-1);
		}
    }
    //关闭WebSocket连接
    function closeWebSocket() {
        websocket.close();
    }
    //发送消息
    function send() {
        var message = document.getElementById('text').value;
        websocket.send(message);
    }
</script>
</html>
```
## 拓展——http协议

HTTP是一个基于TCP/IP通信协议来传递数据，是一种**无状态的**、**无连接的**、**单向的**应用层协议。
**主要特点**：
1.**简单快速**：客户向服务器请求服务时，只需传送请求方法和路径。请求方法常用的有GET、HEAD、POST。每种方法规定了客户与服务器联系的类型不同。由于HTTP协议简单，使得HTTP服务器的程序规模小，因而通信速度很快。
2.**灵活**：HTTP允许传输任意类型的数据对象。正在传输的类型由Content-Type加以标记。
3.**无连接**：无连接的含义是限制每次连接只处理一个请求。服务器处理完客户的请求，并收到客户的应答后，即断开连接。采用这种方式可以节省传输时间。
4.**无状态**：HTTP协议是无状态协议。无状态是指协议对于事务处理没有记忆能力。缺少状态意味着如果后续处理需要前面的信息，则它必须重传，这样可能导致每次连接传送的数据量增大。另一方面，在服务器不需要先前信息时它的应答就较快。
5.**支持B/S及C/S模式**。
