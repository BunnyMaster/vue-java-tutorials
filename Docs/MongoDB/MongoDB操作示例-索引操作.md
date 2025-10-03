# MongoDB 索引操作与优化指南

> [!Tip]
>
> 1. **使用复合索引**遵循ESR规则（等值、排序、范围）
> 2. **创建覆盖索引**避免回表查询
> 3. **监控索引使用率**删除无用索引
> 4. **定期维护索引**解决碎片化问题
> 5. **使用explain分析**查询执行计划

## 1. 索引基础操作

> [!WARNING]
>
> - 创建过多冗余索引
> - 索引字段顺序不合理
> - 忽略索引的维护和监控
> - 不使用explain分析查询性能

### 1.1 创建索引
```javascript
// 单字段索引
db.role.createIndex({ roleCode: 1 })  // 升序索引
db.role.createIndex({ createTime: -1 }) // 降序索引

// 复合索引
db.role.createIndex({ roleType: 1, status: 1 }) // 多字段索引
db.role.createIndex({ roleCode: 1, roleType: -1 }) // 混合排序

// 文本索引（用于全文搜索）
db.role.createIndex({ roleName: "text", remark: "text" })

// 地理空间索引
// db.location.createIndex({ coordinates: "2dsphere" })

// 哈希索引
db.role.createIndex({ roleCode: "hashed" })
```

### 1.2 查看索引信息
```javascript
// 查看集合的所有索引
db.role.getIndexes()

// 查看索引大小和统计信息
db.role.totalIndexSize()
db.role.stats().indexSizes

// 查看索引使用情况
db.role.aggregate([{ $indexStats: {} }])
```

### 1.3 删除索引
```javascript
// 按名称删除索引
db.role.dropIndex("roleCode_1")

// 按规范删除索引
db.role.dropIndex({ roleCode: 1 })

// 删除所有索引（除了_id索引）
db.role.dropIndexes()
```

## 2. 索引选项与高级配置

### 2.1 索引选项
```javascript
// 唯一索引
db.role.createIndex({ roleCode: 1 }, { unique: true })

// 稀疏索引（只索引包含该字段的文档）
db.role.createIndex({ remark: 1 }, { sparse: true })

// TTL索引（自动过期删除）
db.sessions.createIndex({ createdAt: 1 }, { expireAfterSeconds: 3600 })

// 部分索引（只索引满足条件的文档）
db.role.createIndex(
    { roleCode: 1 },
    { partialFilterExpression: { status: 1 } } // 只索引启用状态的角色
)

// 指定索引名称
db.role.createIndex(
    { roleCode: 1, roleType: 1 },
    { name: "idx_role_code_type" }
)

// 后台创建索引（不阻塞操作）
db.role.createIndex({ roleCode: 1 }, { background: true })
```

### 2.2 索引性能配置
```javascript
// 设置索引存储引擎选项
db.role.createIndex(
    { roleCode: 1 },
    { 
        storageEngine: {
            wiredTiger: {
                configString: "block_compressor=snappy"
            }
        }
    }
)

// 设置索引的collation（排序规则）
db.role.createIndex(
    { roleName: 1 },
    { collation: { locale: "en", strength: 2 } }
)
```

## 3. 索引优化策略

### 3.1 查询覆盖索引
```javascript
// 创建覆盖查询的复合索引
db.role.createIndex({ roleType: 1, status: 1, roleName: 1 })

// 查询可以使用索引覆盖，不需要回表
db.role.find(
    { roleType: 1, status: 1 },
    { roleName: 1, _id: 0 }  // 只返回索引中包含的字段
).explain("executionStats")
```

### 3.2 索引前缀优化
```javascript
// 复合索引 { roleType: 1, status: 1, createTime: 1 } 可以支持：
db.role.find({ roleType: 1 })  // 使用前缀
db.role.find({ roleType: 1, status: 1 })  // 使用前缀
db.role.find({ roleType: 1, status: 1, createTime: { $gt: ISODate() } })  // 完整使用

// 但无法支持：
db.role.find({ status: 1 })  // 无法使用索引（不是前缀）
```

### 3.3 多键索引优化
```javascript
// 对数组字段创建多键索引
db.role.createIndex({ "permissions": 1 })

// 查询数组包含特定值的文档
db.role.find({ permissions: "user:read" })  // 可以使用索引
```

## 4. 索引性能分析

### 4.1 使用explain分析查询
```javascript
// 分析查询执行计划
db.role.find({ roleCode: "ADMIN" }).explain("executionStats")

// 查看索引使用情况
const explainResult = db.role.find(
    { roleType: 1, status: 1 }
).explain("executionStats")

print("查询阶段: " + explainResult.executionStats.executionStages.stage)
print("扫描文档数: " + explainResult.executionStats.nReturned)
print("索引使用: " + explainResult.executionStats.executionStages.inputStage.indexName)
```

### 4.2 索引效率监控
```javascript
// 监控索引使用频率
const indexStats = db.role.aggregate([{ $indexStats: {} }]).toArray()

indexStats.forEach(stats => {
    print(`索引: ${stats.name}`)
    print(`- 使用次数: ${stats.accesses.ops}`)
    print(`- 最后使用: ${new Date(stats.accesses.since)}`)
})
```

## 5. 常见索引优化场景

