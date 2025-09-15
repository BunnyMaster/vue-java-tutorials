# Redis 学习笔记与速查手册

## 基础介绍

> [!WARNING]
>
> - ✅ 设置密码认证
> - ✅ 配置最大内存限制  
> - ✅ 设置合理的内存淘汰策略
> - ✅ Docker 部署时确保 `dir` 与 volume 映射目录一致
> - ✅ 定期备份 RDB 和 AOF 文件
> - ❌ 避免使用默认的 `noeviction` 策略（很危险！）

### 🔗 常用链接速查

| 名称         | 地址                                                         | 说明                  |
| :----------- | :----------------------------------------------------------- | :-------------------- |
| **官网**     | https://redis.io/                                            | Redis 官方网站        |
| **下载**     | https://redis.io/downloads/                                  | 下载安装包            |
| **命令大全** | https://redis.io/docs/latest/commands/                       | 所有 Redis 命令及用法 |
| **配置文档** | https://redis.io/docs/latest/operate/oss_and_stack/management/config/ | 官方配置详解          |
| **配置示例** | https://github.com/redis/redis/blob/unstable/redis.conf      | 最全的配置文件示例    |

### 📝 常用命令备忘

```bash
# 连接 Redis
redis-cli -h host -p port -a password

# 查看配置信息
CONFIG GET *

# 实时监控命令执行
MONITOR

# 查看内存信息
INFO memory

# 查看持久化信息
INFO persistence
```

### ⚙️ 核心配置速查表

#### 🏗️ 1. 基础配置
```ini
# 监听地址 (Docker中通常设为 0.0.0.0)
bind 0.0.0.0

# 是否后台运行 (Docker环境用不到，非Docker设为yes)
daemonize no

# 服务端口 (默认6379)
port 6379
```

#### 🔐 2. 安全配置
```ini
# 设置连接密码 (生产环境必须设置！)
requirepass 你的密码

# 保护模式 (设置了密码可保持开启)
protected-mode yes
```

#### 💾 3. 持久化配置
```ini
# RDB 快照触发条件
save 900 1      # 15分钟内有1个key变化
save 300 10     # 5分钟内有10个key变化  
save 60 10000   # 1分钟内有10000个key变化

# RDB 文件名
dbfilename dump.rdb

# 持久化文件存储目录 (Docker中必须与卷映射目录一致)
dir /data

# 开启 AOF 持久化
appendonly yes

# AOF 同步策略 (everysec是性能与安全的平衡点)
appendfsync everysec
```

#### 🧠 4. 内存管理
```ini
# 最大内存限制 (必须设置，建议系统内存的3/4)
maxmemory 1gb

# 内存淘汰策略 (必须设置，不要用默认的noeviction)
maxmemory-policy volatile-lru  # 最常用：只淘汰有过期时间的key
# allkeys-lru    # 淘汰所有key
# noeviction     # 不淘汰，新写入会报错(危险！)
```

## 命令介绍

### 🔑 Key 操作命令

`keys`：不要再线上使用和主节点使用。

```sql
-- 设置键值（支持多种数据类型）
SET name "Bunny"
SET age 25

-- 批量设置键值
MSET k1 v1 k2 v2 k3 v3

-- 检查key是否存在（存在返回1，不存在返回0）
EXISTS name

-- 设置key过期时间（单位：秒）
EXPIRE key_name 60

-- 查看key剩余过期时间（-1：永久，-2：已过期）
TTL key_name

-- 删除key（返回删除数量）
DEL key1 key2 key3

-- 查看所有key（⚠️ 生产环境慎用，影响性能！）
KEYS *

-- 模糊查询
KEYS a*
```

### 常用命令

> [!TIP]
>
> 1. **数据类型**：INCR/INCRBY 只能用于整型，INCRBYFLOAT 用于浮点型
> 2. **原子性**：所有自增操作都是原子性的，适合高并发场景
> 3. **SETNX 用途**：常用于分布式锁实现和防止重复提交
> 4. **性能考虑**：MSET 比多个 SET 命令更高效，减少网络往返时间
> 5. **内存管理**：String 类型适合存储简单数据，复杂结构建议用 Hash

#### 🎯 命令概述

