package com.liujie.websocket;

import java.io.IOException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import com.liujie.websocket.socketmap.SocketMap;

/**
 * 类似于servlet，不需要main方法， 只需要url中写明就可以访问到
 * 
 * @author liujie
 */
@ServerEndpoint("/websocket")
public class WebSocketTest {

	@OnMessage
	public void onMessage(String message,Session session) throws IOException, InterruptedException {
		// 输出sessionID
		// System.out.println("sessionID:" + session.getId());
		// 接收客户端消息
		// 发送消息
		// session.getBasicRemote().sendText("This is the first server
		// message");
		// 发送三条消息
		// int sentMessages = 0;
		// while (sentMessages < 3) {
		// session.getBasicRemote().sendText("This is an intermediate server
		// message. Count: " + sentMessages);
		// sentMessages++;
		// }
		// 向所有session发送消息
		// 结束消息
		// session.getBasicRemote().sendText("This is the last server message");
		System.out.println("sessionID:" + session.getId() + " " + message);
		SocketMap.websocketMap.keySet().forEach(key -> {
			Session sendSession = (Session) SocketMap.websocketMap.get(key);
			// 发送消息
			try {
				sendSession.getBasicRemote().sendText("sessionId:" + session.getId() + " " + message);
			} catch (IOException e) {
				System.out.println("给sessionId:" + sendSession.getId() + "发消息发生异常,异常信息:" + e.getMessage());
			}
		});
	}

	@OnOpen
	public void onOpen(Session session) {
		// 建立连接操作
		System.out.println("sessionId:" + session.getId() + "加入了进来");
		// 向所有session发送消息
		SocketMap.websocketMap.keySet().forEach(key -> {
			Session sendSession = (Session) SocketMap.websocketMap.get(key);
			// 发送消息
			try {
				sendSession.getBasicRemote().sendText("sessionId:" + session.getId() + "加入了进来");
			} catch (IOException e) {
				System.out.println("给sessionId:" + session.getId() + "发消息发生异常,异常信息:" + e.getMessage());
			}
		});
		SocketMap.websocketMap.put(session.getId(), session);
		//发送给链接人
		try {
			session.getBasicRemote().sendText("恭喜您创建链接成功 您的sessionID是:" + session.getId());
		} catch (IOException e) {
			System.out.println("给sessionId:" + session.getId() + "发消息发生异常,异常信息:" + e.getMessage());
		}
	}

	@OnClose
	public void onClose(Session session) {
		// 断开链接
		SocketMap.websocketMap.remove(session.getId());
		System.out.println("sessionId:" + session.getId() + "断开了链接");
		// 向所有session发送消息
		SocketMap.websocketMap.keySet().forEach(key -> {
			Session sendSession = (Session) SocketMap.websocketMap.get(key);
			// 发送消息
			try {
				sendSession.getBasicRemote().sendText("sessionId:" + session.getId() + "断开");
			} catch (IOException e) {
				System.out.println("给sessionId:" + sendSession.getId() + "发消息发生异常,异常信息:" + e.getMessage());
			}
		});
	}

}
