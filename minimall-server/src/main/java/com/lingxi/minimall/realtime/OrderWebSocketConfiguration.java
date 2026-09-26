package com.lingxi.minimall.realtime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

/** 将 /ws/orders 的 HTTP 升级请求交给 WebSocket Handler。 */
@Configuration
@EnableWebSocket
public class OrderWebSocketConfiguration implements WebSocketConfigurer {
    private final OrderWebSocketHandler handler;
    private final String allowedOrigins;
    public OrderWebSocketConfiguration(OrderWebSocketHandler handler,
            @Value("${WS_ALLOWED_ORIGINS:http://localhost:5173,http://localhost:8080}") String allowedOrigins) {
        this.handler = handler;
        this.allowedOrigins = allowedOrigins;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/orders")
                .setAllowedOriginPatterns(allowedOrigins.split(","));
    }
}
