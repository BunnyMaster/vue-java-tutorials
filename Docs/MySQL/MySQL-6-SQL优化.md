# MySQL SQL 优化笔记

## INSERT优化

> [!NOTE] 
>
> 主键顺序插入性能高于乱序插入。
>
> **数据结构选择**
>
> - 用 `INT` 而不是 `VARCHAR` 存储数字
> - 用 `DATE` 而不是 `VARCHAR` 存储日期
>
> - 日志表可按月分表：`logs_202310`、`logs_202311`

| 优化方向       | 具体做法             | 适用场景       |
| ---------- | ---------------- | ---------- |
| **INSERT** | 批量插入、LOAD DATA   | 大数据导入      |
| **SELECT** | 避免 SELECT *、使用索引 | 高频查询       |
| **索引**     | 区分度高的字段加索引       | 查询慢、字段重复值少 |
| **事务**     | 短事务、及时提交         | 高并发写入      |
| **表结构**    | 合理数据类型、分表        | 大数据表、频繁扫描  |

### 批量插入

**批量插入代替单条插入**

如果要进行插入通常使用`INSERT`进行插入这是毋庸置疑的，如果有部分数据进行插入，将数据改成批量插入，通常批量插入范围在`500~1000`这样。

如果插入数据超过这个范围，将其拆分成多个`INSERT`插入。

```sql
-- 不推荐：多次单条插入
INSERT INTO users (name, age) VALUES ('Alice', 25);
INSERT INTO users (name, age) VALUES ('Bob', 30);

-- 推荐：批量插入
INSERT INTO users (name, age) VALUES 
('Alice', 25),
('Bob', 30),
('Charlie', 35);
```

### 手动事务

如果当前插入超过`500~1000`范围时，通过分批的批量插入涉及事务提交换成手动事务

### 超大数据量

**使用 `LOAD DATA INFILE` 替代 INSERT（大数据量）**

如果一次性插入大批量数据，使用`INSERT`语句插入性能就很低了，此时可以使用MySQL数据库提供的load指令进行插入。

可以通过下面方式进行插入，下面的`,`可以是任意的符号，只要有规律的即可，但符号要统一。

```mysql
1,xxx,xxx,xxx,21点46分
2,xxx,xxx,xxx,21点46分
3,xxx,xxx,xxx,21点46分
4,xxx,xxx,xxx,21点46分
5,xxx,xxx,xxx,21点46分
......
```

#### 执行方式

**禁用索引和约束（大数据插入前）**

```sql
ALTER TABLE users DISABLE KEYS;
-- 执行批量插入...
ALTER TABLE users ENABLE KEYS;
```

✅ 比 INSERT 快 10~100 倍，适合日志、CSV 等批量导入。

```mysql
# 连接客户端
mysql --local-infile -u root -p

# 查看当前全局参数
select @@local_infile;

# 设置全局参数
set global local_infile = 1;

# 执行load之类将准备好的数据加载到表结构中
LOAD DATA LOCAL INFILE '/home/data.csv'
INTO TABLE users
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n';
```

## 主键优化

> [!NOTE]
>
> `BIGINT AUTO_INCREMENT`作为主键，业务唯一性通过额外唯一索引保证。

| 场景         | 影响                   | 优化建议             |
| ------------ | ---------------------- | -------------------- |
| **顺序插入** | 性能最佳，无页分裂     | 使用`AUTO_INCREMENT` |
| **乱序插入** | 频繁页分裂，性能下降   | 避免UUID等随机主键   |
| **长主键**   | 索引占用空间大，查询慢 | 使用简短数值类型     |
| **修改主键** | 成本极高，影响关联数据 | 主键一旦创建不修改   |

### 数据组织方式

#### 索引组织表（IOT）

- InnoDB表中，所有数据**按照主键顺序**存储，这种结构称为**索引组织表**。
- **聚集索引**：B+Tree叶子节点直接包含整行数据。
- 如果表定义了主键，则主键就是聚集索引；如果没有主键，则使用第一个唯一非空索引；否则会自动生成隐藏的ROWID作为主键。

> 💡 理解：表数据本身就是一棵按主键排序的B+树。

### 页分裂与页合并机制

#### 📄 页的基本概念

