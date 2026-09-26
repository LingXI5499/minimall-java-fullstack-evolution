package com.lingxi.minimall.realtime;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import tools.jackson.databind.ObjectMapper;

/** 保存已连接的后台页面，并把订单变化实时推给它们。 */
@Component
public class OrderWebSocketHandler extends TextWebSocketHandler {
    private static final Logger log = LoggerFactory.getLogger(OrderWebSocketHandler.class);
    private final Set<WebSocketSession> sessions = new CopyOnWriteArraySet<>();
    private final ObjectMapper json;
    public OrderWebSocketHandler(ObjectMapper json) { this.json = json; }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
        log.info("WebSocket connected sessionId={}", session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
        log.info("WebSocket disconnected sessionId={}", session.getId());
    }

    public void broadcast(String type, Long orderId) {
        String payload = json.writeValueAsString(new OrderNotice(type, orderId));
        for (WebSocketSession session : sessions) {
            if (!session.isOpen()) { sessions.remove(session); continue; }
            try {
                // 多个订单事件可能同时发送；同一连接的写操作必须串行。
                synchronized (session) { session.sendMessage(new TextMessage(payload)); }
            } catch (IOException e) {
                sessions.remove(session);
                log.warn("WebSocket send failed sessionId={}", session.getId(), e);
            }
        }
    }

    /** 页面只接收事件类型和订单 ID，再按需重新请求订单详情。 */
    public record OrderNotice(String type, Long orderId) {}
}
