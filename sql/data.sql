-- DROP TABLE IF EXISTS `permissions`;
-- CREATE TABLE `permissions` (
--                                `name` varchar(255) NOT NULL,
--                                `description` varchar(255) DEFAULT NULL,
--                                PRIMARY KEY (`name`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
--
-- LOCK TABLES `permissions` WRITE;
INSERT IGNORE  INTO `permissions` VALUES
('PASTRY:CREATE','Allow create new pastry'),
('PASTRY:EDIT','Allow create new pastry'),
('PASTRY:DELETE','Allow delete pastry'),
('PASTRY:FIND','Allow find pastry'),
('USER:CREATE','Allow create user'),
('USER:EDIT','Allow edit user'),
('USER:DELETE','Allow delete user by id'),
('USER:FIND','Allow find user by id');
-- UNLOCK TABLES;
--
--
-- DROP TABLE IF EXISTS `roles`;
--
-- CREATE TABLE `roles` (
--                          `name` varchar(255) NOT NULL,
--                          `description` varchar(255) DEFAULT NULL,
--                          PRIMARY KEY (`name`)
-- ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
--
-- LOCK TABLES `roles` WRITE;

INSERT IGNORE INTO `roles` VALUES
('ADMIN','All permission!'),
('GUEST','Guest permission!'),
('EMPLOYEE','Employee permission!'),
('CUSTOMER','Member permission!');
UNLOCK TABLES;

INSERT IGNORE  INTO roles_permissions (role_name, permissions_name) VALUES
('ADMIN', 'PASTRY:CREATE'),
('ADMIN', 'PASTRY:EDIT'),
('ADMIN', 'PASTRY:DELETE'),
('ADMIN', 'PASTRY:FIND'),
('ADMIN', 'USER:CREATE'),
('ADMIN', 'USER:EDIT'),
('ADMIN', 'USER:DELETE'),
('ADMIN', 'USER:FIND'),

('EMPLOYEE', 'PASTRY:FIND'),
('EMPLOYEE', 'PASTRY:EDIT'),

('CUSTOMER', 'PASTRY:FIND'),
-- Insert test user
INSERT IGNORE INTO users (id, firstName, lastName, email, phoneNumber, address, gender, avatar_url, bio, lastSeen, createdAt, updatedAt, status, dateOfBirth) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'Test', 'User', 'test@example.com', '1234567890', 'Test Address', 'MALE', NULL, 'Test bio', NOW(), NOW(), NOW(), 'ONLINE', '1990-01-01');

-- Insert test account
INSERT IGNORE INTO accounts (id, type, credential, password, isVerified, createdAt, lastLogin, user_id) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'EMAIL', 'test@example.com', '$2a$10$dummyhashedpassword', true, NOW(), NOW(), '550e8400-e29b-41d4-a716-446655440000');

-- Assign role to user
INSERT IGNORE INTO user_roles (user_id, role_id) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'CUSTOMER');
('GUEST', 'PASTRY:FIND');

