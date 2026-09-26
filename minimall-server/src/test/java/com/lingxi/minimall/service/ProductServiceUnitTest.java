package com.lingxi.minimall.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import com.lingxi.minimall.cache.ProductCache;
import com.lingxi.minimall.dto.ProductCreateDTO;
import com.lingxi.minimall.entity.Product;
import com.lingxi.minimall.exception.BusinessException;
import com.lingxi.minimall.mapper.CategoryMapper;
import com.lingxi.minimall.mapper.ProductMapper;
import com.lingxi.minimall.service.impl.ProductServiceImpl;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

/** 只测 Service 的业务决策，不连接数据库或 Redis。 */
class ProductServiceUnitTest {
    private final ProductMapper mapper = mock(ProductMapper.class);
    private final CategoryMapper categories = mock(CategoryMapper.class);
    private final ProductCache cache = mock(ProductCache.class);
    private final ProductServiceImpl service = new ProductServiceImpl(mapper, categories, cache);

    @Test void createCopiesEditableFieldsIntoEntity() {
        ProductCreateDTO request = new ProductCreateDTO();
        request.setName("测试键盘");
        request.setPrice(new BigDecimal("12.50"));
        request.setStock(3);
        Product created = service.create(request);
        assertThat(created.getName()).isEqualTo("测试键盘");
        assertThat(created.getPrice()).isEqualByComparingTo("12.50");
        assertThat(created.getStock()).isEqualTo(3);
        verify(mapper).insert(created);
    }

    @Test void missingProductBecomesNotFound() {
        when(cache.find(eq(999L), any())).thenReturn(null);
        assertThatThrownBy(() -> service.getById(999L))
                .isInstanceOfSatisfying(BusinessException.class,
                        error -> assertThat(error.getStatus()).isEqualTo(HttpStatus.NOT_FOUND));
    }
}
