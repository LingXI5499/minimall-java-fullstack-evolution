USE minimall;

-- V9 开始记录订单归属。旧订单的 owner_username 为空，仅管理员可查看。
ALTER TABLE orders ADD COLUMN owner_username VARCHAR(80) NULL;
CREATE INDEX idx_orders_owner_created ON orders (owner_username, create_time);
