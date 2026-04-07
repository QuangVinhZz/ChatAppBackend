-- Insert roles
INSERT IGNORE INTO `roles` VALUES
('ADMIN','All permission!'),
('GUEST','Guest permission!'),
('EMPLOYEE','Employee permission!'),
('CUSTOMER','Member permission!');

-- Insert permissions
INSERT IGNORE INTO `permissions` VALUES
('PASTRY:CREATE','Allow create new pastry'),
('PASTRY:EDIT','Allow create new pastry'),
('PASTRY:DELETE','Allow delete pastry'),
('PASTRY:FIND','Allow find pastry'),
('USER:CREATE','Allow create user'),
('USER:EDIT','Allow edit user'),
('USER:DELETE','Allow delete user by id'),
('USER:FIND','Allow find user by id');

-- Insert role-permission mappings
INSERT IGNORE INTO roles_permissions (role_name, permissions_name) VALUES
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
('GUEST', 'PASTRY:FIND');
