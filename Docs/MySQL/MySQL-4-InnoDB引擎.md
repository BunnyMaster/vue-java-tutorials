# InnoDB引擎

## 逻辑存储结构

```
表空间 → 段 → 区 → 页 → 行
```

**表空间(ibd文件)**
- 一个MySQL实例对应多个表空间
- 存储记录、索引等数据
- 每个表对应一个.ibd文件（file-per-table模式下）

**段(Segment)**
- 分为数据段(Leaf node segment)、索引段(Non-leaf node segment)、回滚段(Rollback segment)
- InnoDB是索引组织表，数据段对应B+树的叶子节点，索引段对应非叶子节点
- 段用来管理多个区

**区(Extent)**
- 表空间的单元结构，每个区大小为1MB
- 默认页大小16KB，每个区包含64个连续的页
- InnoDB每次从磁盘申请4-5个区以保证连续性

**页(Page)**
- InnoDB磁盘管理的最小单元，默认大小16KB
- 包含数据行、索引等信息

**行(Row)**
- InnoDB数据按行存放
- 每行包含两个重要隐藏字段：
  - `DB_TRX_ID`：最近修改事务ID
  - `DB_ROLL_PTR`：回滚指针，指向undo log中的旧版本

## 架构

MySQL 5.5+默认存储引擎，支持事务和崩溃恢复。

### 内存结构

**Buffer Pool（缓冲池）**
- 主内存区域，缓存磁盘热点数据
- 增删改查先操作缓冲池，定期刷新到磁盘
- 按Page页管理，分为三种状态：
  - **Free Page**：空闲页面
  - **Clean Page**：被使用但未修改
  - **Dirty Page**：被修改，与磁盘数据不一致

**Change Buffer（更改缓冲区）**
- **针对非唯一二级索引**的优化
- DML操作时，如果数据页不在Buffer Pool中，将变更暂存到Change Buffer
- 后续读取时再合并到Buffer Pool，减少磁盘IO

**Log Buffer（日志缓冲区）**
- 保存要写入磁盘的redo log、undo log数据
- 默认大小16MB
- 相关参数：
  - `innodb_log_buffer_size`：缓冲区大小
  - `innodb_flush_log_at_trx_commit`：日志刷新策略
    - **1**：每次事务提交时刷盘（最安全）
    - **0**：每秒刷盘一次（性能最好，可能丢失1秒数据）
    - **2**：每次提交写OS缓存，每秒刷盘（平衡方案）

### 磁盘结构

**系统表空间(System Tablespace)**
- 包含Change Buffer存储区域
- MySQL 5.x中还包含数据字典、undo log等
- 参数：`innodb_data_file_path`

**独立表空间(File-Per-Table Tablespaces)**
- 每个表独立.ibd文件，包含表数据和索引
- 参数：`innodb_file_per_table=ON`（推荐）

### 后台线程

| 线程类型                | 数量 | 职责                                         |
| ----------------------- | ---- | -------------------------------------------- |
| **Master Thread**       | 1    | 核心线程，调度其他线程，缓冲池刷新，脏页管理 |
| **Read Thread**         | 4    | 处理读IO请求                                 |
| **Write Thread**        | 4    | 处理写IO请求                                 |
| **Log Thread**          | 1    | 日志缓冲区刷新到磁盘                         |
| **Purge Thread**        | 1    | 回收已提交事务的undo log                     |
| **Page Cleaner Thread** | 1    | 协助Master Thread刷新脏页                    |

## 事务原理

ACID特性：
- **原子性**：Undo Log实现
- **一致性**：Undo Log + Redo Log共同保证
- **隔离性**：锁 + MVCC实现
- **持久性**：Redo Log实现

### Redo Log（重做日志）

**作用**：保证事务持久性，记录数据页的物理修改

**组成**：
- Redo Log Buffer（内存）
- Redo Log File（磁盘）

**工作流程**：
1. 事务修改数据时，先写入Redo Log Buffer
2. 事务提交时，Redo Log刷盘（WAL机制）
3. 定期将脏页刷新到磁盘
4. 崩溃恢复时，通过Redo Log重做已提交的事务

**优势**：顺序IO vs 随机IO，大幅提升性能

### Undo Log（回滚日志）

**作用**：
- 事务回滚
- MVCC多版本并发控制

**特点**：
- 逻辑日志（记录反向操作）
- 事务提交后不立即删除（用于MVCC）
- 存储在回滚段中（1024个undo log segment）

## MVCC多版本并发控制

### 基本概念

**当前读**
- 读取记录最新版本，加锁保证一致性
- 如：`SELECT ... FOR UPDATE`、`UPDATE`、`DELETE`等

**快照读**
- 读取数据的历史可见版本，不加锁
- 普通`SELECT`语句
- 隔离级别影响：
  - **Read Committed**：每次SELECT生成新快照
  - **Repeatable Read**：事务第一次SELECT生成快照
  - **Serializable**：退化为当前读

### MVCC实现原理

**三要素**：
1. 隐藏字段
2. Undo Log版本链
3. Read View可见性判断

**隐藏字段**：
| 字段名        | 说明                                  |
| ------------- | ------------------------------------- |
| `DB_TRX_ID`   | 最近修改事务ID（6字节）               |
| `DB_ROLL_PTR` | 回滚指针，指向undo log旧版本（7字节） |
| `DB_ROW_ID`   | 隐藏主键（6字节，无主键时生成）       |

**工作流程**：
1. 每个事务生成Read View
2. 根据DB_TRX_ID判断数据版本可见性
3. 通过DB_ROLL_PTR遍历undo log版本链
4. 返回对当前事务可见的数据版本
