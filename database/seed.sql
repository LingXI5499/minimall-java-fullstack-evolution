USE minimall;
INSERT IGNORE INTO category (id, name) VALUES (1, '数码'), (2, '家居');
INSERT INTO product (name, price, stock, status, description) VALUES
('机械键盘', 299.00, 25, 1, '教学示例商品'),
('无线鼠标', 99.00, 60, 1, '教学示例商品');
