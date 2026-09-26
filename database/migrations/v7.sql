USE minimall;

-- 从 V6 升级只需执行一次；CREATE IF NOT EXISTS 也方便全新数据库初始化。
CREATE TABLE IF NOT EXISTS order_notification (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  order_id BIGINT NOT NULL,
  message VARCHAR(255) NOT NULL,
  processed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_order_notification_order (order_id)
) ENGINE=InnoDB;
