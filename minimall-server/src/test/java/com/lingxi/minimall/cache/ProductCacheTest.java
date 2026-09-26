package com.lingxi.minimall.cache;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.lingxi.minimall.entity.Product;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import tools.jackson.databind.ObjectMapper;

/** 用假的 Redis 操作验证命中与未命中的分支，无需依赖本机 Redis 服务。 */
class ProductCacheTest {
    private final StringRedisTemplate redis = mock(StringRedisTemplate.class);
    @SuppressWarnings("unchecked")
    private final ValueOperations<String, String> values = mock(ValueOperations.class);
    private final ObjectMapper json = new ObjectMapper();
    private final ProductCache cache = new ProductCache(redis, json);

    @Test void missLoadsDatabaseAndWritesTtl() {
        when(redis.opsForValue()).thenReturn(values);
        Product product = new Product();
        product.setId(7L);
        product.setName("键盘");
        AtomicInteger queries = new AtomicInteger();

        Product result = cache.find(7L, () -> { queries.incrementAndGet(); return product; });

        assertThat(result.getName()).isEqualTo("键盘");
        assertThat(queries).hasValue(1);
        verify(values).set(eq("minimall:product:7"), any(String.class), eq(Duration.ofSeconds(60)));
    }

    @Test void hitDoesNotQueryDatabase() {
        when(redis.opsForValue()).thenReturn(values);
        when(values.get("minimall:product:7")).thenReturn("{\"id\":7,\"name\":\"键盘\"}");
        Product result = cache.find(7L, () -> { throw new AssertionError("命中缓存时不应查数据库"); });
        assertThat(result.getId()).isEqualTo(7L);
        verify(values, never()).set(any(), any(), any(Duration.class));
    }

    @Test void missingProductUsesShortNegativeCache() {
        when(redis.opsForValue()).thenReturn(values);
        assertThat(cache.find(999L, () -> null)).isNull();
        verify(values).set("minimall:product:999", "__MISSING__", Duration.ofSeconds(20));
    }

    @Test void redisFailureFallsBackToDatabase() {
        when(redis.opsForValue()).thenThrow(new IllegalStateException("Redis unavailable"));
        Product product = new Product();
        product.setId(8L);
        assertThat(cache.find(8L, () -> product)).isSameAs(product);
    }
}
