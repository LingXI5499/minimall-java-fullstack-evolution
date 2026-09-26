package com.lingxi.minimall.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/** V0 保留的请求链演示：展示路径、查询参数和 JSON 请求体如何进入 Controller。 */
@RestController
@RequestMapping("/api")
public class HelloController {
    public record EchoRequest(String message) {}

    @GetMapping("/hello")
    public Map<String, String> hello() { return Map.of("message", "Hello MiniMall"); }

    @GetMapping("/info")
    public Map<String, String> info() { return Map.of("stage", "v0", "chain", "Browser → Spring MVC → Controller → Jackson"); }

    @GetMapping("/greet")
    public Map<String, String> greet(@RequestParam String name) { return Map.of("message", "Hello " + name); }

    @GetMapping("/users/{id}")
    public Map<String, Long> user(@PathVariable Long id) { return Map.of("id", id); }

    @PostMapping("/echo")
    public EchoRequest echo(@RequestBody EchoRequest request) { return request; }
}
