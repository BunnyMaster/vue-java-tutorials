# MySQL 存储过程

## 存储过程概述

**定义**：事先经过编译并存储在数据库中的一段 SQL 语句集合。

**优势**：
- ✅ 简化应用开发
- ✅ 减少网络传输
- ✅ 提高数据处理效率
- ✅ 代码封装与重用

**特点**：封装、复用、参数传递、减少网络交互、提升效率。

## 存储过程基础操作

### 查看存储过程
```mysql
-- 查看指定数据库的所有存储过程
SELECT * 
FROM information_schema.ROUTINES 
WHERE ROUTINE_SCHEMA = 'test_auth';
```

### 创建存储过程
```mysql
-- 简单示例：统计用户数量
DELIMITER //
CREATE PROCEDURE p1()
BEGIN
    SELECT COUNT(*) FROM sys_user;
END //
DELIMITER ;
```

### 调用存储过程
```mysql
CALL p1();
```

### 删除存储过程
```mysql
DROP PROCEDURE IF EXISTS p1;
```

## 变量系统

### 系统变量
分为**会话变量**（SESSION）和**全局变量**（GLOBAL）

#### 查看系统变量
```mysql
-- 查看所有会话变量
SHOW SESSION VARIABLES;

-- 模糊查询会话变量
SHOW SESSION VARIABLES LIKE 'auto%';
SELECT @@SESSION.AUTOCOMMIT;
SELECT @@AUTOCOMMIT;  -- 默认会话级别

-- 查看全局变量
SHOW GLOBAL VARIABLES LIKE 'auto%';
SELECT @@GLOBAL.AUTOCOMMIT;
```

#### 设置系统变量
```mysql
-- 设置会话变量
SET SESSION AUTOCOMMIT = 1;
SET @@SESSION.AUTOCOMMIT = 1;

-- 设置全局变量（需要权限）
SET GLOBAL AUTOCOMMIT = 1;
SET @@GLOBAL.AUTOCOMMIT = 1;
```

### 用户变量
- 用户自定义，作用域为当前连接
- 使用时直接 `@变量名`，无需提前声明

#### 赋值方式
```mysql
-- 方式1：SET赋值
SET @var_name = 1;
SET @var_name2 := 'Bunny';  -- 推荐使用 :=
SET @my_name := 'qiu_qiu';

-- 方式2：SELECT INTO赋值
SELECT @my_color := 'red';
SELECT COUNT(*) INTO @my_count FROM auth_db.sys_user;
```

#### 使用用户变量

> [!WARNING]
> 未赋值的用户变量返回 `NULL`，不会报错

```mysql
SELECT @var_name, @var_name2, @my_name;
```

### 局部变量
- 在存储过程/函数中声明使用
- 作用域限于所在的 BEGIN-END 块

#### 声明与使用
```mysql
DELIMITER //
CREATE PROCEDURE p2()
BEGIN
    -- 声明局部变量（必须指定类型）
    DECLARE stu_count INT DEFAULT 0;
    
    -- 赋值局部变量
    SELECT COUNT(*) INTO stu_count FROM auth_db.sys_user;
    
    -- 使用局部变量
    SELECT stu_count;
END //
DELIMITER ;

CALL p2();
```

## 流程控制

### IF-ELSEIF-ELSE 判断
```mysql
DELIMITER //
CREATE PROCEDURE p3()
BEGIN
    DECLARE score INT DEFAULT 58;
    DECLARE result VARCHAR(10);
    
    IF score >= 85 THEN
        SET result := '优秀';
    ELSEIF score >= 60 THEN
        SET result := '及格';
    ELSE
        SET result := '不及格';
    END IF;
    
    SELECT result;
END //
DELIMITER ;
```

