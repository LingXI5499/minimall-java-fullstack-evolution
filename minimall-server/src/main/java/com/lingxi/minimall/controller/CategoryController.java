package com.lingxi.minimall.controller;

import com.lingxi.minimall.common.Result;
import com.lingxi.minimall.entity.Category;
import com.lingxi.minimall.mapper.CategoryMapper;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** 提供分类选项，供商品页面选择现有分类。 */
@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryMapper mapper;
    public CategoryController(CategoryMapper mapper) { this.mapper = mapper; }
    @GetMapping
    public Result<List<Category>> list() { return Result.success(mapper.selectAll()); }
}
