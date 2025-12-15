# MongoDB 删除操作完整指南

## 1. 基础删除操作

### 1.1 删除单个文档
```javascript
// 根据ID删除单个文档
db.role.deleteOne({ _id: ObjectId("68c8383f149bac1cfbdd4783") })

// 根据条件删除单个文档
db.role.deleteOne({ roleCode: "TEST" })

// 删除第一个匹配的文档
db.role.deleteOne({ status: 0 })  // 删除第一个状态为0的角色
```

### 1.2 删除多个文档
```javascript
// 删除所有匹配的文档
db.role.deleteMany({ status: 0 })  // 删除所有状态为0的角色

// 删除所有普通用户角色
db.role.deleteMany({ roleType: 3 })

// 删除所有包含"测试"的角色
db.role.deleteMany({ 
    $or: [
        { roleName: { $regex: /测试/i } },
        { remark: { $regex: /测试/i } }
    ]
})

// 删除所有文档（清空集合）
db.role.deleteMany({})
```

## 2. 删除操作与条件组合

### 2.1 复杂条件删除
```javascript
// 删除创建时间早于某个日期的文档
db.role.deleteMany({
    createTime: { $lt: new Date("2025-09-01") }
})

// 删除排序号大于10且状态为禁用的角色
db.role.deleteMany({
    orderNum: { $gt: 10 },
    status: 0
})

// 删除角色类型为2且数据范围小于3的角色
db.role.deleteMany({
    roleType: 2,
    dataScope: { $lt: 3 }
})
```

### 2.2 使用操作符的删除
```javascript
// 删除数组字段为空的文档
db.role.deleteMany({
    permissions: { $exists: true, $size: 0 }
})

// 删除没有备注信息的角色
db.role.deleteMany({
    $or: [
        { remark: { $exists: false } },
        { remark: "" },
        { remark: null }
    ]
})

// 删除更新时间超过30天的文档
db.role.deleteMany({
    updateTime: { $lt: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000) }
})
```

## 3. 删除整个集合

### 3.1 删除集合
```javascript
// 删除整个集合（包括所有文档和索引）
db.role.drop()

// 删除前检查集合是否存在
if (db.getCollectionNames().includes("role")) {
    db.role.drop()
    print("角色集合已删除")
} else {
    print("角色集合不存在")
}
```

### 3.2 删除数据库
```javascript
// 切换到要删除的数据库
use old_database

// 删除当前数据库
db.dropDatabase()

// 删除前确认
print("即将删除数据库: " + db.getName())
db.dropDatabase()
print("数据库已删除")
```

## 4. 错误处理和验证

### 4.1 完整的错误处理
```javascript
try {
    const result = db.role.deleteMany({ status: 0 })
    
    print(`删除操作结果:`)
    print(`- 匹配文档数: ${result.deletedCount}`)
    print(`- 操作是否确认: ${result.acknowledged}`)
    
    if (result.deletedCount === 0) {
        print("警告: 没有找到匹配的文档进行删除")
    }
    
} catch (error) {
    print("删除操作失败: " + error)
    
    // 处理特定错误
    if (error.code === 13) {  // 权限错误
        print("错误: 没有删除权限")
    }
}
```

### 4.2 删除前的验证
```javascript
// 删除前先检查匹配的文档
const documentsToDelete = db.role.find({ status: 0 }).count()

if (documentsToDelete > 0) {
    print(`找到 ${documentsToDelete} 个待删除文档`)
    
    // 预览要删除的文档
    const sampleDocs = db.role.find({ status: 0 }).limit(3)
    print("示例文档:")
    sampleDocs.forEach(doc => {
        print(`- ${doc.roleCode}: ${doc.roleName}`)
    })
    
    // 执行删除
    const result = db.role.deleteMany({ status: 0 })
    print(`成功删除 ${result.deletedCount} 个文档`)
    
} else {
    print("没有找到匹配的文档")
}
```

## 5. 安全删除实践

### 5.1 备份后再删除
```javascript
// 先备份要删除的数据到新集合
db.role.aggregate([
    { $match: { status: 0 } },
    { $out: "deleted_roles_backup" }
])

// 然后执行删除
const result = db.role.deleteMany({ status: 0 })
print(`已备份并删除 ${result.deletedCount} 个文档`)
```

