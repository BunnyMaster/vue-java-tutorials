# MongoDB 基础插入与查询操作示例

> [!NOTE]
>
> 1. **插入操作**：使用 `insertOne()` 插入单个文档，`insertMany()` 插入多个文档
> 2. **查询操作**：使用 `find()` 查询多个文档，`findOne()` 查询单个文档
> 3. **条件查询**：使用 `{field: value}` 格式进行条件筛选
> 4. **投影查询**：使用 `{field: 1}` 包含字段，`{field: 0}` 排除字段
> 5. **错误处理**：始终使用 try-catch 处理可能的操作错误
> 6. **嵌套查询**：使用点符号 `"meta.title"` 查询嵌套文档字段

## 1. 数据库与集合准备

```javascript
// 创建/切换到 routes 数据库
use routes

// 查看当前数据库
db

// 显示所有数据库（空数据库不会显示）
show dbs
```

## 2. 插入文档操作

### 2.1 插入单个文档
```javascript
// 插入单个路由文档
try {
    db.routes.insertOne({
        "name": "Dashboard",
        "path": "/dashboard",
        "hiddenTag": false,
        "meta": {
            "title": "仪表板",
            "icon": "material-symbols:dashboard",
            "showLink": true,
            "roles": ["ADMIN", "USER"],
            "rank": 0
        }
    })
    print("单个文档插入成功")
} catch(error) {
    print("插入失败: " + error)
}
```

### 2.2 插入多个文档（路由菜单数据）
```javascript
// 插入系统管理相关路由
try {
    db.routes.insertMany([
        {
            "name": "System",
            "path": "/system",
            "hiddenTag": false,
            "meta": {
                "title": "系统管理",
                "icon": "grommet-icons:system",
                "showLink": true,
                "roles": ["ADMIN"],
                "rank": 1
            },
            "children": [
                {
                    "name": "SystemUser",
                    "path": "/system/user",
                    "component": "system/user/index",
                    "meta": {
                        "title": "用户管理",
                        "icon": "iconoir:user",
                        "rank": 2
                    }
                },
                {
                    "name": "SystemRole",
                    "path": "/system/role",
                    "component": "/system/role/index",
                    "meta": {
                        "title": "角色管理",
                        "icon": "carbon:user-role",
                        "rank": 3
                    }
                }
            ]
        },
        {
            "name": "SystemLog",
            "path": "/log",
            "redirect": "/log/login",
            "meta": {
                "title": "系统日志",
                "icon": "ri:login-box-fill",
                "roles": ["ADMIN"],
                "rank": 10
            },
            "children": [
                {
                    "name": "LoginLog",
                    "path": "/log/login-log",
                    "component": "/log/login-log/index",
                    "meta": {
                        "title": "登录日志",
                        "icon": "mdi:login",
                        "rank": 11
                    }
                }
            ]
        }
    ])
    print("多个文档插入成功")
} catch(error) {
    print("批量插入失败: " + error)
}
```

## 3. 查询文档操作

### 3.1 基础查询
```javascript
// 查询集合中的所有文档
db.routes.find()

// 美化输出格式
db.routes.find().pretty()

// 查询单个文档
db.routes.findOne()
```

### 3.2 条件查询
```javascript
// 按名称查询
db.routes.find({name: "System"})

// 查询隐藏标签为false的路由
db.routes.find({hiddenTag: false})

// 查询角色包含ADMIN的路由
db.routes.find({"meta.roles": "ADMIN"})
```

### 3.3 投影查询（指定返回字段）
```javascript
// 只返回name和path字段
db.routes.find({}, {name: 1, path: 1})

// 排除_id字段
db.routes.find({}, {name: 1, path: 1, _id: 0})

// 查询System路由的特定字段
db.routes.find(
    {name: "System"}, 
    {name: 1, "meta.title": 1, "meta.icon": 1, _id: 0}
)
```

### 3.4 嵌套文档查询
```javascript
// 查询包含子路由的路由
db.routes.find({"children.0": {$exists: true}})

// 查询特定子路由名称
db.routes.find({"children.name": "SystemUser"})

// 查询rank大于5的路由
db.routes.find({"meta.rank": {$gt: 5}})
```

## 4. 结果处理

### 4.1 计数查询
```javascript
// 统计所有文档数量
db.routes.countDocuments()

// 统计特定条件文档数量
db.routes.countDocuments({hiddenTag: false})
```

### 4.2 结果限制与排序
```javascript
// 限制返回结果数量
db.routes.find().limit(3)

// 按rank降序排序
db.routes.find().sort({"meta.rank": -1})

// 组合使用：排序+限制
db.routes.find()
    .sort({"meta.rank": 1})
    .limit(5)
```

## 5. 错误处理最佳实践

```javascript
// 完整的错误处理示例
try {
    const result = db.routes.insertOne({
        "name": "Settings",
        "path": "/settings",
        "meta": {
            "title": "设置",
            "icon": "material-symbols:settings"
        }
    })
    
    if (result.insertedId) {
        print("文档插入成功，ID: " + result.insertedId)
        
        // 查询刚插入的文档
        const newDoc = db.routes.findOne({_id: result.insertedId})
        print("新插入的文档: ")
        printjson(newDoc)
    }
    
} catch(error) {
    print("操作失败: " + error)
    
    // 检查是否是重复键错误
    if (error.code === 11000) {
        print("错误原因: 文档已存在")
    }
}
```

## 6. 实用技巧

### 6.1 快速查看数据结构
```javascript
// 查看一个文档的结构
const sampleDoc = db.routes.findOne()
printjson(sampleDoc)

// 查看所有字段名
const fieldNames = []
db.routes.findOne({}, function(err, doc) {
    for (var key in doc) {
        fieldNames.push(key)
    }
})
print("所有字段: " + fieldNames.join(", "))
```

### 6.2 验证插入结果
```javascript
// 插入后立即验证
const insertResult = db.routes.insertOne({
    "name": "TestRoute",
    "path": "/test"
})

if (insertResult.acknowledged) {
    const count = db.routes.countDocuments({_id: insertResult.insertedId})
    print("验证结果: " + (count > 0 ? "插入成功" : "插入失败"))
}
```
