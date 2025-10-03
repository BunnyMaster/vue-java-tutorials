# MySQL-视图

> [!WARNING]
> **基于阿里规范**
>
> 1. **主要用于查询**：视图最适合简化复杂查询，不适合频繁更新
> 2. **避免通过视图插入数据**：直接操作基表更安全、更清晰
> 3. **检查选项要谨慎**：`WITH CHECK OPTION` 会增加复杂性

## 1. 基本操作

### 1.1 创建视图

```mysql
CREATE OR REPLACE VIEW user_dept AS
SELECT su.id,
       su.username,
       su.nickname,
       su.email,
       su.phone,
       su.password,
       su.avatar,
       su.gender,
       su.birthday,
       su.introduction,
       su.last_login_ip,
       su.last_login_region,
       su.last_login_time,
       su.status,
       su.create_time,
       su.update_time,
       su.create_user,
       su.update_user,
       su.is_deleted,
       sd.id AS dept_id,
       sd.parent_id,
       sd.dept_code,
       sd.dept_name,
       sd.dept_level,
       sd.dept_path,
       sd.order_num,
       sd.status AS dept_status,
       sd.remark
FROM sys_user su
         INNER JOIN sys_user_dept sud ON su.id = sud.user_id AND sud.is_deleted = 0
         LEFT JOIN sys_dept sd ON sud.dept_id = sd.id AND sd.is_deleted = 0
WHERE su.is_deleted = 0
  AND su.status = 1;
```

### 1.2 查看视图

```mysql
-- 查看视图数据
SELECT * FROM user_dept;

-- 查看视图定义
SHOW CREATE VIEW user_dept;

-- 查看所有视图
SHOW FULL TABLES WHERE TABLE_TYPE LIKE 'VIEW';
```

### 1.3 修改视图

> [!NOTE]
> `CREATE OR REPLACE VIEW` 和 `ALTER VIEW` 语法基本一致，主要区别在于：
>
> - `CREATE OR REPLACE`：视图不存在时创建，存在时替换
> - `ALTER VIEW`：只能修改已存在的视图

```mysql
-- 方法1：使用 CREATE OR REPLACE
CREATE OR REPLACE VIEW user_dept AS
SELECT su.id, su.username, su.email, sd.dept_name
FROM sys_user su
INNER JOIN sys_user_dept sud ON su.id = sud.user_id
LEFT JOIN sys_dept sd ON sud.dept_id = sd.id
WHERE su.is_deleted = 0;

-- 方法2：使用 ALTER VIEW
ALTER VIEW user_dept AS
SELECT su.id, su.username, su.email, sd.dept_name
FROM sys_user su
INNER JOIN sys_user_dept sud ON su.id = sud.user_id
LEFT JOIN sys_dept sd ON sud.dept_id = sd.id
WHERE su.status = 1;
```

### 1.4 删除视图

```mysql
DROP VIEW IF EXISTS user_dept;
```

## 2. 视图检查选项

> [!NOTE]
> 当通过视图插入或更新数据时，如果有检查选项，MySQL会验证操作是否符合视图的WHERE条件，如果违反则拒绝执行。

### 2.1 检查选项类型

| 选项类型                     | 说明                                |
| ---------------------------- | ----------------------------------- |
| `WITH CASCADED CHECK OPTION` | 严格检查，会检查所有底层视图的条件  |
| `WITH LOCAL CHECK OPTION`    | 宽松检查，只检查当前视图的条件      |
| 无检查选项                   | 不验证插入/更新数据是否符合视图条件 |

### 2.2 创建带检查选项的视图

```mysql
CREATE OR REPLACE VIEW test_view AS
SELECT id, name, age
FROM view_test
WHERE age > 18
WITH CASCADED CHECK OPTION;
```

### 2.3 插入数据测试

**无法插入的情况（年龄不满足条件）：**

```mysql
-- 报错：CHECK OPTION failed 'database_name.test_view'
INSERT INTO test_view (id, name, age) VALUES 
(1, '张三', 8),
(2, '李四', 9);
```

**可以插入的情况：**

```mysql
INSERT INTO test_view (id, name, age) VALUES 
(1, '张三', 19),
(2, '李四', 20);
```

### 2.4 删除视图数据

```mysql
DELETE FROM test_view WHERE id = 1;
```

## 3. 生产环境最佳实践

```mysql
-- ✅ 推荐：创建只读视图（用于查询）
CREATE OR REPLACE VIEW user_dept_readonly AS
SELECT su.id, su.username, sd.dept_name
FROM sys_user su
INNER JOIN sys_user_dept sud ON su.id = sud.user_id
LEFT JOIN sys_dept sd ON sud.dept_id = sd.id
WHERE su.is_deleted = 0
  AND su.status = 1;

-- ❌ 避免：创建可更新视图（除非有特殊需求）
```

## 4. 视图可更新性条件

要使视图可更新，视图中的行与基础表中的行之间必须存在一对一的关系。如果视图包含以下任何一项，则该视图不可更新：

### 4.1 不可更新的情况

- **聚合函数**：`DISTINCT`, `COUNT`, `MAX`, `MIN`, `SUM`, `AVG`等
- **GROUP BY 或 HAVING** 子句
- **UNION 或 UNION ALL**
- **子查询在SELECT列表中**
- **连接多个表**（某些简单情况除外）
- **不可更新的子查询**

### 4.2 检查视图是否可更新

```mysql
SELECT TABLE_NAME, IS_UPDATABLE 
FROM INFORMATION_SCHEMA.VIEWS 
WHERE TABLE_SCHEMA = 'your_database_name';
```

## 5. 视图的作用与优势

### 5.1 简化操作

视图可以简化复杂查询，用户只需操作视图而不必关心底层表结构和关联关系。

### 5.2 数据安全

通过视图可以控制用户访问的数据范围，实现行列级别的权限控制。

### 5.3 逻辑独立性

视图可以帮助应用程序屏蔽底层表结构变化带来的影响。

### 5.4 数据抽象

为不同的用户或应用提供定制化的数据视角。

> [!IMPORTANT] 
> **尽管视图有这些优势，但在互联网架构中仍应谨慎使用，优先考虑在应用层实现业务逻辑。**
