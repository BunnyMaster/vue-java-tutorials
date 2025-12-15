# MySQL 索引

> [!TIP]
> **索引使用建议：**
> - 索引应建立在**查询频繁、更新较少**的字段上。
> - 避免对经常更新的表创建过多索引。
> - 多列索引要注意**最左前缀原则**。
> - 全文索引适用于文本搜索，但需要注意字符集和排序规则。
> - 定期分析查询语句，使用 `EXPLAIN` 查看索引使用情况。

## 一、索引概述

索引是对数据库表中一列或多列的值进行排序的一种数据结构，使用索引可以**快速访问**数据库表中的特定信息，从而**加快对表中记录的查找或排序**。

**数据结构可视化网站：** https://www.cs.usfca.edu/~galles/visualization/Algorithms.html

## 二、索引的优缺点

### ✅ 优点

- **加速查询**：显著减少数据检索时间
- **保证唯一性**：唯一索引确保数据唯一性
- **优化连接**：加快表连接操作速度
- **提升排序分组**：改善ORDER BY和GROUP BY性能

### ❌ 缺点

- **空间占用**：索引需要额外的存储空间
- **维护成本**：增删改操作需要更新索引，影响写入性能
- **设计复杂度**：不合理的索引反而降低性能

## 三、索引分类

| 索引类型     | 说明                                                     |
| -------- | ------------------------------------------------------ |
| **普通索引** | 最基本的索引，无任何限制，可在任何数据类型上创建                               |
| **唯一索引** | 使用 `UNIQUE` 约束，索引值必须唯一，主键是特殊的唯一索引                      |
| **全文索引** | 使用 `FULLTEXT`，只能用于 `CHAR`/`VARCHAR`/`TEXT` 类型，适用于大文本搜索 |
| **单列索引** | 只针对一个字段建立的索引，可以是上述任意类型                                 |
| **多列索引** | 针对多个字段建立的索引，查询时必须遵循**最左前缀原则**                          |
| **空间索引** | 使用 `SPATIAL`，只能用于空间数据类型，仅 MyISAM 引擎支持，且字段不能为空          |

## 四、查看索引

### 1. 查看所有索引（系统视图）
```sql
SELECT * FROM mysql.innodb_index_stats;
```

### 2. 查看指定数据库中的索引
```sql
SELECT *
FROM mysql.innodb_index_stats
WHERE database_name = 'test_auth';
```

### 3. 查看某表中所有索引
```sql
SELECT *
FROM mysql.innodb_index_stats
WHERE database_name = 'test_auth' AND table_name LIKE '%log%';
```

### 4. 查看表中索引（SHOW 语句）
```sql
SHOW INDEX FROM test_auth.sys_user;
```

### 5. 查看索引使用情况（性能分析）
```sql
EXPLAIN SELECT * FROM table_name WHERE condition;
```

## 五、创建索引

> [!NOTE]
> **索引创建最佳实践：**
> 1. **设计阶段**：根据业务查询模式设计合适的索引
> 2. **开发阶段**：使用 `EXPLAIN` 验证索引使用情况
> 3. **测试阶段**：通过性能测试验证索引效果
> 4. **生产环境**：定期监控和优化索引性能
> 5. **维护阶段**：定期清理无用索引，重建碎片化索引
>
> 推荐使用联合索引

### 1. 创建普通索引
```sql
CREATE INDEX index_name ON table_name(column_name);
```

### 2. 创建唯一索引
```sql
CREATE UNIQUE INDEX index_name ON table_name(column_name);
```

### 3. 创建全文索引

> [!NOTE]
> **注意事项：**
> - 只有 `CHAR`、`VARCHAR`、`TEXT` 类型字段可以建立全文索引
> - 在数据量较大时，先插入数据再创建全文索引比先创建索引再插入数据效率更高
> - 使用前需确认 MySQL 版本、存储引擎和字符集支持全文索引

```sql
CREATE FULLTEXT INDEX index_name ON table_name(column_name);
```

查看全文索引配置：
```sql
SHOW VARIABLES LIKE '%ft%';
```

重点关注：
- `ft_min_word_len`：最小词元长度（默认4）
- `ft_max_word_len`：最大词元长度（默认取决于版本）

### 4. 创建联合索引

创建联合索引时一定要注意位置，遵循**最左前缀法则**

```sql
CREATE INDEX index_name ON table_name(col1, col2, col3);
```

