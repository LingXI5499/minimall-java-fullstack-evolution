package com.lingxi.minimall.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.lingxi.minimall.dto.OrderCreateDTO;
import com.lingxi.minimall.dto.OrderLineDTO;
import com.lingxi.minimall.entity.Product;
import com.lingxi.minimall.mapper.ProductMapper;
import com.lingxi.minimall.service.OrderService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/** 故意在订单和明细 INSERT 后抛异常，检查真实 MySQL 中没有留下半笔订单。 */
@SpringBootTest(properties = {
    "JWT_SECRET=test-only-key-that-is-long-enough-for-hs256-2026",
    "ADMIN_PASSWORD=test-admin-password-2026", "USER_PASSWORD=test-user-password-2026",
    "app.demo.rollback-enabled=true", "spring.rabbitmq.listener.simple.auto-startup=false",
    "app.orders.expiry-scan-ms=99999999"
})
@EnabledIfEnvironmentVariable(named = "RUN_DB_TESTS", matches = "true")
class OrderRollbackIntegrationTest {
    @Autowired ProductMapper products;
    @Autowired OrderService orders;
    @Autowired JdbcTemplate jdbc;

    @Test void orderAndItemsRollBackTogether() {
        Product product = new Product();
        product.setName("rollback-test-" + System.nanoTime());
        product.setPrice(new BigDecimal("7.00"));
        product.setStock(2);
        products.insert(product);
        Integer beforeOrders = jdbc.queryForObject("SELECT COUNT(*) FROM orders", Integer.class);
        Integer beforeItems = jdbc.queryForObject("SELECT COUNT(*) FROM order_item", Integer.class);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                "test-user", "n/a", List.of(new SimpleGrantedAuthority("ROLE_USER"))));
        try {
            OrderCreateDTO request = new OrderCreateDTO();
            request.setItems(List.of(new OrderLineDTO(product.getId(), 1)));
            request.setSimulateFailure(true);
            assertThatThrownBy(() -> orders.create(request)).isInstanceOf(IllegalStateException.class);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM orders", Integer.class)).isEqualTo(beforeOrders);
            assertThat(jdbc.queryForObject("SELECT COUNT(*) FROM order_item", Integer.class)).isEqualTo(beforeItems);
            assertThat(products.selectById(product.getId()).getStock()).isEqualTo(2);
        } finally {
            SecurityContextHolder.clearContext();
            products.deleteById(product.getId());
        }
    }
}
