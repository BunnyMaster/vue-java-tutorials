-- 创建用户表
CREATE TABLE IF NOT EXISTS users
(
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    username                VARCHAR(50)  NOT NULL UNIQUE,
    password                VARCHAR(100) NOT NULL,
    email                   VARCHAR(100),
    enabled                 BOOLEAN   DEFAULT TRUE,
    account_non_expired     BOOLEAN   DEFAULT TRUE,
    account_non_locked      BOOLEAN   DEFAULT TRUE,
    credentials_non_expired BOOLEAN   DEFAULT TRUE,
    created_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- 创建角色表
CREATE TABLE IF NOT EXISTS authorities
(
    id        BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id   BIGINT      NOT NULL,
    authority VARCHAR(50) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE KEY uk_user_authority (user_id, authority)
);