**最左前缀原则示例：**

```sql
-- 创建组合索引
CREATE INDEX idx_phone_name ON test_auth.sys_user (phone, username);

-- 以下查询会使用索引：
SELECT * FROM sys_user WHERE phone = 'xxx';
SELECT * FROM sys_user WHERE phone = 'xxx' AND username = 'xxx';
SELECT * FROM sys_user WHERE username = 'xxx' AND phone = 'xxx'; -- AND条件优化

-- 以下查询不会使用该组合索引：
SELECT * FROM sys_user WHERE username = 'xxx'; -- 不满足最左前缀
SELECT * FROM sys_user WHERE phone = 'xxx' OR username = 'xxx'; -- OR条件不使用组合索引
```

### 5. 创建空间索引（仅 MyISAM）
```sql
CREATE SPATIAL INDEX index_name ON table_name(column_name);
```

### 6. 修改表结构添加索引
```sql
ALTER TABLE table_name ADD INDEX index_name(column_name);
ALTER TABLE table_name ADD UNIQUE INDEX index_name(column_name);
ALTER TABLE table_name ADD FULLTEXT INDEX index_name(column_name);
```

### 7. 创建前缀索引

当字段类型为字符串时(varchar、text)，有时需要索引很长的字符串，这会让索引变得很大。此时可以只将字符串的一部分前缀建立索引，节约索引空间。

#### 前缀长度选择原则

> [!NOTE]
>
> 选择性越高越好，最高为1（即100%），表示每个截取后的前缀都是唯一的，和完整的列一样好
>
> 但不代表就一定要选择第一个为1（即100%）

根据索引的选择性决定，选择性是不重复的索引值（基数）和数据表记录总数的比值。

**计算方法：**

```sql
-- 计算完整字段的选择性
SELECT COUNT(DISTINCT email) / COUNT(*) FROM sys_user;

-- 计算不同前缀长度的选择性，当前我的数据库输出为1
SELECT COUNT(DISTINCT SUBSTRING(email, 1, 15)) / COUNT(*) FROM sys_user;
```

#### 最佳实践

**选择第一个达到或超过 0.95（或 0.9） 的长度。**

- 如果 `selectivity_14 = 0.93`, `selectivity_15 = 1.0`，那么**选择15**是更稳妥的，因为它保证了唯一性。
- 如果 `selectivity_14 = 0.98`, `selectivity_15 = 1.0`，那么需要权衡：
  - **选14**： 索引更小，写入更快。牺牲了2%的选择性（即每100行有2个可能的前缀冲突），在数万行的表中可能影响不大。
  - **选15**： 索引最大程度有效，但比14长1个字节。对于超大型表，这1个字节的累积开销也需要考虑。

#### 选择性分析示例

| 前缀长度 | 选择性 | 性价比评价     |
| -------- | ------ | -------------- |
| 8        | 87.80% | 太低           |
| 9        | 94.55% | 接近标准       |
| 10       | 98.09% | ✅ **高性价比** |
| 11       | 99.41% | ✅ 优秀         |
| 12       | 99.82% | 很好           |
| 13       | 99.93% | 🥇 **最佳推荐** |
| 14       | 99.98% | 近乎完美       |
| 15       | 100%   | 完美           |

#### 创建前缀索引

```sql
-- 推荐使用长度13
CREATE INDEX idx_email_prefix ON table_name(email(13));
```

## 六、删除索引

### 方式一：DROP INDEX
```sql
DROP INDEX index_name ON table_name;
```

### 方式二：ALTER TABLE
```sql
ALTER TABLE table_name DROP INDEX index_name;
```

## 七、索引优化建议

1. **选择合适的数据类型**：使用较小的数据类型，整数类型比字符类型索引效率更高
2. **避免过度索引**：每个额外的索引都会增加写操作的开销
3. **使用前缀索引**：对于长字符串字段，可以只索引前几个字符
   
   ```sql
   CREATE INDEX index_name ON table_name(column_name(10));
   ```
4. **定期分析索引使用情况**：使用性能监控工具监控索引效果
5. **考虑索引选择性**：选择性高的字段（唯一值多的字段）更适合建索引

## 八、性能监控与分析

### 1. 查询操作统计

根据输出结果可以判断当前数据库是以查询还是增删改为主，方便我们知道是否要对其做优化。

```sql
SHOW GLOBAL STATUS LIKE 'Com_______';
```

