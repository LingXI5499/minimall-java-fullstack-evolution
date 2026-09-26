package com.lingxi.minimall.service.impl;

import com.lingxi.minimall.dto.ProductCreateDTO;
import com.lingxi.minimall.dto.ProductQueryDTO;
import com.lingxi.minimall.dto.ProductUpdateDTO;
import com.lingxi.minimall.entity.Product;
import com.lingxi.minimall.mapper.ProductMapper;
import com.lingxi.minimall.mapper.CategoryMapper;
import com.lingxi.minimall.exception.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import com.lingxi.minimall.service.ProductService;
import com.lingxi.minimall.vo.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

/** 商品业务实现：组装 Entity、调用 Mapper，并把“查无此商品”转为 404。 */
@Service
public class ProductServiceImpl implements ProductService {
    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;

    public ProductServiceImpl(ProductMapper productMapper, CategoryMapper categoryMapper){
        this.productMapper=productMapper;
        this.categoryMapper=categoryMapper;
    }

    @Override
    public Product getById(Long id){
        Product product = productMapper.selectById(id);
        if (product == null) throw new BusinessException(HttpStatus.NOT_FOUND, "商品不存在");
        return product;
    }

    @Override
    public Product create(ProductCreateDTO productCreateDTO) {

        Product product = new Product();

        product.setName(productCreateDTO.getName());
        product.setPrice(productCreateDTO.getPrice());
        product.setStock(productCreateDTO.getStock());
        requireCategory(productCreateDTO.getCategoryId());
        product.setCategoryId(productCreateDTO.getCategoryId());

        productMapper.insert(product);
        log.info("Created product id={}", product.getId());

        return product;
    }

    @Override
    public Product update(
            Long id,
            ProductUpdateDTO productUpdateDTO) {

        Product product = new Product();

        product.setId(id);
        product.setName(productUpdateDTO.getName());
        product.setPrice(productUpdateDTO.getPrice());
        product.setStock(productUpdateDTO.getStock());
        requireCategory(productUpdateDTO.getCategoryId());
        product.setCategoryId(productUpdateDTO.getCategoryId());

        if (productMapper.update(product) == 0) throw new BusinessException(HttpStatus.NOT_FOUND, "商品不存在");
        log.info("Updated product id={}", id);

        return productMapper.selectById(id);
    }

    @Override
    public int deleteById(Long id) {

        int rows = productMapper.deleteById(id);
        if (rows == 0) throw new BusinessException(HttpStatus.NOT_FOUND, "商品不存在");
        log.info("Deleted product id={}", id);
        return rows;
    }

    @Override
    public List<Product> list() {
        return productMapper.selectAll();
    }

    @Override
    public PageResult<Product> pageQuery(
            ProductQueryDTO queryDTO) {
        // 价格区间的先后关系不是单字段注解能表达的，在业务层检查。
        if (queryDTO.getMinPrice() != null && queryDTO.getMaxPrice() != null
                && queryDTO.getMinPrice().compareTo(queryDTO.getMaxPrice()) > 0) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "最低价格不能大于最高价格");
        }

        Integer page = queryDTO.getPage();
        Integer pageSize = queryDTO.getPageSize();

        if (page == null || page < 1) {
            page = 1;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = 5;
        }

        // SQL OFFSET 从 0 开始；第一页跳过 0 条，第二页跳过 pageSize 条。
        int offset =
                (page - 1) * pageSize;

        queryDTO.setPage(page);
        queryDTO.setPageSize(pageSize);
        queryDTO.setOffset(offset);

        long total =
                productMapper.countByCondition(queryDTO);

        List<Product> records =
                productMapper.selectPageByCondition(queryDTO);

        return new PageResult<>(
                total,
                page,
                pageSize,
                records
        );
    }

    private void requireCategory(Long id) {
        if (id != null && categoryMapper.selectById(id) == null) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "分类不存在");
        }
    }
}
