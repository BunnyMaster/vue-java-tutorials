-- 插入测试用户（密码使用 BCrypt 加密：123456）
INSERT INTO users (username, password, email)
VALUES ('admin', '{bcrypt}$2a$10$NlTwL934k92KqphtcVCFze106IQuvD1n1JxWszy6U5sSS9ixdnckG', 'admin@example.com'),
       ('user', '{bcrypt}$2a$10$NlTwL934k92KqphtcVCFze106IQuvD1n1JxWszy6U5sSS9ixdnckG', 'user@example.com');

-- 插入角色
INSERT INTO authorities (user_id, authority)
VALUES (1, 'ROLE_ADMIN'),
       (1, 'ROLE_USER'),
       (2, 'ROLE_USER');