返回结果说明：
- `Com_select`：SELECT查询执行次数
- `Com_insert`：INSERT语句执行次数
- `Com_update`：UPDATE语句执行次数
- `Com_delete`：DELETE语句执行次数
- `Com_begin`：事务开始次数
- `Com_commit`：事务提交次数
- `Com_rollback`：事务回滚次数
- `Com_change_db`：数据库切换次数

### 2. 慢查询日志配置

**配置文件位置：** `/etc/my.cnf` 或 `/etc/mysql/my.cnf`

```properties
# 开启MySQL慢查询日志开关
slow_query_log = 1

# 设置慢日志的时间为2秒，SQL语句执行时间超过2秒就会视为慢查询
long_query_time = 2

# 指定慢查询日志文件路径（可选）
slow_query_log_file = /var/log/mysql/slow.log
```

**查看慢查询日志位置：**
```sql
SHOW VARIABLES LIKE 'slow_query_log_file';
```

### 3. SQL性能分析（PROFILING）

**检查是否支持 profiling：**
```sql
SELECT @@have_profiling;
```

**开启 profiling：**
```sql
SET profiling = 1;
```

**查看所有查询的性能分析：**
```sql
SHOW PROFILES;
```

**查看特定查询的详细分析：**
```sql
SHOW PROFILE FOR QUERY [query_id];
```

**查看CPU耗费情况：**
```sql
SHOW PROFILE CPU FOR QUERY [query_id];
```

**其他分析选项：**
```sql
SHOW PROFILE ALL FOR QUERY [query_id];          -- 所有信息
SHOW PROFILE BLOCK IO FOR QUERY [query_id];     -- 块IO操作
SHOW PROFILE CONTEXT SWITCHES FOR QUERY [query_id]; -- 上下文切换
SHOW PROFILE IPC FOR QUERY [query_id];          -- IPC操作
SHOW PROFILE MEMORY FOR QUERY [query_id];       -- 内存使用
SHOW PROFILE PAGE FAULTS FOR QUERY [query_id];  -- 页面错误
SHOW PROFILE SOURCE FOR QUERY [query_id];       -- 源代码信息
SHOW PROFILE SWAPS FOR QUERY [query_id];        -- 交换次数
```

### 4. EXPLAIN 执行计划分析

**EXPLAIN 输出字段详解：**

#### id
select查询的序列号，表示查询中执行的select子句或者是操作表的顺序。
- id相同：执行顺序从上到下
- id不同：值越大，越先执行

#### select_type
表示SELECT的类型，常见取值：
- `SIMPLE`：简单查询，不使用表连接或者子查询
- `PRIMARY`：主查询，即外层的查询
- `UNION`：UNION中的第二个或者后面的查询语句
- `SUBQUERY`：SELECT/WHERE之后包含了子查询
- `DERIVED`：派生表的SELECT
- `UNION RESULT`：UNION的结果

#### type（连接类型性能排名）

**✅ 最优级别**

| type     | 说明                                 | 性能   |
| -------- | ------------------------------------ | ------ |
| `system` | 系统表，只有一行数据                 | 🏆 最佳 |
| `const`  | 通过主键或唯一索引查询，最多返回一行 | 🏆 最佳 |

**👍 优秀级别**

| type       | 说明                         | 性能   |
| ---------- | ---------------------------- | ------ |
| `eq_ref`   | 关联查询时使用主键或唯一索引 | ⭐ 优秀 |
| `ref`      | 使用普通索引查询             | ⭐ 良好 |
| `fulltext` | 全文索引查询                 | ⭐ 良好 |

**⚠️ 可接受级别**

| type              | 说明                      | 性能   |
| ----------------- | ------------------------- | ------ |
| `ref_or_null`     | 类似ref，但包含NULL值查询 | 📊 一般 |
| `index_merge`     | 索引合并优化              | 📊 一般 |
| `unique_subquery` | 子查询中使用唯一索引      | 📊 一般 |
| `index_subquery`  | 子查询中使用普通索引      | 📊 一般 |

**🚨 需要优化级别**

| type    | 说明         | 性能     |
| ------- | ------------ | -------- |
| `range` | 范围扫描索引 | ⚠️ 需关注 |
| `index` | 全索引扫描   | ⚠️ 需优化 |

**❌ 最差级别**