- 页是InnoDB磁盘管理的最小单元（默认16KB）
- 页可空、半满或全满，每页包含2-N行数据
- 行数据过大时会发生"行溢出"

#### 🔀 页分裂（Page Split）

**顺序插入（最优）**

- 主键顺序插入时，数据按页顺序填充
- 当前页写满后，自动分配到下一页
- 如：`1→2→3→4` 顺序插入，性能最佳

**乱序插入（需优化）**

- 数据根据主键值找到对应页位置
- 如果目标页已满，会：
  1. 创建新页
  2. 将原页部分数据移到新页
  3. 重新排列数据并调整指针
- **性能损耗**：频繁页分裂导致性能下降

> ⚠️ 乱序插入会导致频繁页分裂，影响写入性能。

#### 🔄 页合并（Page Merge）

- 删除记录时只是**标记删除**，空间可复用
- 当页中删除记录达到`MERGE_THRESHOLD`（默认50%）时，InnoDB会尝试与相邻页合并
- **可配置**：可在建表时指定合并阈值

```sql
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50)
) COMMENT 'MERGE_THRESHOLD=40';  -- 设置合并阈值为40%
```

### 主键设计核心原则

| 原则         | 推荐做法             | 避免                   |
| ------------ | -------------------- | ---------------------- |
| **长度最短** | 使用`INT`/`BIGINT`   | 长字符串如UUID、身份证 |
| **顺序插入** | `AUTO_INCREMENT`自增 | 随机值主键             |
| **业务无关** | 代理主键             | 自然主键(如身份证号)   |
| **避免修改** | 主键创建后不变更     | 业务操作修改主键       |

#### ✅ 推荐示例

```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,  -- 自增主键
    order_no VARCHAR(32) UNIQUE,          -- 业务编号另设索引
    user_id INT,
    -- ... 其他字段
);
```

#### ❌ 不推荐示例

```sql
CREATE TABLE users (
    id_card VARCHAR(18) PRIMARY KEY,      -- 过长的主键
    name VARCHAR(50)
    -- 页分裂频繁，查询性能差
);
```

## ORDER BY 优化

### 一、排序执行方式对比

> [!TIP]
>
> 💡 **判断方法**：使用 `EXPLAIN` 查看执行计划的 `Extra` 字段

| 排序类型           | 执行方式                   | 性能       | 显示标识                |
| ------------------ | -------------------------- | ---------- | ----------------------- |
| **Using index**    | 通过索引直接返回有序数据   | ⭐⭐⭐⭐⭐ 高效 | `Extra: Using index`    |
| **Using filesort** | 读取数据后在排序缓冲区排序 | ⭐⭐ 较低    | `Extra: Using filesort` |

### 二、示例分析：联合索引排序

#### 创建测试索引

```sql
CREATE INDEX idx_user_age_phone ON sys_user(age, phone);
-- 索引默认按 ASC 排序：age ASC, phone ASC
```

#### ✅ 有效使用索引的排序（Using index）

```sql
-- 情况1：按索引最左字段排序
EXPLAIN SELECT id, age, phone FROM sys_user ORDER BY age;

-- 情况2：按索引完整顺序排序
EXPLAIN SELECT id, age, phone FROM sys_user ORDER BY age, phone;

-- 情况3：统一降序排序（索引可反向扫描）
EXPLAIN SELECT id, age, phone FROM sys_user ORDER BY age DESC, phone DESC;
```

#### ❌ 导致 filesort 的情况

```sql
-- 情况1：违反最左前缀原则
EXPLAIN SELECT id, age, phone FROM sys_user ORDER BY phone, age;
-- 结果：Using filesort（跳过了age，直接按phone排序）

-- 情况2：排序方向不一致
EXPLAIN SELECT id, age, phone FROM sys_user ORDER BY age ASC, phone DESC;
-- 结果：Using filesort（索引是age ASC, phone ASC）
```

---

### 三、优化策略总结

#### 1. 索引设计原则

- **遵循最左前缀**：排序字段顺序与索引顺序一致
- **覆盖索引**：SELECT 字段尽量包含在索引中
- **方向一致**：多字段排序时保持统一的 ASC/DESC

#### 2. 特殊场景解决方案

