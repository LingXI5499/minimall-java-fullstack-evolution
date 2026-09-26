package com.lingxi.minimall.security;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

/**
 * 浏览器 WebSocket 不能设置 Bearer 请求头，因此先用 JWT 换一张 60 秒的一次性票据。
 * 票据仅存在当前进程内，适合本教程的单实例部署。
 */
@Component
public class WsTicketService {
    private final Map<String, Instant> tickets = new ConcurrentHashMap<>();
    public String issue() {
        tickets.entrySet().removeIf(entry -> entry.getValue().isBefore(Instant.now()));
        String ticket = UUID.randomUUID().toString();
        tickets.put(ticket, Instant.now().plusSeconds(60));
        return ticket;
    }
    public boolean consume(String ticket) {
        if (ticket == null) return false;
        Instant expiry = tickets.remove(ticket);
        return expiry != null && expiry.isAfter(Instant.now());
    }
}