| type  | 说明     | 性能       |
| ----- | -------- | ---------- |
| `ALL` | 全表扫描 | ❌ 急需优化 |

#### possible_keys
可能用到的索引，一个或多个。

#### key
实际使用的索引，如果为NULL，则表示没有使用索引。

#### key_len
表示索引中使用的字节数，该值为索引字段最大可能长度，并非实际使用长度，在不损失精确性的前提下，长度越短越好。

#### rows
MySQL认为必要执行查询的行数，在InnoDB引擎中是一个预估值。

#### filtered
表示返回结果的行数占需要读取行数的百分比，filtered的值越大越好。

#### extra
额外的执行信息，常见值：
- `NULL`：回表查询
- `Using index`：使用覆盖索引
- `Using where`：使用WHERE过滤
- `Using temporary`：使用临时表
- `Using filesort`：使用文件排序
- `Using join buffer`：使用连接缓冲

## 九、常见问题排查

### 1. 索引失效场景分析

#### 最左前缀法则
如果索引引了多列（联合索引），要遵循最左法则。查询从索引最左列开始，并且不跳过索引中的列。

```mysql
-- 创建联合索引
CREATE INDEX idx_profession_age_status ON tb_user(profession, age, status);

-- ✅ 生效：包含最左列 profession
EXPLAIN SELECT * FROM tb_user WHERE profession = '软件工程' AND age = 31 AND status = 'O';
EXPLAIN SELECT * FROM tb_user WHERE profession = '软件工程' AND age = 31;
EXPLAIN SELECT * FROM tb_user WHERE profession = '软件工程';

-- ❌ 失效：不包含最左列 profession
EXPLAIN SELECT * FROM tb_user WHERE age = 31 AND status = 'O';
EXPLAIN SELECT * FROM tb_user WHERE status = 'O';

-- ⚠️ 部分失效：跳过 age 列，status 索引失效
EXPLAIN SELECT * FROM tb_user WHERE profession = '软件工程' AND status = 'O';
```

#### 范围查询影响
联合索引中，出现范围查询(<,>)，范围查询右侧的列索引失效

```mysql
-- ❌ status 索引失效（范围查询右侧）
EXPLAIN SELECT * FROM tb_user WHERE profession = '软件工程' AND age > 30 AND status = 'O';

-- ✅ 使用 >= 可避免索引失效（业务允许情况下）
EXPLAIN SELECT * FROM tb_user WHERE profession = '软件工程' AND age >= 30 AND status = 'O';
```

#### 运算操作导致失效
不要在索引列上进行运算操作，索引将失效。

```mysql
-- ❌ 使用函数导致索引失效
EXPLAIN SELECT * FROM tb_user WHERE SUBSTRING(phone, 10, 2) = '15';

-- ✅ 避免在索引列上使用函数
EXPLAIN SELECT * FROM tb_user WHERE phone LIKE '%15';
```

#### 字符串类型不加引号
字符串类型字段使用时，不加引号，索引失效。

```mysql
-- ❌ 数字类型比较，索引失效
EXPLAIN SELECT * FROM tb_user WHERE phone = 17799990015;

-- ✅ 字符串类型比较，索引生效
EXPLAIN SELECT * FROM tb_user WHERE phone = '17799990015';
```

#### 模糊查询影响
- 尾部模糊匹配：索引不会失效
- 头部模糊匹配：索引失效

```mysql
-- ✅ 尾部模糊匹配，索引生效
EXPLAIN SELECT * FROM tb_user WHERE phone LIKE '1779999%';

-- ❌ 头部模糊匹配，索引失效
EXPLAIN SELECT * FROM tb_user WHERE phone LIKE '%99990015';

-- ❌ 前后都模糊匹配，索引失效
EXPLAIN SELECT * FROM tb_user WHERE phone LIKE '%9999%';
```

#### OR连接条件影响
如果使用`OR`进行分割条件，只有所有条件都有索引时才会使用索引。

```mysql
-- ❌ age 无索引，导致整个查询索引失效
EXPLAIN SELECT * FROM tb_user WHERE phone = '17799990017' OR age = 23;

-- ✅ 所有条件都有索引，索引生效
EXPLAIN SELECT * FROM tb_user WHERE phone = '17799990017' OR profession = '软件工程';
```

#### 数据分布影响
如果MySQL评估使用索引比全表扫描慢，则不使用索引。
MySQL可能认为，当前的数据库全表扫描会更快。