### 5.2 使用事务进行安全删除
```javascript
const session = db.getMongo().startSession()

try {
    session.startTransaction()
    
    const roleCollection = session.getDatabase("your_db").role
    
    // 先记录要删除的文档ID
    const docsToDelete = roleCollection.find({ status: 0 }, { _id: 1 }).toArray()
    
    // 执行删除
    const deleteResult = roleCollection.deleteMany({ status: 0 })
    
    // 记录删除操作到日志
    db.deletion_log.insertOne({
        collection: "role",
        deletedCount: deleteResult.deletedCount,
        deletedIds: docsToDelete.map(doc => doc._id),
        deleteTime: new Date(),
        executedBy: "admin"
    })
    
    session.commitTransaction()
    print("删除事务提交成功")
    
} catch (error) {
    session.abortTransaction()
    print("删除事务回滚: " + error)
    
} finally {
    session.endSession()
}
```

## 6. 批量删除操作

### 6.1 使用bulkWrite进行批量删除
```javascript
// 批量删除多个条件的文档
db.role.bulkWrite([
    {
        deleteOne: {
            filter: { roleCode: "TEST_USER" }
        }
    },
    {
        deleteMany: {
            filter: { 
                roleType: 3,
                createTime: { $lt: new Date("2025-01-01") }
            }
        }
    },
    {
        deleteMany: {
            filter: { status: 0 }
        }
    }
])
```

### 6.2 分批次删除大量数据
```javascript
// 分批次删除，避免性能问题
let deletedCount = 0
let batchSize = 1000

while (true) {
    const result = db.role.deleteMany({
        status: 0,
        _id: { $lt: ObjectId() }  // 可以添加其他条件来分批次
    }, { limit: batchSize })
    
    deletedCount += result.deletedCount
    print(`已删除 ${deletedCount} 个文档`)
    
    if (result.deletedCount < batchSize) {
        break
    }
    
    // 添加延迟，避免对生产系统造成太大压力
    sleep(100)
}
```

## 7. 实用示例

### 7.1 清理测试数据
```javascript
// 清理所有测试相关的角色
const testPatterns = ["TEST", "测试", "temp", "demo"]

const deleteResult = db.role.deleteMany({
    $or: [
        { roleCode: { $regex: new RegExp(testPatterns.join("|"), "i") } },
        { roleName: { $regex: new RegExp(testPatterns.join("|"), "i") } },
        { remark: { $regex: new RegExp(testPatterns.join("|"), "i") } }
    ]
})

print(`清理了 ${deleteResult.deletedCount} 个测试角色`)
```

### 7.2 定期清理过期数据
```javascript
// 清理30天前创建的无效角色
const thirtyDaysAgo = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000)

const cleanupResult = db.role.deleteMany({
    status: 0,
    createTime: { $lt: thirtyDaysAgo }
})

print(`定期清理: 删除了 ${cleanupResult.deletedCount} 个过期角色`)
```

## 8. 删除操作的最佳实践

1. **始终备份**：重要数据删除前先备份
2. **先查询后删除**：使用find()确认要删除的文档
3. **使用条件限制**：避免误删整个集合
4. **分批处理**：大量数据删除时分批进行
5. **记录日志**：重要删除操作记录日志
6. **权限控制**：生产环境严格控制删除权限
7. **测试环境验证**：先在测试环境验证删除逻辑

```javascript
// 安全删除示例
function safeDelete(collectionName, filter, options = {}) {
    const collection = db[collectionName]
    const backupName = options.backupName || `deleted_${collectionName}_${new Date().getTime()}`
    
    // 备份数据
    collection.aggregate([
        { $match: filter },
        { $out: backupName }
    ])
    
    // 执行删除
    const result = collection.deleteMany(filter)
    
    print(`安全删除完成:`)
    print(`- 备份集合: ${backupName}`)
    print(`- 删除文档数: ${result.deletedCount}`)
    
    return result
}

// 使用安全删除函数
safeDelete("role", { status: 0 })
```
