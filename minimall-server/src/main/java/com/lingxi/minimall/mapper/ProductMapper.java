package com.lingxi.minimall.mapper;

import com.lingxi.minimall.dto.ProductQueryDTO;
import com.lingxi.minimall.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    Product selectById(Long id);

    int insert(Product product);

    int update(Product product);

    int deleteById(Long id);

    List<Product> selectAll();

    long countByCondition(ProductQueryDTO queryDTO);

    List<Product> selectPageByCondition(ProductQueryDTO queryDTO);
}
