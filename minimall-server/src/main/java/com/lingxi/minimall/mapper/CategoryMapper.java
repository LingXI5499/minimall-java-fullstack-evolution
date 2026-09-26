package com.lingxi.minimall.mapper;

import com.lingxi.minimall.entity.Category;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;

/** 分类表 SQL 入口，商品表通过 category_id 引用这里的主键。 */
@Mapper
public interface CategoryMapper {
    List<Category> selectAll();
    Category selectById(Long id);
}