| 命令            | 说明                           | 使用场景         |
| :-------------- | :----------------------------- | :--------------- |
| **SET**         | 设置或修改 String 类型键值对   | 基本数据存储     |
| **GET**         | 获取 String 类型键值对         | 数据读取         |
| **MSET**        | 批量设置多个 String 键值对     | 批量操作         |
| **INCR**        | 整型 key 自增 1                | 计数器           |
| **INCRBY**      | 整型 key 自增指定步长          | 自定义步长计数   |
| **INCRBYFLOAT** | 浮点型数字自增指定步长         | 浮点数计算       |
| **SETNX**       | Key 不存在时才设置（原子操作） | 分布式锁         |
| **SETEX**       | 设置键值对并指定有效期（秒）   | 带过期时间的数据 |

#### 💻 命令示例与用法

**基本操作**

```sql
-- 设置键值对
SET username "Bunny"
SET age 25

-- 获取值
GET username    -- 返回 "Bunny"
GET age         -- 返回 "25"

-- 批量设置
MSET k1 v1 k2 v2 k3 v3
```

**数字操作**

```sql
-- 设置初始值
SET counter 1
SET score 10.1

-- 自增操作
INCR counter          -- counter 变为 2
INCRBY age 2          -- age 增加 2（如从25变为27）
INCRBYFLOAT score 0.1 -- score 变为 10.2

-- 查看结果
GET counter  -- 返回 "2"
GET score    -- 返回 "10.2"
```

**条件设置**

```sql
-- SETNX：Key不存在时才设置（返回1表示成功，0表示失败）
SETNX new_key "value"    -- 如果new_key不存在则设置
SETNX age 30            -- 如果age已存在，不执行

-- 等效的SET命令写法
SET age 6 NX            -- 与SETNX效果相同

-- SETEX：设置值并指定过期时间（秒）
SETEX temp_data 60 "临时数据"  -- 60秒后自动删除
```

#### 🚀 实用技巧

**计数器场景**

```sql
-- 页面访问计数
INCR page:view:homepage
INCRBY page:view:article:1001 5

-- 实时统计
SETEX user:online:123 300 "active"  -- 5分钟在线状态
```

**分布式锁实现**

```sql
-- 获取锁（设置10秒过期）
SETNX lock:resource 1
SETEX lock:resource 10 1

-- 释放锁
DEL lock:resource
```

**批量操作优化**

```sql
-- 代替多个SET命令，减少网络开销
MSET user:1000:name "John" user:1000:age 30 user:1000:email "john@example.com"
```

### Hash 类型

> [!TIP] 
>
> 1. **内存效率**：Hash 类型适合存储对象，比多个 String 键更节省内存
> 2. **字段数量**：字段过多时，HGETALL 可能返回大量数据，影响性能
> 3. **原子操作**：HINCRBY 是原子性的，适合计数器场景
> 4. **字段过期**：Redis 不支持单个字段的过期时间，只能对整个 Hash 键设置过期
> 5. **使用场景**：适合存储对象类数据（用户信息、商品详情、配置信息等）

| 命令        | 说明               | 使用场景     |
| :---------- | :----------------- | :----------- |
| **HSET**    | 设置 Hash 字段值   | 单个字段操作 |
| **HGET**    | 获取 Hash 字段值   | 读取特定字段 |
| **HMSET**   | 批量设置多个字段值 | 批量初始化   |
| **HMGET**   | 批量获取多个字段值 | 批量读取     |
| **HGETALL** | 获取所有字段和值   | 完整数据导出 |
| **HKEYS**   | 获取所有字段名     | 查看字段列表 |
| **HVALS**   | 获取所有字段值     | 查看值列表   |
| **HINCRBY** | 字段值整数自增     | 计数器场景   |
| **HSETNX**  | 字段不存在时才设置 | 防止覆盖     |

---

#### 💻 命令示例与用法

**基本操作**

```sql
-- 设置单个字段
HSET user:1 name "Lucy"
HSET user:1 age 25

-- 获取单个字段值
HGET user:1 name    -- 返回 "Lucy"
HGET user:1 age     -- 返回 "25"
```

🔄 **批量操作**

```sql
-- 批量设置多个字段
HMSET user:2 name "Lucky" age 8 gender 1

-- 批量获取多个字段值
HMGET user:2 name age    -- 返回 ["Lucky", "8"]
```

📊 **数据查看**

```sql
-- 获取所有字段和值（返回交替的字段名和值）
HGETALL user:2
-- 返回: 1) "name" 2) "Lucky" 3) "age" 4) "8" 5) "gender" 6) "1"

-- 获取所有字段名
HKEYS user:1    -- 返回: 1) "name" 2) "age"

-- 获取所有字段值  
HVALS user:2    -- 返回: 1) "Lucky" 2) "8" 3) "1"
```

🔢 **数字操作**

