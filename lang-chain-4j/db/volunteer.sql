CREATE DATABASE IF NOT EXISTS `volunteer`;
USE `volunteer`;

CREATE TABLE IF NOT EXISTS `reservation`
(
    `id`                 bigint PRIMARY KEY AUTO_INCREMENT NOT NULL COMMENT '主键Id',
    `name`               VARCHAR(50)                       NOT NULL COMMENT '考生姓名',
    `gender`             VARCHAR(2)                        NOT NULL COMMENT '考生性别',
    `phone`              VARCHAR(20)                       NOT NULL COMMENT '考生手机号',
    `communication_time` DATETIME                          NOT NULL COMMENT '沟通时间',
    `province`           VARCHAR(32)                       NOT NULL COMMENT '考生所处的省份',
    `estimated_score`    INT                               NOT NULL COMMENT '考生预估分数'
)