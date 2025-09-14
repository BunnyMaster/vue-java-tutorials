# 📘 Redis 学习笔记与速查手册

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
```

### 📊 数据类型操作

```sql
-- String 类型
SET counter 1
INCR counter          -- 自增1
GET counter

-- List 类型  
LPUSH mylist "item1"
RPUSH mylist "item2"
LRANGE mylist 0 -1    -- 获取所有元素

-- Hash 类型
HSET user:1000 name "John" age 30
HGETALL user:1000

-- Set 类型
SADD tags "redis" "database" "nosql"
SMEMBERS tags

-- Sorted Set 类型
ZADD leaderboard 100 "player1" 200 "player2"
ZRANGE leaderboard 0 -1 WITHSCORES
```