```sql
-- 整数字段自增（支持正负步长）
HINCRBY user:2 age 2     -- age 从 8 → 10
HINCRBY user:2 age -1    -- age 从 10 → 9

-- 浮点数自增（使用 HINCRBYFLOAT，示例中未展示但常用）
```

#### 🔒 条件设置

```sql
-- 字段不存在时才设置（返回1成功，0失败）
HSETNX user:2 gender 1    -- 如果gender字段不存在则设置
HSETNX user:2 name "Tom"  -- 如果name已存在，不执行
```

#### 🚀 实用技巧

**用户信息存储**

```sql
-- 存储用户完整信息
HMSET user:1000 username "john_doe" email "john@example.com" age 30 status "active"

-- 更新单个字段
HSET user:1000 last_login "2024-01-15"

-- 年龄增长
HINCRBY user:1000 age 1
```

**商品信息管理**

```sql
-- 商品详情
HMSET product:5001 name "iPhone" price 5999 stock 100 category "electronics"

-- 库存管理
HINCRBY product:5001 stock -1    -- 卖出1件，库存-1
HINCRBY product:5001 stock 10    -- 进货10件，库存+10
```

**统计计数**

```sql
-- 页面访问统计
HINCRBY stats:page:home visits 1
HINCRBY stats:page:home users 1

-- 实时更新
HSETNX stats:page:home first_visit "2024-01-15"
```

### List 常用命令

> [!TIP]
>
> 📌 **提示**：List 类型非常适合实现消息队列、最新消息列表、任务队列等场景，BLPOP/BRPOP 提供了可靠的阻塞式消费机制。
>
> 1. **性能考虑**：LPUSH/RPUSH 操作时间复杂度为 O(1)，非常高效
> 2. **阻塞超时**：BLPOP/BRPOP 超时时间为秒级，0表示无限等待
> 3. **空列表处理**：列表为空时，LPOP/RPOP 返回 nil
> 4. **索引范围**：LRANGE 支持负数索引，-1 表示最后一个元素
> 5. **内存管理**：列表元素过多时考虑分片或使用其他数据结构

| 命令       | 说明                         | 使用场景         |
| :--------- | :--------------------------- | :--------------- |
| **LPUSH**  | 向列表左侧插入一个或多个元素 | 队列、栈操作     |
| **LPOP**   | 移除并返回列表左侧第一个元素 | 消费队列消息     |
| **RPUSH**  | 向列表右侧插入一个或多个元素 | 队列操作         |
| **RPOP**   | 移除并返回列表右侧第一个元素 | 消费队列消息     |
| **LRANGE** | 返回指定索引范围内的所有元素 | 查看列表内容     |
| **BLPOP**  | 阻塞式左侧弹出，无元素时等待 | 消息队列阻塞消费 |
| **BRPOP**  | 阻塞式右侧弹出，无元素时等待 | 消息队列阻塞消费 |

---

#### 💻 命令示例与用法

**基本列表操作**

```sql
-- 向左插入多个元素（存储顺序：3 → 2 → 1）
LPUSH students 1 2 3

-- 从左侧弹出元素（返回：3）
LPOP students

-- 向右插入多个元素（存储顺序：4 → 5 → 6）
RPUSH users 4 5 6

-- 从右侧弹出元素（返回：6）
RPOP users

-- 查看列表范围元素（0到-1表示所有元素）
LRANGE students 0 -1
```

**阻塞式操作**

```sql
-- 阻塞等待100秒，从user2左侧弹出元素
BLPOP user2 100

-- 在另一个客户端执行：向user2插入元素
LPUSH user2 111
```

**队列和栈应用**

```sql
-- 队列实现（先进先出 FIFO）
RPUSH task_queue "task1" "task2" "task3"  -- 入队
LPOP task_queue                          -- 出队（返回："task1"）

-- 栈实现（后进先出 LIFO）
LPUSH message_stack "msg1" "msg2" "msg3"  -- 压栈
LPOP message_stack                       -- 弹栈（返回："msg3"）
```

#### 🚀 实用技巧

**消息队列系统**

```sql
-- 生产者：推送任务到队列
LPUSH email_queue "user1@example.com" "订单确认"
LPUSH email_queue "user2@example.com" "欢迎邮件"

-- 消费者：处理队列任务
BLPOP email_queue 30  -- 阻塞30秒等待任务
```

**最新消息列表**

```sql
-- 存储最新10条消息
LPUSH recent_messages "消息1" "消息2" "消息3" "消息4" "消息5"
LTRIM recent_messages 0 9  -- 只保留前10条
```

**阻塞式任务处理**