### CASE 条件判断
```mysql
DELIMITER //
CREATE PROCEDURE p6(IN month INT)
BEGIN
    DECLARE result VARCHAR(10);
    
    CASE
        WHEN month >= 1 AND month <= 3 THEN 
            SET result = '第一季度';
        WHEN month >= 4 AND month <= 6 THEN 
            SET result = '第二季度';
        WHEN month >= 7 AND month <= 9 THEN 
            SET result = '第三季度';
        WHEN month >= 10 AND month <= 12 THEN 
            SET result = '第四季度';
        ELSE 
            SET result := '非法参数';
    END CASE;

    SELECT CONCAT('输入的月份：', month, '，所属季度：', result);
END //
DELIMITER ;
```

**调用示例**
```mysql
CALL p6(9);   -- 第三季度
CALL p6(16);  -- 非法参数
```

## 循环控制

### WHILE 循环
条件满足时执行循环
```mysql
DELIMITER //
CREATE PROCEDURE p7(IN n INT)
BEGIN
    DECLARE total INT DEFAULT 0;

    WHILE n > 0 DO
        SET total := total + n;
        SET n := n - 1;
    END WHILE;

    SELECT total;
END //
DELIMITER ;
```

**调用示例**
```mysql
CALL p7(10);  -- 返回55 (1+2+...+10)
```

### REPEAT 循环
先执行后判断，直到条件满足时退出
```mysql
DELIMITER //
CREATE PROCEDURE p8(IN n INT)
BEGIN
    DECLARE total INT DEFAULT 0;

    REPEAT
        SET total := total + n;
        SET n := n - 1;
    UNTIL n <= 0 END REPEAT;

    SELECT total;
END //
DELIMITER ;
```

**调用示例**
```mysql
CALL p8(10);  -- 返回55
```

### LOOP 循环
需要手动控制退出条件
```mysql
DELIMITER //
CREATE PROCEDURE p9(IN n INT)
BEGIN
    DECLARE total INT DEFAULT 0;

    -- 定义循环标签
    sum: LOOP
        IF n <= 0 THEN 
            LEAVE sum;  -- 退出循环
        END IF;

        SET total := total + n;
        SET n := n - 1;
    END LOOP sum;

    SELECT total;
END //
DELIMITER ;
```

**调用示例**
```mysql
CALL p9(7);  -- 返回28 (1+2+...+7)
```

### ITERATE 跳过当前循环
```mysql
DELIMITER //
CREATE PROCEDURE p10(IN n INT)
BEGIN
    DECLARE total INT DEFAULT 0;
    DECLARE i INT DEFAULT 0;
    
    loop_label: LOOP
        SET i := i + 1;
        
        -- 跳过偶数
        IF i % 2 = 0 THEN
            ITERATE loop_label;
        END IF;
        
        SET total := total + i;
        
        IF i >= n THEN
            LEAVE loop_label;
        END IF;
    END LOOP;
    
    SELECT total;  -- 奇数和
END //
DELIMITER ;
```

## 参数传递

| 类型      | 含义         | 说明                     |
| :-------- | :----------- | :----------------------- |
| **IN**    | 输入参数     | 调用时传入值（默认类型） |
| **OUT**   | 输出参数     | 可以作为返回值           |
| **INOUT** | 输入输出参数 | 既可传入也可返回         |

### IN 和 OUT 参数
```mysql
DELIMITER //
CREATE PROCEDURE p4(
    IN score INT, 
    OUT result VARCHAR(10)
)
BEGIN
    IF score >= 85 THEN
        SET result := '优秀';
    ELSEIF score >= 60 THEN
        SET result := '及格';
    ELSE
        SET result := '不及格';
    END IF;
END //
DELIMITER ;
```

**调用示例**
```mysql
CALL p4(68, @result);
SELECT @result;  -- 显示'及格'
```

### INOUT 参数
```mysql
DELIMITER //
CREATE PROCEDURE p5(INOUT score DOUBLE)
BEGIN
    SET score := score * 0.5;  -- 打五折
END //
DELIMITER ;
```

**调用示例**
```mysql
SET @score = 198;
CALL p5(@score);
SELECT @score;  -- 显示99
```

## 游标(Cursor)