**混合排序方向需求**

```sql
-- 如果需要 age ASC, phone DESC 的排序
CREATE INDEX idx_age_asc_phone_desc ON sys_user(age ASC, phone DESC);
-- MySQL 8.0+ 支持指定排序方向的索引
```

#### 3. 无法避免 filesort 时的优化

> [!WARNING]
>
> ⚠️ **注意**：`sort_buffer_size` 是会话级设置，重启后失效，如需永久修改需配置 `my.cnf`

```sql
-- 增大排序缓冲区（默认256K）
SET sort_buffer_size = 2 * 1024 * 1024;  -- 设置为2MB

-- 查询完成后恢复默认值
SET sort_buffer_size = DEFAULT;
```

### 四、实战检查清单

| 检查项            | 是否达标 | 说明                      |
| -------------- | ---- | ----------------------- |
| 排序字段是否使用索引最左前缀 | ✅/❌  | 确保 ORDER BY 字段顺序与索引一致   |
| 排序方向是否统一       | ✅/❌  | 全部 ASC 或全部 DESC         |
| 是否使用覆盖索引       | ✅/❌  | 查询字段包含在索引中              |
| 大数据量排序是否调整缓冲区  | ✅/❌  | 适当增加 `sort_buffer_size` |

#### 优化前后对比示例

```sql
-- ❌ 优化前：产生 filesort
EXPLAIN SELECT * FROM sys_user ORDER BY phone, age;

-- ✅ 优化后：Using index
EXPLAIN SELECT id, age, phone FROM sys_user ORDER BY age, phone;
-- 优化方法：调整排序顺序 + 使用覆盖索引
```

## GROUP BY 优化

### 一、索引设计示例

```sql
-- 创建联合索引（顺序很重要！）
CREATE INDEX idx_user_pro_age_sta ON sys_user(profession, age, status);
```

### 二、分组查询效果分析

#### ✅ 有效使用索引的情况

```sql
-- 情况1：按索引最左字段分组
EXPLAIN SELECT profession, COUNT(*) FROM sys_user GROUP BY profession;
-- 结果：Using index

-- 情况2：按索引前缀字段分组
EXPLAIN SELECT profession, COUNT(*) FROM sys_user GROUP BY profession, age;
-- 结果：Using index

-- 情况3：WHERE条件使用最左字段 + 按后续字段分组
EXPLAIN SELECT age, COUNT(*) FROM sys_user 
WHERE profession = '软件工程' GROUP BY age;
-- 结果：Using index

-- 情况4：完整使用索引前缀分组
EXPLAIN SELECT profession, age, COUNT(*) FROM sys_user 
GROUP BY profession, age;
-- 结果：Using index
```

#### ⚠️ 部分使用索引的情况

```sql
-- 情况：跳过最左字段，直接按非前缀字段分组
EXPLAIN SELECT age, COUNT(*) FROM sys_user GROUP BY age;
-- 结果：Using index; Using temporary
-- 说明：使用了索引但需要临时表辅助
```

### 三、核心优化原则

#### 1. 最左前缀法则（与ORDER BY相同）

- 分组字段顺序必须与索引字段顺序一致
- 不能跳过索引前面的字段直接使用后面的字段分组

#### 2. WHERE + GROUP BY 组合优化

```sql
-- ✅ 高效：WHERE使用索引最左字段
EXPLAIN SELECT age, COUNT(*) FROM sys_user 
WHERE profession = '软件工程' GROUP BY age;

-- ❌ 低效：WHERE条件不符合最左前缀
EXPLAIN SELECT profession, COUNT(*) FROM sys_user 
WHERE age = 25 GROUP BY profession;
```

#### 3. 覆盖索引优化

```sql
-- ✅ 推荐：查询字段都包含在索引中
EXPLAIN SELECT profession, age, COUNT(*) FROM sys_user 
GROUP BY profession, age;

-- ❌ 不推荐：需要回表查询
EXPLAIN SELECT profession, age, name, COUNT(*) FROM sys_user 
GROUP BY profession, age;
-- 结果：Using index; Using temporary（可能还需要Using filesort）
```

### 四、性能问题诊断与解决

#### 识别问题指标