```sql
-- 工作进程等待任务
BLPOP task_queue 0  -- 无限期等待任务

-- 主进程分配任务
LPUSH task_queue "process_data"
LPUSH task_queue "generate_report"
```

### Set 常用命令

> [!TIP] 
>
> 📌 **提示**：Set 类型非常适合需要唯一性保证的场景，如标签系统、好友关系、唯一访客统计等，集合运算功能强大且实用。
>
> 1. **自动去重**：Set 中元素唯一，重复添加会自动去重
> 2. **无序性**：Set 中的元素是无序的，SMEMBERS 返回顺序不固定
> 3. **性能优秀**：SADD、SREM、SISMEMBER 操作都是 O(1) 时间复杂度
> 4. **内存占用**：大量元素时考虑使用 HyperLogLog 进行基数统计
> 5. **集合运算**：SINTER、SDIFF、SUNION 在大集合上可能较慢

| 命令          | 说明                                    | 使用场景     |
| :------------ | :-------------------------------------- | :----------- |
| **SADD**      | 向 Set 中添加一个或多个元素（自动去重） | 添加唯一元素 |
| **SREM**      | 从 Set 中移除指定元素                   | 删除元素     |
| **SCARD**     | 返回 Set 中元素个数                     | 统计元素数量 |
| **SISMEMBER** | 判断元素是否存在于 Set 中               | 成员检查     |
| **SMEMBERS**  | 获取 Set 中所有元素                     | 查看所有成员 |
| **SINTER**    | 求两个或多个 Set 的交集                 | 共同好友等   |
| **SDIFF**     | 求两个 Set 的差集                       | 差异分析     |
| **SUNION**    | 求两个或多个 Set 的并集                 | 合并去重     |

---

#### 💻 命令示例与用法

**基本 Set 操作**

```sql
-- 向集合添加元素（自动去重，实际存储：a, b, c）
SADD s1 a b c a b c

-- 获取所有元素
SMEMBERS s1          -- 返回: 1) "a" 2) "b" 3) "c"

-- 删除指定元素
SREM s1 a            -- 移除元素"a"

-- 判断元素是否存在
SISMEMBER s1 a       -- 返回: 0 (不存在)
SISMEMBER s1 b       -- 返回: 1 (存在)

-- 查看元素个数
SCARD s1             -- 返回: 2 (剩余b和c)
```

🎯 **社交好友案例**

```sql
-- 设置张三的好友列表
SADD "张三" "李四" "王五" "赵六"

-- 设置李四的好友列表  
SADD "李四" "王五" "麻子" "二狗"

-- 查看张三有几个好友
SCARD "张三"          -- 返回: 3

-- 查看张三和李四的共同好友
SINTER "张三" "李四"  -- 返回: 1) "王五"

-- 查看是张三好友但不是李四好友的人
SDIFF "张三" "李四"   -- 返回: 1) "李四" 2) "赵六"

-- 查看张三和李四的所有好友（去重）
SUNION "张三" "李四"  -- 返回: 1) "李四" 2) "王五" 3) "赵六" 4) "麻子" 5) "二狗"

-- 判断好友关系
SISMEMBER "张三" "李四"  -- 返回: 1 (李四是张三好友)
SISMEMBER "李四" "张三"  -- 返回: 0 (张三不是李四好友)

-- 移除好友关系
SREM "张三" "李四"      -- 从张三好友列表中移除李四
```

#### 🚀 实用技巧

**标签系统**

```sql
-- 文章标签
SADD "article:1001:tags" "redis" "database" "nosql"
SADD "article:1002:tags" "redis" "tutorial" "beginner"

-- 查找同时包含redis和database的文章
SINTER "article:1001:tags" "article:1002:tags"  -- 返回共同标签
```

**用户兴趣匹配**

```sql
-- 用户兴趣标签
SADD "user:1000:interests" "music" "sports" "reading"
SADD "user:1001:interests" "music" "movies" "travel"

-- 查找共同兴趣
SINTER "user:1000:interests" "user:1001:interests"  -- 返回: 1) "music"
```

**唯一值统计**

```sql
-- 网站唯一访客统计
SADD "daily:visitors:2024-01-15" "192.168.1.1" "192.168.1.2" "192.168.1.1"
SCARD "daily:visitors:2024-01-15"  -- 返回: 2 (自动去重)
```

### Sorted Set 常用命令