### 游标基本操作
用来存储查询结果集的数据类型，在存储过程和函数中可以使用游标对结果集进行循环处理。

```mysql
DELIMITER //
CREATE PROCEDURE p11()
BEGIN
    DECLARE user_id INT;
    DECLARE user_name VARCHAR(50);
    DECLARE done INT DEFAULT FALSE;
    
    -- 声明游标
    DECLARE user_cursor CURSOR FOR 
        SELECT id, name FROM sys_user WHERE status = 'active';
    
    -- 声明异常处理
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- 创建临时表存储结果
    CREATE TEMPORARY TABLE temp_user_summary (
        user_id INT,
        user_name VARCHAR(50),
        process_time DATETIME
    );
    
    -- 打开游标
    OPEN user_cursor;
    
    -- 循环处理数据
    read_loop: LOOP
        FETCH user_cursor INTO user_id, user_name;
        IF done THEN
            LEAVE read_loop;
        END IF;
        
        -- 处理逻辑
        INSERT INTO temp_user_summary VALUES (user_id, user_name, NOW());
    END LOOP;
    
    -- 关闭游标
    CLOSE user_cursor;
    
    -- 返回结果
    SELECT * FROM temp_user_summary;
    
    -- 清理临时表
    DROP TEMPORARY TABLE temp_user_summary;
END //
DELIMITER ;
```

### 带参数的游标示例
```mysql
DELIMITER //
CREATE PROCEDURE p12(IN dept_id INT)
BEGIN
    DECLARE emp_id INT;
    DECLARE emp_name VARCHAR(50);
    DECLARE emp_salary DECIMAL(10,2);
    DECLARE done INT DEFAULT FALSE;
    
    -- 声明带参数的游标
    DECLARE emp_cursor CURSOR FOR 
        SELECT id, name, salary FROM employees 
        WHERE department_id = dept_id AND salary > 5000;
    
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;
    
    -- 打开游标
    OPEN emp_cursor;
    
    -- 处理数据
    FETCH emp_cursor INTO emp_id, emp_name, emp_salary;
    WHILE NOT done DO
        -- 业务处理逻辑
        SELECT CONCAT('员工:', emp_name, ', 薪资:', emp_salary) AS employee_info;
        
        FETCH emp_cursor INTO emp_id, emp_name, emp_salary;
    END WHILE;
    
    -- 关闭游标
    CLOSE emp_cursor;
END //
DELIMITER ;
```

## 异常处理

### 基本的异常处理
```mysql
DELIMITER //
CREATE PROCEDURE p13(IN input_value INT)
BEGIN
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        SELECT '发生错误，事务已回滚' AS error_message;
        ROLLBACK;
    END;
    
    START TRANSACTION;
    
    -- 业务逻辑
    IF input_value < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = '输入值不能为负数';
    END IF;
    
    -- 其他操作
    INSERT INTO log_table VALUES (input_value, NOW());
    
    COMMIT;
    SELECT '操作成功' AS result;
END //
DELIMITER ;
```

## 存储过程最佳实践

### 完整模板
```mysql
DELIMITER //
CREATE PROCEDURE template_procedure(
    IN input_param INT,
    OUT output_param VARCHAR(100),
    INOUT inout_param DECIMAL(10,2)
)
BEGIN
    -- 变量声明
    DECLARE local_var INT DEFAULT 0;
    DECLARE done INT DEFAULT FALSE;
    
    -- 异常处理
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        ROLLBACK;
        SET output_param = '执行失败';
    END;
    
    START TRANSACTION;
    
    -- 业务逻辑
    -- ...
    
    COMMIT;
    SET output_param = '执行成功';
END //
DELIMITER ;
```

### 调试技巧
```mysql
-- 在存储过程中添加调试信息
SELECT '调试点1：变量值 = ', @variable_name;

-- 查看存储过程状态
SHOW PROCEDURE STATUS LIKE 'p_name';

-- 查看存储过程定义
SHOW CREATE PROCEDURE p_name;
```