```mysql
-- 数据分布影响索引选择
EXPLAIN SELECT * FROM tb_user WHERE status IS NULL;
EXPLAIN SELECT * FROM tb_user WHERE status IS NOT NULL;

-- MySQL会根据数据分布决定是否使用索引
```

### 2. 索引提示（SQL Hints）

SQL提示是优化数据库的一个重要手段，通过在SQL语句中加入人为提示来达到优化目的。

```mysql
-- 建议使用指定索引（MySQL可能会拒绝）
EXPLAIN SELECT * FROM test_auth.sys_user USE INDEX (idx_page_query) 
WHERE is_deleted = 0 AND username = 'tsuism';

-- 忽略指定索引
EXPLAIN SELECT * FROM test_auth.sys_user IGNORE INDEX (idx_user_login_time_sorted) 
WHERE is_deleted = 0 AND username = 'tsuism';

-- 强制使用指定索引
EXPLAIN SELECT * FROM test_auth.sys_user FORCE INDEX (idx_page_query) 
WHERE is_deleted = 0 AND username = 'tsuism';
```

### 3. 覆盖索引与回表查询

#### 回表查询
当查询的列不在索引中时，MySQL需要根据索引找到主键，再通过主键回表查询完整数据行，这个过程称为回表查询。

#### 覆盖索引
覆盖索引是指查询使用了索引，并且需要返回的列在该索引中已经全部能够找到，不需要回表查询。

**EXPLAIN中的Extra字段说明：**

> [!TIP]
>
> 每个数据库的版本不同所以看到的信息也可能不同，下面只是参考。
>
> `Using where; Using index`性能更优

- `Using index condition`：查找使用了索引，但需要回表查询
- `Using where; Using index`：查找使用了索引，且所有数据都在索引列中，不需要回表查询

#### 示例优化
表结构：`(id, username, password, status)`

优化SQL：`SELECT id, username, password FROM tb_user WHERE username = 'xxx';`

**解决方案：** 为 `username` 和 `password` 建立联合索引
```sql
CREATE INDEX idx_username_password ON tb_user(username, password);
```

这样查询时只需要使用索引就能获取所有需要的数据，避免回表查询。

### 4. 常规问题排查

1. **索引未生效**：使用 `EXPLAIN` 分析查询执行计划
2. **索引选择不当**：检查是否满足最左前缀原则
3. **索引碎片化**：定期优化表或重建索引
   ```sql
   OPTIMIZE TABLE table_name;
   ```
4. **统计信息过期**：使用 `ANALYZE TABLE` 更新统计信息
   ```sql
   ANALYZE TABLE table_name;
   ```
5. **锁竞争问题**：监控锁等待情况，优化事务设计

## 十、最佳实践总结

1. **设计合理的索引策略**：根据业务查询模式设计索引
2. **定期监控性能**：使用慢查询日志和EXPLAIN分析
3. **避免索引过度**：每个索引都有维护成本
4. **使用覆盖索引**：减少回表操作
5. **定期维护**：重建碎片化索引，更新统计信息
6. **避免索引失效**：注意查询语句写法，避免上述索引失效场景
7. **合理使用索引提示**：在必要时使用USE/IGNORE/FORCE INDEX
8. **优化查询语句**：尽量使用覆盖索引，减少SELECT * 的使用

### 设计原则

1. 针对于数据量较大，且查询比较频繁的表建立索引。
2. 针对于常作为查询条件（where）、排序（order by)、分组（group by）操作的字段建立索引。
3. 尽量选择区分度高的列作为索引，尽量建立唯一索引，区分度越高，使用索引的效率越高。尽量不要使用`gender`、`status`这种因为区分度不高
4. 如果是字符串类型的字段，字段的长度较长，可以针对于字段的特点，建立前缀索引。
5. 尽量使用联合索引，减少单列索引，查询时，联合索引很多时候可以覆盖索引，节省存储空间，避免回表，提高查询效率。
6. 要控制索引的数量，索引并不是多多益善，索引越多，维护索引结构的代价也就越大，会影响增删改的效率。比如当前表只涉及插入不涉及查询，那么就不要建立索引，相反如果建立越多的索引增删改效率反而降低，还会浪费磁盘空间
7. 如果索引列不能存储NULL值，请在创建表时使用NOTNULL约束它。当优化器知道每列是否包含NULL值时，它可以更好地确定哪个索引最有效地用于查询。
