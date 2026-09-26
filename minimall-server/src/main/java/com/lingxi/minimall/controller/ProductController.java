package com.lingxi.minimall.controller;

import com.lingxi.minimall.common.Result;
import com.lingxi.minimall.dto.ProductCreateDTO;
import com.lingxi.minimall.dto.ProductQueryDTO;
import com.lingxi.minimall.dto.ProductUpdateDTO;
import com.lingxi.minimall.entity.Product;
import com.lingxi.minimall.service.ProductService;
import com.lingxi.minimall.vo.PageResult;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** 商品 HTTP 入口：接收 DTO、触发校验，再把 Service 的结果装入统一响应。 */
@RestController
@Tag(name = "商品", description = "读取商品需登录，写入商品需 ADMIN")
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService service;
    public ProductController(ProductService service) { this.service = service; }

    @PostMapping
    @Operation(summary = "新增商品", description = "ADMIN；名称、价格和库存通过 Bean Validation 校验")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "创建成功"), @ApiResponse(responseCode = "400", description = "参数错误"), @ApiResponse(responseCode = "403", description = "权限不足")})
    public ResponseEntity<Result<Product>> create(@Valid @RequestBody ProductCreateDTO body) {
        return ResponseEntity.status(HttpStatus.CREATED).body(Result.created(service.create(body)));
    }

    @GetMapping("/{id}")
    @Operation(summary = "按 ID 查询商品")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "查询成功"), @ApiResponse(responseCode = "404", description = "商品不存在")})
    public Result<Product> getById(@PathVariable Long id) { return Result.success(service.getById(id)); }

    @GetMapping
    @Operation(summary = "分页和多条件查询商品")
    public Result<PageResult<Product>> pageQuery(@Valid @ModelAttribute ProductQueryDTO query) {
        return Result.success(service.pageQuery(query));
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改商品（ADMIN）")
    public Result<Product> update(@PathVariable Long id, @Valid @RequestBody ProductUpdateDTO body) {
        return Result.success(service.update(id, body));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除商品（ADMIN）")
    public Result<Integer> delete(@PathVariable Long id) { return Result.success(service.deleteById(id)); }
}
