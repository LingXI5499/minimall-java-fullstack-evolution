package com.lingxi.minimall.controller;

import com.lingxi.minimall.common.Result;
import com.lingxi.minimall.exception.BusinessException;
import com.lingxi.minimall.security.WsTicketService;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 登录签发短期 JWT；管理员可换取一次性 WebSocket 握手票据。 */
@RestController
@Tag(name = "认证", description = "登录签发 JWT，管理员换取 WebSocket 一次性票据")
@RequestMapping("/api/auth")
public class AuthController {
    public record LoginRequest(@NotBlank String username, @NotBlank String password) {}
    public record LoginResponse(String token, String username, String role, Instant expiresAt) {}
    public record TicketResponse(String ticket) {}
    private final AuthenticationManager authentication;
    private final JwtEncoder encoder;
    private final WsTicketService tickets;
    public AuthController(AuthenticationManager authentication, JwtEncoder encoder, WsTicketService tickets) {
        this.authentication = authentication;
        this.encoder = encoder;
        this.tickets = tickets;
    }

    @PostMapping("/login")
    @Operation(summary = "用户名密码登录", description = "公开接口；成功返回 2 小时有效的 JWT")
    @SecurityRequirements
    public Result<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication user = authentication.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            String role = user.getAuthorities().stream().findFirst().orElseThrow().getAuthority().replace("ROLE_", "");
            Instant now = Instant.now();
            Instant expires = now.plusSeconds(7200);
            JwtClaimsSet claims = JwtClaimsSet.builder().issuer("minimall").subject(user.getName())
                    .issuedAt(now).expiresAt(expires).claim("role", role).build();
            String token = encoder.encode(JwtEncoderParameters.from(
                    JwsHeader.with(MacAlgorithm.HS256).build(), claims)).getTokenValue();
            return Result.success(new LoginResponse(token, user.getName(), role, expires));
        } catch (AuthenticationException e) {
            throw new BusinessException(HttpStatus.UNAUTHORIZED, "用户名或密码错误");
        }
    }

    @PostMapping("/ws-ticket")
    @Operation(summary = "获取 WebSocket 握手票据", description = "仅 ADMIN；票据 60 秒有效且只能使用一次")
    public Result<TicketResponse> wsTicket() { return Result.success(new TicketResponse(tickets.issue())); }
}
