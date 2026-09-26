package com.lingxi.minimall.controller;

import com.lingxi.minimall.common.Result;
import com.lingxi.minimall.dto.ProductCreateDTO;
import com.lingxi.minimall.dto.ProductQueryDTO;
import com.lingxi.minimall.dto.ProductUpdateDTO;
import com.lingxi.minimall.entity.Product;
import com.lingxi.minimall.service.ProductService;
import com.lingxi.minimall.vo.PageResult;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** 商品 HTTP 入口：接收 DTO、触发校验，再把 Service 的结果装入统一响应。 */
@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @PostMapping
    public ResponseEntity<Result<Product>> create(@Valid @RequestBody ProductCreateDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Result.created(service.create(body)));
    }

    @GetMapping("/{id}")
    public Result<Product> getById(@PathVariable Long id) { return Result.success(service.getById(id)); }

    @GetMapping
    public Result<PageResult<Product>> pageQuery(@Valid @ModelAttribute ProductQueryDTO query) {
        return Result.success(service.pageQuery(query));
    }

    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO body) {
        return Result.success(service.update(id, body));
    }

    @DeleteMapping("/{id}")
    public Result<Integer> delete(@PathVariable Long id) { return Result.success(service.deleteById(id)); }
}
