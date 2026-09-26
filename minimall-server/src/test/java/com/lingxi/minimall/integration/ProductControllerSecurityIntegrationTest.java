package com.lingxi.minimall.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

/** 经真实 FilterChain 和 Controller 验证 401、403、Validation 与管理员写入。 */
@SpringBootTest(properties = {
    "JWT_SECRET=test-only-key-that-is-long-enough-for-hs256-2026",
    "ADMIN_PASSWORD=test-admin-password-2026", "USER_PASSWORD=test-user-password-2026",
    "spring.rabbitmq.listener.simple.auto-startup=false", "app.orders.expiry-scan-ms=99999999"
})
@AutoConfigureMockMvc
@EnabledIfEnvironmentVariable(named = "RUN_DB_TESTS", matches = "true")
class ProductControllerSecurityIntegrationTest {
    @Autowired MockMvc mvc;
    @Autowired ObjectMapper json;

    @Test void securityAndHttpContract() throws Exception {
        mvc.perform(get("/api/products")).andExpect(status().isUnauthorized());
        String user = login("user", "test-user-password-2026");
        String admin = login("admin", "test-admin-password-2026");
        mvc.perform(get("/api/products").header("Authorization", "Bearer " + user))
                .andExpect(status().isOk()).andExpect(jsonPath("$.code").value(200));
        mvc.perform(post("/api/products").header("Authorization", "Bearer " + user)
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"forbidden\",\"price\":1,\"stock\":1}"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/api/products").header("Authorization", "Bearer " + admin)
                .contentType(MediaType.APPLICATION_JSON).content("{\"name\":\"\",\"price\":-1,\"stock\":1}"))
                .andExpect(status().isBadRequest());
        mvc.perform(post("/api/auth/ws-ticket").header("Authorization", "Bearer " + user))
                .andExpect(status().isForbidden());

        String body = "{\"name\":\"mvc-test-" + System.nanoTime() + "\",\"price\":5,\"stock\":1}";
        String response = mvc.perform(post("/api/products").header("Authorization", "Bearer " + admin)
                .contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.code").value(201))
                .andReturn().getResponse().getContentAsString();
        long id = json.readTree(response).path("data").path("id").asLong();
        assertThat(id).isPositive();
        mvc.perform(delete("/api/products/{id}", id).header("Authorization", "Bearer " + admin))
                .andExpect(status().isOk());
    }

    private String login(String username, String password) throws Exception {
        String body = json.writeValueAsString(new LoginInput(username, password));
        String response = mvc.perform(post("/api/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        return json.readTree(response).path("data").path("token").asText();
    }

    private record LoginInput(String username, String password) {}
}
