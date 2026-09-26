package com.lingxi.minimall.entity;

/** 分类表的一行，名称在数据库中唯一。 */
public class Category {
    private Long id;
    private String name;
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