### 5.1 排序优化
```javascript
// 为排序操作创建索引
db.role.createIndex({ orderNum: 1 })  // 升序排序优化
db.role.createIndex({ createTime: -1 }) // 降序排序优化

// 复合排序索引
db.role.createIndex({ roleType: 1, orderNum: 1 })  // 先按类型再按排序号
```

### 5.2 范围查询优化
```javascript
// 对于范围查询，将等值查询字段放在前面
db.role.createIndex({ status: 1, createTime: 1 })  // 优化: status=1 AND createTime > X

// 不好的索引顺序
db.role.createIndex({ createTime: 1, status: 1 })  // 对于上述查询效率较低
```

### 5.3 文本搜索优化
```javascript
// 创建文本索引支持全文搜索
db.role.createIndex({
    roleName: "text",
    remark: "text"
}, {
    weights: {
        roleName: 10,  // 角色名称权重更高
        remark: 5      // 备注权重较低
    },
    default_language: "english"
})

// 使用文本搜索
db.role.find({ $text: { $search: "管理员" } })
```

## 6. 索引维护与管理

### 6.1 索引重建与压缩
```javascript
// 重建索引（解决索引碎片化）
db.role.reIndex()

// 在线重建索引（MongoDB 4.4+）
db.adminCommand({
    compact: "role",
    force: false
})

// 监控索引大小
db.role.aggregate([
    { $indexStats: {} },
    { $project: { name: 1, size: 1 } }
])
```

### 6.2 索引存储优化
```javascript
// 使用压缩选项
db.role.createIndex(
    { roleCode: 1 },
    {
        storageEngine: {
            wiredTiger: {
                configString: "block_compressor=zstd"  // 更好的压缩比
            }
        }
    }
)
```

## 7. 实战优化示例

### 7.1 角色查询优化
```javascript
// 常见查询模式1：按角色类型和状态查询
db.role.createIndex({ roleType: 1, status: 1 })

// 常见查询模式2：按创建时间范围查询
db.role.createIndex({ createTime: -1 })

// 常见查询模式3：按角色代码精确查询
db.role.createIndex({ roleCode: 1 }, { unique: true })

// 复合查询优化
db.role.createIndex({ 
    roleType: 1, 
    status: 1, 
    orderNum: 1 
})
```

### 7.2 分页查询优化
```javascript
// 优化分页查询的索引
db.role.createIndex({ createTime: -1, _id: 1 })

// 高效的分页查询
const lastDoc = db.role.findOne({}, { sort: { createTime: -1 } })
db.role.find({ 
    createTime: { $lt: lastDoc.createTime } 
}).sort({ createTime: -1 }).limit(10)
```

### 7.3 聚合查询优化
```javascript
// 为聚合管道创建索引
db.role.createIndex({ roleType: 1, createTime: 1 })

// 优化聚合查询
db.role.aggregate([
    { $match: { roleType: 1, createTime: { $gte: new Date("2025-01-01") } } },
    { $group: { _id: "$status", count: { $sum: 1 } } },
    { $sort: { count: -1 } }
]).explain("executionStats")
```

## 8. 索引最佳实践

### 8.1 索引设计原则
```javascript
// 1. ESR规则：Equality, Sort, Range
db.role.createIndex({ 
    status: 1,        // Equality: 等值查询
    createTime: -1,   // Sort: 排序字段  
    orderNum: 1       // Range: 范围查询
})

// 2. 避免过多索引（一般不超过5-10个）
// 3. 监控索引使用情况，删除未使用的索引
```

### 8.2 性能监控脚本
```javascript
// 索引使用情况监控
function monitorIndexUsage(collectionName) {
    const stats = db[collectionName].aggregate([{ $indexStats: {} }]).toArray()
    
    print(`=== ${collectionName} 索引使用情况 ===`)
    stats.forEach(index => {
        const usagePercent = (index.accesses.ops / (index.accesses.ops + 1)) * 100
        print(`${index.name}: ${index.accesses.ops} 次使用 (${usagePercent.toFixed(1)}%)`)
    })
    
    // 建议删除使用率低的索引
    const unusedIndexes = stats.filter(index => index.accesses.ops < 10)
    if (unusedIndexes.length > 0) {
        print("\n建议考虑删除以下低使用率索引:")
        unusedIndexes.forEach(index => {
            print(`- ${index.name} (使用 ${index.accesses.ops} 次)`)
        })
    }
}

// 执行监控
monitorIndexUsage("role")
```

### 8.3 索引维护自动化
```javascript
// 定期索引维护脚本
function maintainIndexes() {
    const collections = db.getCollectionNames()
    
    collections.forEach(collectionName => {
        if (collectionName !== "system.profile") {
            // 重建碎片化严重的索引
            db[collectionName].reIndex()
            
            // 删除长时间未使用的索引
            const indexStats = db[collectionName].aggregate([{ $indexStats: {} }]).toArray()
            const unusedIndexes = indexStats.filter(index => {
                const lastUsed = new Date(index.accesses.since)
                const daysUnused = (Date.now() - lastUsed) / (1000 * 60 * 60 * 24)
                return daysUnused > 30 && index.name !== "_id_"
            })
            
            unusedIndexes.forEach(index => {
                print(`删除未使用索引: ${collectionName}.${index.name}`)
                db[collectionName].dropIndex(index.name)
            })
        }
    })
}

// 每月执行一次维护
// maintainIndexes()
```