> [!TIP]
>
> 📌 **提示**：Sorted Set 是Redis中最强大的数据结构之一，非常适合排行榜、评分系统、范围查询等场景，结合WITHSCORES参数可以同时获取分数信息。
>
> 1. **分数精度**：分数是64位浮点数，支持小数运算
> 2. **排名规则**：分数相同按字典序排序
> 3. **性能考虑**：ZRANGEBYSCORE 在大数据集上可能较慢
> 4. **内存占用**：比普通Set占用更多内存，因为要存储分数
> 5. **REV前缀**：在所有命令前加REV即可获得倒序版本

| 命令              | 说明                             | 使用场景         |
| :---------------- | :------------------------------- | :--------------- |
| **ZADD**          | 添加或更新元素及其分数           | 排行榜、评分系统 |
| **ZREM**          | 删除指定元素                     | 移除数据         |
| **ZSCORE**        | 获取元素的分数值                 | 查看具体分数     |
| **ZRANK**         | 获取元素正序排名（从0开始）      | 查看排名位置     |
| **ZREVRANK**      | 获取元素倒序排名                 | 查看倒序排名     |
| **ZCARD**         | 获取有序集合元素个数             | 统计总数         |
| **ZCOUNT**        | 统计指定分数范围内的元素个数     | 分数段统计       |
| **ZINCRBY**       | 元素分数自增指定步长             | 分数调整         |
| **ZRANGE**        | 获取指定排名范围内的元素（正序） | 查看排名段       |
| **ZREVRANGE**     | 获取指定排名范围内的元素（倒序） | 查看倒序排名段   |
| **ZRANGEBYSCORE** | 获取指定分数范围内的元素         | 分数段查询       |
| **ZDIFF**         | 求差集                           | 集合运算         |
| **ZINTER**        | 求交集                           | 集合运算         |
| **ZUNION**        | 求并集                           | 集合运算         |

#### 💻 命令示例与用法

**基本操作**

```sql
-- 添加学生成绩（分数→元素）
ZADD students 76 Miles 78 Jerry 82 Rose 85 Jack 89 Lucy 92 Amy 95 Tom

-- 删除指定学生
ZREM students Tom

-- 获取学生分数
ZSCORE students Amy    -- 返回: "92"

-- 获取正序排名（从0开始）
ZRANK students Rose    -- 返回: 2 (第3名)

-- 获取倒序排名
ZREVRANK students Rose -- 返回: 3 (倒数第4名)

-- 查看元素总数
ZCARD students         -- 返回: 6
```

**分数统计与调整**

```sql
-- 统计80分以下的学生人数
ZCOUNT students 0 80   -- 返回: 2 (Miles和Jerry)

-- 为Amy加2分
ZINCRBY students 2 Amy -- 返回: "94" (新分数)

-- 为Jerry减3分
ZINCRBY students -3 Jerry -- 返回: "75" (新分数)
```

**排名查询**

```sql
-- 查询前三名（正序：分数从低到高）
ZRANGE students 0 2 WITHSCORES
-- 返回: 1) "Jerry" 2) "75" 3) "Miles" 4) "76" 5) "Rose" 6) "82"

-- 查询前三名（倒序：分数从高到低）
ZREVRANGE students 0 2 WITHSCORES
-- 返回: 1) "Amy" 2) "94" 3) "Lucy" 4) "89" 5) "Jack" 6) "85"

-- 查询80分以下的所有学生
ZRANGEBYSCORE students 0 80 WITHSCORES
-- 返回: 1) "Jerry" 2) "75" 3) "Miles" 4) "76"
```

**集合运算**

```sql
-- 创建第二个有序集合
ZADD class2 80 Jerry 85 Rose 90 Bob 88 Lisa

-- 求交集（默认求和分数）
ZINTER 2 students class2
-- 返回共同元素: Jerry, Rose

-- 求并集
ZUNION 2 students class2
-- 返回所有元素（去重）
```

#### 🚀 实用技巧

**排行榜系统**

```sql
-- 游戏玩家排行榜
ZADD leaderboard 1500 "player1" 2000 "player2" 1800 "player3"

-- 获取前十名
ZREVRANGE leaderboard 0 9 WITHSCORES

-- 玩家得分增加
ZINCRBY leaderboard 50 "player1"
```

**时间轴排序**

```sql
-- 新闻热度排行榜（时间戳作为分数）
ZADD news:hot 1642252800 "article1" 1642253100 "article2" 1642253400 "article3"

-- 获取最新文章（时间戳越大越新）
ZREVRANGE news:hot 0 9
```

**价格区间查询**

```sql
-- 商品价格排序
ZADD products 299 "iphone" 599 "ipad" 1299 "macbook" 199 "airpods"

-- 查询500-1000元的产品
ZRANGEBYSCORE products 500 1000 WITHSCORES
```