```sql
EXPLAIN SELECT age, COUNT(*) FROM sys_user GROUP BY age;
-- 关注Extra字段：
-- • Using index: 理想情况
-- • Using temporary: 需要临时表（需优化）
-- • Using filesort: 需要额外排序（需优化）
```

#### 解决方案

1. **调整分组顺序**匹配索引

2. **添加缺失的索引字段**到WHERE条件

3. **创建更合适的索引**

   ```sql
   -- 如果经常按age单独分组
   CREATE INDEX idx_user_age ON sys_user(age);
   -- 或者调整联合索引顺序（根据业务需求权衡）
   ```

### 五、最佳实践总结

> [!NOTE]
>
> 🚀 **关键记忆点**：GROUP BY的索引使用规则与ORDER BY完全一致，都遵循**最左前缀法则**！

| 场景                   | 优化策略              | 效果  |
| ---------------------- | --------------------- | ----- |
| **按索引最左字段分组** | 直接使用现有索引      | ⭐⭐⭐⭐⭐ |
| **按索引多字段分组**   | 确保字段顺序一致      | ⭐⭐⭐⭐⭐ |
| **WHERE + GROUP BY**   | WHERE使用索引最左字段 | ⭐⭐⭐⭐  |
| **跳过最左字段分组**   | 创建新索引或调整查询  | ⭐⭐⭐   |

#### 实用检查清单

```sql
-- 优化前诊断
EXPLAIN SELECT [字段] FROM sys_user GROUP BY [分组字段];

-- 优化方法：
-- 1. 检查分组字段顺序是否匹配索引
-- 2. 确认是否使用覆盖索引
-- 3. 考虑添加WHERE条件使用索引最左字段
-- 4. 必要时创建新索引
```

## LIMIT优化

```sql
-- ❌ 性能问题：OFFSET 越大，性能越差
SELECT * FROM tb_sku ORDER BY id LIMIT 200000, 10;
-- 需要先扫描 200010 行，再返回最后 10 行
```

**性能问题根源：**

- `LIMIT 200000,10` 需要先读取 200010 行数据
- 丢弃前 200000 行，只返回最后 10 行
- 数据量越大，排序和扫描代价越高

### 覆盖索引 + 子查询

#### ✅ 方案1：覆盖索引优化（推荐）

```sql
-- ✅ 优化后：先通过覆盖索引定位，再关联查询
EXPLAIN SELECT t.* FROM tb_sku t
INNER JOIN (SELECT id FROM tb_sku ORDER BY id LIMIT 200000, 10) a 
ON t.id = a.id;

-- 执行步骤：
-- 1. 子查询：SELECT id FROM tb_sku ORDER BY id LIMIT 200000, 10（使用索引，快速）
-- 2. 主查询：通过id回表查询具体数据（只查询10条）
```

#### ✅ 方案2：WHERE + ID 范围优化（更高效）

```sql
-- ✅ 如果id是连续自增，记录上一页最大id
SELECT * FROM tb_sku 
WHERE id > 200000  -- 上一页最后一条记录的id
ORDER BY id 
LIMIT 10;

-- 示例：假设已知上一页最后id为 200000
SELECT * FROM tb_sku WHERE id > 200000 ORDER BY id LIMIT 10;
```

#### ✅ 方案3：游标分页（适用于连续分页）

```sql
-- 第一页
SELECT * FROM tb_sku ORDER BY id LIMIT 10;

-- 第二页：使用上一页最后一条记录的id
SELECT * FROM tb_sku WHERE id > 10 ORDER BY id LIMIT 10;

-- 第三页：使用第二页最后一条记录的id  
SELECT * FROM tb_sku WHERE id > 20 ORDER BY id LIMIT 10;
```

### 优化原理对比

| 分页方式                          | 执行过程                  | 性能  | 适用场景               |
| --------------------------------- | ------------------------- | ----- | ---------------------- |
| **传统分页**<br>`LIMIT 200000,10` | 扫描200010行→丢弃200000行 | ⭐⭐    | 小数据量分页           |
| **覆盖索引优化**<br>子查询+JOIN   | 索引定位10个id→回表查询   | ⭐⭐⭐⭐  | 大数据量随机跳页       |
| **游标分页**<br>`WHERE id > ?`    | 直接定位→读取10行         | ⭐⭐⭐⭐⭐ | 连续分页（如无限滚动） |

