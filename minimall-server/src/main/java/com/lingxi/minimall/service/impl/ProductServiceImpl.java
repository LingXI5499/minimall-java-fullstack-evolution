package com.lingxi.minimall.service.impl;

import com.lingxi.minimall.dto.ProductCreateDTO;
import com.lingxi.minimall.dto.ProductUpdateDTO;
import com.lingxi.minimall.entity.Product;
import com.lingxi.minimall.mapper.ProductMapper;
import com.lingxi.minimall.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;

    public ProductServiceImpl(ProductMapper productMapper){
        this.productMapper=productMapper;
    }

    @Override
    public Product getById(Long id){
        return productMapper.selectById(id);
    }

    @Override
    public Product create(ProductCreateDTO productCreateDTO) {

        Product product = new Product();

        product.setName(productCreateDTO.getName());
        product.setPrice(productCreateDTO.getPrice());
        product.setStock(productCreateDTO.getStock());

        productMapper.insert(product);

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

        productMapper.update(product);

        return productMapper.selectById(id);
    }

    @Override
    public int deleteById(Long id) {

        return productMapper.deleteById(id);
    }

    @Override
    public List<Product> list() {
        return productMapper.selectAll();
    }
}
