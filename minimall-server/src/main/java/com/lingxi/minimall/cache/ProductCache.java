package com.lingxi.minimall.cache;

import com.lingxi.minimall.entity.Product;
import java.time.Duration;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

/**
 * 商品详情的 Cache Aside 实现。先查 Redis，未命中再查 MySQL 并回填。
 * 库存虽然包含在详情中，但 TTL 很短，订单成功后还会主动删除缓存。
 */
@Component
public class ProductCache {
    private static final Logger log = LoggerFactory.getLogger(ProductCache.class);
    private static final String NULL_MARKER = "__MISSING__";
    private final StringRedisTemplate redis;
    private final ObjectMapper json;

    public ProductCache(StringRedisTemplate redis, ObjectMapper json) {
        this.redis = redis;
        this.json = json;
    }

    public Product find(Long id, Supplier<Product> databaseLookup) {
        String key = "minimall:product:" + id;
        try {
            String cached = redis.opsForValue().get(key);
            if (NULL_MARKER.equals(cached)) {
                log.info("CACHE HIT negative productId={}", id);
                return null;
            }
            if (cached != null) {
                log.info("CACHE HIT productId={}", id);
                return json.readValue(cached, Product.class);
            }
        } catch (RuntimeException e) {
            // Redis 故障不能拖垮商品查询，退回数据库主链。
            log.warn("Redis read unavailable for productId={}: {}", id, e.getMessage());
        }

        log.info("CACHE MISS productId={}", id);
        log.info("DB QUERY productId={}", id);
        Product product = databaseLookup.get();
        try {
            // 不存在的 ID 短暂缓存，防止同一个无效 ID 持续打到数据库。
            redis.opsForValue().set(key, product == null ? NULL_MARKER : json.writeValueAsString(product),
                    product == null ? Duration.ofSeconds(20) : Duration.ofSeconds(60));
        } catch (RuntimeException e) {
            log.warn("Redis write unavailable for productId={}: {}", id, e.getMessage());
        }
        return product;
    }

    public void evict(Long id) {
        try {
            redis.delete("minimall:product:" + id);
            log.info("CACHE EVICT productId={}", id);
        } catch (RuntimeException e) {
            log.warn("Redis evict unavailable for productId={}: {}", id, e.getMessage());
        }
    }
}
