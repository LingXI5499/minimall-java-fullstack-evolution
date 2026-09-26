package com.lingxi.minimall.integration;

import static org.assertj.core.api.Assertions.assertThat;

import com.lingxi.minimall.dto.ProductQueryDTO;
import com.lingxi.minimall.entity.Product;
import com.lingxi.minimall.mapper.ProductMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/** 使用真实 MySQL 验证 XML 映射、动态 SQL 与原子库存扣减。 */
@SpringBootTest(properties = {
    "JWT_SECRET=test-only-key-that-is-long-enough-for-hs256-2026",
    "ADMIN_PASSWORD=test-admin-password-2026", "USER_PASSWORD=test-user-password-2026",
    "spring.rabbitmq.listener.simple.auto-startup=false", "app.orders.expiry-scan-ms=99999999"
})
@EnabledIfEnvironmentVariable(named = "RUN_DB_TESTS", matches = "true")
class ProductMapperIntegrationTest {
    @Autowired ProductMapper mapper;

    @Test void dynamicQueryAndAtomicStockUseRealSql() {
        Product product = new Product();
        product.setName("mapper-test-" + System.nanoTime());
        product.setPrice(new BigDecimal("12.50"));
        product.setStock(1);
        mapper.insert(product);
        try {
            ProductQueryDTO query = new ProductQueryDTO();
            query.setName(product.getName());
            query.setStatus(1);
            query.setMinPrice(new BigDecimal("10"));
            query.setMaxPrice(new BigDecimal("13"));
            query.setOffset(0);
            assertThat(mapper.countByCondition(query)).isEqualTo(1);
            assertThat(mapper.selectPageByCondition(query)).hasSize(1);
            assertThat(mapper.decreaseStock(product.getId(), 1)).isEqualTo(1);
            assertThat(mapper.decreaseStock(product.getId(), 1)).isZero();
            assertThat(mapper.selectById(product.getId()).getStock()).isZero();
        } finally { mapper.deleteById(product.getId()); }
    }
}
