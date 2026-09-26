package com.lingxi.minimall.mapper;

import com.lingxi.minimall.dto.ProductQueryDTO;
import com.lingxi.minimall.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品 SQL 的 Java 入口。这里没有手写 MapperImpl：MyBatis 在运行时生成代理，
 * 用方法名匹配 ProductMapper.xml 中的 statement id。
 */
@Mapper
public interface ProductMapper {
    Product selectById(Long id);

    int insert(Product product);

    int update(Product product);

    int deleteById(Long id);

    List<Product> selectAll();

    long countByCondition(ProductQueryDTO queryDTO);

    List<Product> selectPageByCondition(ProductQueryDTO queryDTO);
    int decreaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);
    int increaseStock(@Param("id") Long id, @Param("quantity") Integer quantity);
}
