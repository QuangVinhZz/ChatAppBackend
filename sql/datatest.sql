-- 1.3. Insert User Roles
INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
('user-uuid-004', 'CUSTOMER'), ('user-uuid-005', 'CUSTOMER'), ('user-uuid-006', 'CUSTOMER'), ('user-uuid-007', 'CUSTOMER'),
('user-uuid-008', 'CUSTOMER'), ('user-uuid-009', 'CUSTOMER'), ('user-uuid-010', 'CUSTOMER'), ('user-uuid-011', 'CUSTOMER'),
('user-uuid-012', 'CUSTOMER'), ('user-uuid-013', 'CUSTOMER'), ('user-uuid-014', 'CUSTOMER'), ('user-uuid-015', 'CUSTOMER'),
('user-uuid-016', 'CUSTOMER'), ('user-uuid-017', 'CUSTOMER'), ('user-uuid-018', 'CUSTOMER'), ('user-uuid-019', 'CUSTOMER'),
('user-uuid-020', 'CUSTOMER'), ('user-uuid-021', 'CUSTOMER'), ('user-uuid-022', 'CUSTOMER'), ('user-uuid-023', 'CUSTOMER');

-- 1.4. Insert Customer Info
INSERT INTO `customers` (`id`, `loyalty_points`) VALUES
('user-uuid-004', 10), ('user-uuid-005', 200), ('user-uuid-006', 0), ('user-uuid-007', 50),
('user-uuid-008', 120), ('user-uuid-009', 30), ('user-uuid-010', 80), ('user-uuid-011', 15),
('user-uuid-012', 40), ('user-uuid-013', 0), ('user-uuid-014', 90), ('user-uuid-015', 300),
('user-uuid-016', 60), ('user-uuid-017', 25), ('user-uuid-018', 0), ('user-uuid-019', 45),
('user-uuid-020', 10), ('user-uuid-021', 100), ('user-uuid-022', 500), ('user-uuid-023', 0);


-- ====================================================================================
-- PHẦN 2: TẠO 20 ĐƠN HÀNG (Trải dài Quý 3 & 4 năm 2025)
-- ====================================================================================

-- 2.1. Insert Orders
-- Lưu ý: Ngày đặt hàng (ngay_dat_hang) phải SAU ngày tạo tài khoản của user đó
INSERT INTO `orders` (
    `id`, `user_id`, `ngay_dat_hang`, `tong_tien`, `trang_thai`,
    `payment_method`, `ly_do_huy`,
    `bank_account_name`, `bank_account_number`, `bank_name`,
    `refund_proof_image_url`
) VALUES
('order-005', 'user-uuid-004', '2025-07-02 09:00:00', 335000, 'COMPLETED', 'CASH', NULL,
    NULL, NULL, NULL, NULL),

('order-006', 'user-uuid-005', '2025-07-20 10:30:00', 60000, 'COMPLETED', 'VN_PAY', NULL,
    'NGUYEN VAN A', '9704198526191432198', 'NCB', NULL),

('order-007', 'user-uuid-006', '2025-08-01 15:00:00', 325000, 'PAID', 'VN_PAY', NULL,
    'NGUYEN VAN A', '9704198526191432198', 'NCB', NULL),

('order-008', 'user-uuid-007', '2025-08-10 18:00:00', 17000, 'CANCELLED', 'CASH', 'Đặt nhầm món',
    NULL, NULL, NULL, NULL),

('order-009', 'user-uuid-008', '2025-08-25 11:20:00', 67000, 'COMPLETED', 'CASH', NULL,
    NULL, NULL, NULL, NULL),

('order-010', 'user-uuid-009', '2025-09-06 08:45:00', 345000, 'COMPLETED', 'VN_PAY', NULL,
    'NGUYEN VAN A', '9704198526191432198', 'NCB', NULL),

('order-011', 'user-uuid-010', '2025-09-15 12:00:00', 52000, 'PENDING', 'BANK_TRANSFER', NULL,
    NULL, NULL, NULL, NULL),

('order-012', 'user-uuid-011', '2025-09-30 19:15:00', 335000, 'COMPLETED', 'CASH', NULL,
    NULL, NULL, NULL, NULL),

('order-013', 'user-uuid-012', '2025-10-05 10:00:00', 48000, 'COMPLETED', 'VN_PAY', NULL,
    'NGUYEN VAN A', '9704198526191432198', 'NCB', NULL),

('order-014', 'user-uuid-013', '2025-10-15 14:30:00', 160000, 'COMPLETED', 'CASH', NULL,
    NULL, NULL, NULL, NULL),

('order-015', 'user-uuid-014', '2025-10-25 09:10:00', 315000, 'REFUND_PENDING', 'VN_PAY', 'Bánh bị móp khi giao',
    'NGUYEN VAN A', '9704198526191432198', 'NCB', NULL),

