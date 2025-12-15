## RDB和AOF

Redis提供了两种持久化机制：**RDB**（快照）和**AOF**（日志），确保数据在重启后能够恢复。

RDB是Redis的数据快照文件，通过定时将内存中的数据保存到磁盘实现持久化。

#### 基本命令
```bash
# 后台执行RDB快照（推荐）
bgsave

# 前台执行RDB快照（会阻塞所有命令）
save
```

#### 配置文件示例
```bash
# RDB触发条件配置
save 900 1    # 900秒内至少1个key被修改
save 300 10   # 300秒内至少10个key被修改  
save 60 10000 # 60秒内至少10000个key被修改

# RDB文件配置
dbfilename dump.rdb  # RDB文件名
dir ./               # 保存目录
rdbcompression yes   # 是否压缩（建议开启）
```

#### 工作原理
1. **fork子进程**：执行`bgsave`时，主进程fork一个子进程
2. **写时复制**：子进程与主进程共享内存，采用写时复制技术
3. **生成RDB**：子进程将内存数据写入临时RDB文件
4. **替换文件**：完成后替换旧的RDB文件

> [!WARNING]
> 写时复制可能导致内存占用翻倍，建议为Redis预留足够内存空间。

#### RDB优缺点
| 优点       | 缺点                       |
| ---------- | -------------------------- |
| 文件体积小 | 数据可能丢失（两次快照间） |
| 恢复速度快 | fork操作耗时，可能阻塞     |
| 适合备份   | 频繁保存影响性能           |

### 📝 AOF（Append Only File）

AOF通过记录所有写命令来实现持久化，数据完整性更高。

#### 开启配置
```bash
# 开启AOF
appendonly yes
appendfilename "appendonly.aof"
```

#### 刷盘策略
```bash
# 三种策略选择
appendfsync always    # 每个命令都同步刷盘（最安全）
appendfsync everysec  # 每秒刷盘（默认，平衡点）
appendfsync no        # 由操作系统决定（性能最好）
```

#### AOF重写
为避免AOF文件过大，Redis支持AOF重写：
```bash
# 手动触发重写
bgrewriteaof

# 自动重写配置
auto-aof-rewrite-percentage 100  # 比上次增长100%时触发
auto-aof-rewrite-min-size 64mb   # 最小64MB才触发
```

### 🔄 RDB与AOF对比

| 特性       | RDB                          | AOF              |
| ---------- | ---------------------------- | ---------------- |
| 持久化方式 | 定时内存快照                 | 记录每次写命令   |
| 数据完整性 | 可能丢失数据                 | 相对完整         |
| 文件大小   | 小（有压缩）                 | 大               |
| 恢复速度   | 快                           | 慢               |
| 资源占用   | 高（CPU/内存）               | 低（磁盘IO）     |
| 适用场景   | 可容忍数据丢失，追求快速启动 | 数据安全性要求高 |

**生产建议**：通常同时开启RDB和AOF，利用RDB快速恢复，AOF保证数据完整性。

## 🔗 主从同步

### 同步机制

#### 全量同步
- **触发条件**：
  - Slave首次连接Master
  - Slave数据落后过多，offset被覆盖
- **过程**：
  1. Master生成RDB发送给Slave
  2. 期间写命令记录到`repl_baklog`
  3. Slave加载RDB后，同步`repl_baklog`中的命令

#### 增量同步
- **触发条件**：Slave短暂断开后重连，offset仍有效
- **过程**：Slave提交offset，Master发送offset后的命令

### 优化建议
1. **启用无磁盘复制**：
   ```ini
   repl-diskless-sync yes
   ```
2. **控制内存占用**：减少RDB大小
3. **增大缓冲区**：提高`repl_baklog`大小
4. **快速故障恢复**：监控Slave状态

## 🛡️ 哨兵模式（Sentinel）

### 主要功能
1. **监控**：持续检查主从节点状态
2. **自动故障转移**：主节点故障时自动提升从节点
3. **通知**：向客户端推送集群状态变更

### 服务状态监控
- **主观下线**：单个Sentinel认为节点不可用
- **客观下线**：多数Sentinel（超过quorum）认为节点不可用
  ```bash
  # 配置示例
  sentinel monitor mymaster 127.0.0.1 6379 2
  # quorum=2，需要2个Sentinel同意才判定客观下线
  ```

### 故障转移流程
1. **选举Leader Sentinel**：负责执行故障转移
2. **选择新Master**：依据：
   - 与旧Master断开时间（不能太长）
   - slave-priority值（越小优先级越高）
   - offset值（越大数据越新）
   - run_id（越小优先级越高）
3. **提升新Master**：
   ```bash
   slaveof no one  # 让选定节点成为Master
   ```
4. **重新配置集群**：
   ```bash
   slaveof <new-master-ip> <port>  # 其他节点成为新Master的Slave
   ```

### 哨兵配置要点
```bash
# 基本配置
port 26379
daemonize yes
sentinel monitor mymaster 127.0.0.1 6379 2
sentinel down-after-milliseconds mymaster 5000  # 5秒无响应认为主观下线
sentinel failover-timeout mymaster 60000       # 故障转移超时时间
```

## 📋 常见问题解答

### RDB相关
**Q：`save 60 10000`的含义？**
A：60秒内至少有10000次修改则触发RDB。

**Q：RDB的缺点？**
A：可能丢失两次快照间的数据；fork操作可能阻塞服务。

### AOF相关
**Q：AOF重写的意义？**
A：压缩AOF文件，去除冗余命令，减少文件体积。

### 哨兵相关
**Q：Sentinel如何判断节点健康？**
A：每秒发送ping命令，超时无响应则主观下线；多数Sentinel同意则客观下线。

**Q：故障转移步骤？**
A：1.选举新Master 2.执行`slaveof no one` 3.其他节点重新slaveof 4.更新客户端配置。

### 最佳实践建议

1. **持久化策略**：
   - 生产环境建议RDB+AOF
   - AOF使用`everysec`策略平衡性能与安全

2. **内存规划**：
   - 预留50%内存应对fork操作
   - 监控内存使用，避免swap

3. **高可用部署**：
   - 至少3个Sentinel实例
   - 跨机房部署主从节点
   - 定期测试故障转移

4. **监控告警**：
   - 监控持久化延迟
   - 设置主从同步延迟告警
   - 监控Sentinel选举状态
