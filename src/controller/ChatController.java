package controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/chatting") //웹소켓의 서버 클래스
public class ChatController {
   private static Set<Session> clients = Collections.synchronizedSet(new HashSet<Session>());

   @OnMessage	//client에서 메세지 수신시
   public void onMessage(String message, Session session) throws IOException {
      synchronized (clients) {
         for (Session client : clients) {
            if (!client.equals(session)) { 
               client.getBasicRemote().sendText(message);
            }
         }
      }
   }

   @OnOpen
   public void onOpen(Session session) {
      clients.add(session);
   }

   @OnClose
   public void onClose(Session session) {
      clients.remove(session);
   }
}