### 实战示例与效果对比

#### 测试数据：100万条记录的表

```sql
-- ❌ 传统分页（性能差）
SELECT * FROM tb_sku ORDER BY id LIMIT 900000, 10;
-- 执行时间：~2.5秒

-- ✅ 覆盖索引优化（性能提升10倍+）
SELECT t.* FROM tb_sku t
INNER JOIN (SELECT id FROM tb_sku ORDER BY id LIMIT 900000, 10) a 
ON t.id = a.id;
-- 执行时间：~0.2秒

-- ✅ 游标分页（最优性能）
SELECT * FROM tb_sku WHERE id > 900000 ORDER BY id LIMIT 10;
-- 执行时间：~0.01秒
```

### 最佳实践总结

#### 根据业务场景选择方案：

| 场景                                    | 推荐方案                 | 说明             |
| --------------------------------------- | ------------------------ | ---------------- |
| **随机跳页**<br>（如直接跳转到第100页） | 覆盖索引 + 子查询        | 平衡性能和灵活性 |
| **连续浏览**<br>（如无限滚动、下一页）  | 游标分页（WHERE id > ?） | 性能最优         |
| **小数据量**<br>（< 1万条记录）         | 传统LIMIT分页            | 简单直接         |

#### 索引要求：

```sql
-- 确保排序字段有索引
CREATE INDEX idx_sku_id ON tb_sku(id);
-- 如果是其他排序字段
CREATE INDEX idx_sku_create_time ON tb_sku(create_time);
```

> 🚀 **关键建议**：对于大数据量分页，优先考虑**游标分页**设计，如无法避免随机跳页，使用**覆盖索引优化**方案

## COUNT优化

> [!TIP]
>
> count(字段)<count(主键id)<count(1)≈count(\*)，所以尽量使用count(\*)。

MyISAM引擎吧一个表的总行数存在了磁盘上，因此执行`COUNT(*)`的时候会直接返回这个数，效率很高；

InnoDB引擎就很麻烦了，执行时候，需要把数据一行一行的从引擎中读取出来，然后进行计数。

优化方式：自己计数。

### COUNT几种用法

- 聚合函数返回结果集，一行行进行判断，如果COUNT函数的参数不是NULL ，累计值就加1，否则不加，最后返回累计值。
- 用法：`COUNT(*)`、`COUNT(主键)`、`COUNT (字段)`、`COUNT(1)`

#### COUNT(*)

InnoDB引擎并不会把全部字段取出来，而是专门做了优化，不取值，服务层直接按行进行累加。

#### COUNT(主键)

InnoDB引擎会遍历整张表，把每一行的主键id值都取出来，返回给服务层。服务层拿到主键后，直接按行进行累加（主键不可能为null)。

#### COUNT (字段)

没有notnull约束：InnoDB引擎会遍历整张表把每一行的字段值都取出来，返回给服务层，服务层判断是否为null，不为null，计数累加。

有notnull约束：InnoDB引擎会遍历整张表把每一行的字段值都取出来，返回给服务层，直接按行进行累加。

#### COUNT(1)

InnoDB引擎遍历整张表，但不取值。服务层对于返回的每一行，放一个数字“1”进去，直接按行进行累加。

## UPDATE优化

### 一、锁粒度原理：索引决定锁级别

> 💡 **核心规则**：UPDATE的WHERE条件是否使用索引，直接决定锁的粒度（行锁 vs 表锁）

### 二、不同场景下的锁行为分析

#### ✅ 场景1：基于主键更新（行锁 - 最优）

```sql
-- ✅ 使用主键：行级锁（只锁定受影响的行）
UPDATE user SET name = '张三' WHERE id = 1;
-- 锁粒度：只锁定 id=1 这一行，其他行可正常读写
```

#### ✅ 场景2：基于索引字段更新（行锁 - 推荐）

```sql
-- ✅ 使用索引字段：行级锁
UPDATE user SET status = 'active' WHERE email = 'user@example.com';
-- 前提：email字段有索引
-- 锁粒度：只锁定符合条件的行
```

#### ❌ 场景3：基于非索引字段更新（表锁 - 需避免）