('order-016', 'user-uuid-015', '2025-11-05 16:40:00', 110000, 'COMPLETED', 'CASH', NULL,
    NULL, NULL, NULL, NULL),

('order-017', 'user-uuid-016', '2025-11-20 11:00:00', 25000, 'CANCELLED', 'CASH', 'Thôi không ăn nữa',
    NULL, NULL, NULL, NULL),

('order-018', 'user-uuid-017', '2025-12-01 13:20:00', 37000, 'COMPLETED', 'VN_PAY', NULL,
    'NGUYEN VAN A', '9704198526191432198', 'NCB', NULL),

('order-019', 'user-uuid-018', '2025-12-08 08:30:00', 42000, 'PAID', 'VN_PAY', NULL,
    'NGUYEN VAN A', '9704198526191432198', 'NCB', NULL),

('order-020', 'user-uuid-019', '2025-12-12 17:00:00', 15000, 'COMPLETED', 'CASH', NULL,
    NULL, NULL, NULL, NULL),

('order-021', 'user-uuid-020', '2025-12-18 10:45:00', 64000, 'PENDING', 'CASH', NULL,
    NULL, NULL, NULL, NULL),

('order-022', 'user-uuid-021', '2025-12-22 15:50:00', 16000, 'COMPLETED', 'CASH', NULL,
    NULL, NULL, NULL, NULL),

('order-023', 'user-uuid-022', '2025-12-25 09:00:00', 670000, 'COMPLETED', 'VN_PAY', NULL,
    'NGUYEN VAN A', '9704198526191432198', 'NCB', NULL),

('order-024', 'user-uuid-023', '2025-12-30 20:00:00', 335000, 'COMPLETED', 'VN_PAY', NULL,
    'NGUYEN VAN A', '9704198526191432198', 'NCB', NULL);



-- 2.2. Insert Order Details (Chi tiết từng món cho các đơn hàng trên)
INSERT INTO `order_detail` (`id`, `don_hang_id`, `banh_ngot_id`, `so_luong`) VALUES
('od-005', 'order-005', 'p001', 1),   -- Hawaii mousse
('od-006', 'order-006', 'p019', 2),   -- Bánh mì gối đen (30k x 2)
('od-007', 'order-007', 'p004', 1),   -- Black Forest
('od-008', 'order-008', 'p027', 1),   -- Croissant
('od-009a', 'order-009', 'p025', 1),  -- Butter Raisins (53k)
('od-009b', 'order-009', 'p093', 1),  -- Bánh mì que (14k)
('od-010', 'order-010', 'p008', 1),   -- Mango cheese cake
('od-011', 'order-011', 'p029', 1),   -- Delicacy Coconut cake
('od-012', 'order-012', 'p002', 1),   -- Tiramisu
('od-013', 'order-013', 'p061', 3),   -- Chocolate 1 (16k x 3)
('od-014', 'order-014', 'p013', 10),  -- Hamburger (16k x 10)
('od-015', 'order-015', 'p009', 1),   -- Passion fruit mousse
('od-016', 'order-016', 'p052', 2),   -- Cigarette cookies (55k x 2)
('od-017', 'order-017', 'p083', 1),   -- Bánh chả
('od-018', 'order-018', 'p071', 1),   -- Green Tea Cake piece
('od-019', 'order-019', 'p073', 1),   -- Red Velvet piece
('od-020', 'order-020', 'p015', 1),   -- Baguette
('od-021', 'order-021', 'p044', 2),   -- Su kem cốm (32k x 2)
('od-022', 'order-022', 'p061', 1),   -- Chocolate 1
('od-023', 'order-023', 'p001', 2),   -- Hawaii mousse (335k x 2)
('od-024', 'order-024', 'p007', 1);   -- White cheese caramel


-- ====================================================================================
-- PHẦN 3: TẠO GIỎ HÀNG (Một vài user đang có hàng trong giỏ)
-- ====================================================================================

-- 3.1. Insert Carts (Cho 3 user cuối cùng)
INSERT INTO `carts` (`id`, `created_at`, `updated_at`, `user_id`) VALUES
('cart-021', NOW(), NOW(), 'user-uuid-021'),
('cart-022', NOW(), NOW(), 'user-uuid-022'),
('cart-023', NOW(), NOW(), 'user-uuid-023');

-- 3.2. Insert Cart Items (Hàng đang nằm trong giỏ)
INSERT INTO `cart_item` (`id`, `so_luong`, `cart_id`, `banh_ngot_id`) VALUES
('ci-003', 2, 'cart-021', 'p015'),  -- User 21 đang định mua 2 Baguette
('ci-004', 1, 'cart-022', 'p076'),  -- User 22 đang ngắm Set Petit fours
('ci-005', 5, 'cart-023', 'p093');  -- User 23 muốn mua 5 Bánh mì que
