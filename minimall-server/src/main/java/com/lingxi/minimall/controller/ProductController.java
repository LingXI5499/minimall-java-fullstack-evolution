package com.lingxi.minimall.controller;

import com.lingxi.minimall.dto.ProductCreateDTO;
import com.lingxi.minimall.dto.ProductUpdateDTO;
import com.lingxi.minimall.entity.Product;
import com.lingxi.minimall.service.ProductService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService){
        this.productService=productService;
    }

    @PostMapping
    public Product create(
            @RequestBody ProductCreateDTO productCreateDTO
    ){

        return productService.create(productCreateDTO);
    }


    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id){
        return productService.getById(id);
    }

    @PutMapping("/{id}")
    public Product update(
            @PathVariable Long id,
            @RequestBody ProductUpdateDTO productUpdateDTO) {

        return productService.update(id, productUpdateDTO);
    }

    @DeleteMapping("/{id}")
    public int deleteById(@PathVariable Long id) {

        return productService.deleteById(id);
    }

    @GetMapping
    public List<Product> list() {
        return productService.list();
    }
}