```sql
-- ❌ 非索引字段：表级锁（锁定整张表）
UPDATE user SET score = score + 10 WHERE name = '张三';
-- 前提：name字段无索引
-- 锁粒度：锁定整个user表，其他操作阻塞！
```

#### ⚠️ 场景4：索引失效导致表锁

```sql
-- ❌ 索引失效：退化为表锁
UPDATE user SET age = 25 WHERE id + 1 = 10;  -- 对索引字段进行运算
UPDATE user SET age = 25 WHERE name LIKE '%张三%';  -- 前导通配符
-- 即使字段有索引，不当使用也会导致索引失效
```

### 三、锁粒度对比表

| WHERE条件类型      | 锁级别 | 并发性能 | 推荐度     |
| ------------------ | ------ | -------- | ---------- |
| **主键条件**       | 行锁   | ⭐⭐⭐⭐⭐    | ✅ **最佳** |
| **索引字段条件**   | 行锁   | ⭐⭐⭐⭐     | ✅ **推荐** |
| **非索引字段条件** | 表锁   | ⭐        | ❌ **避免** |
| **索引失效条件**   | 表锁   | ⭐        | ❌ **避免** |

### 四、实战优化方案

#### 方案1：确保使用索引

```sql
-- 优化前：非索引字段导致表锁
UPDATE orders SET status = 'shipped' WHERE order_no = '202401001';

-- 优化后：为查询字段添加索引
CREATE INDEX idx_orders_order_no ON orders(order_no);
UPDATE orders SET status = 'shipped' WHERE order_no = '202401001';  -- 行锁
```

#### 方案2：避免索引失效的写法

```sql
-- ❌ 导致索引失效的写法
UPDATE products SET price = price * 0.9 WHERE LEFT(name, 3) = '苹果';
UPDATE products SET stock = stock - 1 WHERE id + 0 = 100;

-- ✅ 优化后的写法
UPDATE products SET price = price * 0.9 WHERE name LIKE '苹果%';
UPDATE products SET stock = stock - 1 WHERE id = 100;
```

#### 方案3：分批更新避免长事务

```sql
-- ❌ 一次性更新大量数据（长事务风险）
UPDATE large_table SET status = 'processed' WHERE create_time < '2024-01-01';

-- ✅ 分批更新（减少锁持有时间）
UPDATE large_table SET status = 'processed' 
WHERE create_time < '2024-01-01' AND id % 10 = 0 LIMIT 1000;
-- 多次执行，每次更新1000条
```

### 五、并发问题与解决方案

#### 问题：死锁风险

```sql
-- 事务1
UPDATE account SET balance = balance - 100 WHERE id = 1;
UPDATE account SET balance = balance + 100 WHERE id = 2;

-- 事务2（相反顺序，可能死锁）
UPDATE account SET balance = balance + 100 WHERE id = 2;
UPDATE account SET balance = balance - 100 WHERE id = 1;
```

#### 解决方案：

1. **统一更新顺序**：所有事务按相同顺序操作记录

2. **使用乐观锁**：

   ```sql
   UPDATE products SET stock = stock - 1, version = version + 1 
   WHERE id = 100 AND version = 5;
   -- 如果版本号不匹配，更新失败
   ```

### 六、最佳实践总结

#### UPDATE优化检查清单：

| 优化点         | 具体做法                       |
| -------------- | ------------------------------ |
| **索引使用**   | WHERE条件必须使用索引字段      |
| **避免表锁**   | 确保索引有效，避免索引失效写法 |
| **减少锁时间** | 大批量更新采用分批处理         |
| **死锁预防**   | 统一操作顺序，使用乐观锁机制   |

#### 性能对比示例：

```sql
-- 测试表：100万条记录

-- ❌ 非索引更新（表锁）：~5秒，阻塞其他操作
UPDATE large_table SET status = 'new' WHERE phone = '13800138000';

-- ✅ 索引更新（行锁）：~0.1秒，不影响并发
UPDATE large_table SET status = 'new' WHERE id = 500000;
```

> 🚀 **关键建议**：UPDATE优化的核心是**确保WHERE条件使用有效的索引**，这是避免表锁、提升并发性能的关键！
