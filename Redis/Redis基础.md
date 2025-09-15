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

#### 🎯 命令概述

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

---

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

