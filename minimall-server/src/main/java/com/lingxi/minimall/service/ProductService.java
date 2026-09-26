package com.lingxi.minimall.service;

import com.lingxi.minimall.dto.ProductCreateDTO;
import com.lingxi.minimall.dto.ProductQueryDTO;
import com.lingxi.minimall.dto.ProductUpdateDTO;
import com.lingxi.minimall.entity.Product;
import com.lingxi.minimall.vo.PageResult;

import java.util.List;

public interface ProductService {
    Product getById(Long id);

    Product create(ProductCreateDTO productCreateDTO);

    Product update(Long id, ProductUpdateDTO productUpdateDTO);

    int deleteById(Long id);

    List<Product> list();

    PageResult<Product> pageQuery(ProductQueryDTO queryDTO);
}
