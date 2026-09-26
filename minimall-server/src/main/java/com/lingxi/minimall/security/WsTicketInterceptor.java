package com.lingxi.minimall.security;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

/** WebSocket 升级握手时消费票据；无效或重复使用都拒绝连接。 */
@Component
public class WsTicketInterceptor implements HandshakeInterceptor {
    private final WsTicketService tickets;
    public WsTicketInterceptor(WsTicketService tickets) { this.tickets = tickets; }
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler handler, Map<String, Object> attributes) {
        String ticket = UriComponentsBuilder.fromUri(request.getURI()).build().getQueryParams().getFirst("ticket");
        if (tickets.consume(ticket)) return true;
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return false;
    }
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler handler, Exception exception) {}
}
