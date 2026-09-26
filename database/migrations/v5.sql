USE minimall;

-- 从 v4 升级到 v5 时只执行一次；新建库也先运行 schema.sql，再运行本脚本。
-- InnoDB 外键保证分类和订单头存在。历史订单保留商品快照，商品可删除，故 product_id 只建索引。
ALTER TABLE product ADD KEY idx_product_category (category_id), ADD KEY idx_product_status_price (status, price);
ALTER TABLE orders ADD KEY idx_orders_status_created (status, create_time);
ALTER TABLE order_item ADD KEY idx_order_item_product_id (product_id);
ALTER TABLE product ADD CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id);
ALTER TABLE order_item ADD CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders(id);

-- 对比两种查询计划。小表或要求按 id 排序时，优化器可能选择 PRIMARY 而非组合索引。
EXPLAIN SELECT id, name, price FROM product WHERE status = 1 AND price BETWEEN 50 AND 500;
EXPLAIN SELECT id, name, price FROM product WHERE status = 1 AND price BETWEEN 50 AND 500 ORDER BY id DESC LIMIT 10;
