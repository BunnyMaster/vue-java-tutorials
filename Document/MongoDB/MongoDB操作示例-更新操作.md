# MongoDB 更新操作完整指南

## 1. 基础更新操作

### 1.1 更新单个文档
```javascript
// 使用updateOne更新单个文档
db.role.updateOne(
    { _id: ObjectId("68c8383f149bac1cfbdd4783") },
    { $set: { createUsername: "Bunny" } }
)

// 更新多个字段
db.role.updateOne(
    { roleCode: "ADMIN" },
    { 
        $set: { 
            updateUsername: "NewAdmin",
            updateTime: new Date(),
            remark: "更新后的管理员角色"
        }
    }
)
```

### 1.2 更新多个文档
```javascript
// 使用updateMany批量更新
db.role.updateMany(
    { roleType: 3 },  // 条件：所有角色类型为3的文档
    { 
        $set: { 
            status: 0,  // 禁用所有类型3的角色
            updateTime: new Date(),
            updateUsername: "System"
        }
    }
)

// 批量更新数据范围
db.role.updateMany(
    { dataScope: { $lt: 3 } },  // 数据范围小于3的角色
    { 
        $set: { 
            dataScope: 3,
            updateTime: new Date()
        }
    }
)
```

## 2. 更新操作符详解

### 2.1 字段更新操作符
```javascript
// $set - 设置字段值
db.role.updateOne(
    { roleCode: "USER" },
    { $set: { roleName: "普通用户", orderNum: 1 } }
)

// $unset - 删除字段
db.role.updateOne(
    { roleCode: "TEST" },
    { $unset: { remark: "" } }  // 删除remark字段
)

// $rename - 重命名字段
db.role.updateMany(
    {},
    { $rename: { "createUser": "creatorId", "updateUser": "updaterId" } }
)

// $inc - 增加数值
db.role.updateOne(
    { roleCode: "ADMIN" },
    { $inc: { orderNum: 1 } }  // orderNum增加1
)

// $mul - 乘以数值
db.role.updateOne(
    { roleCode: "USER" },
    { $mul: { orderNum: 2 } }  // orderNum乘以2
)
```

### 2.2 数组更新操作符
```javascript
// 假设role文档有permissions数组字段
// $push - 添加元素到数组
db.role.updateOne(
    { roleCode: "ADMIN" },
    { $push: { permissions: "user:delete" } }
)

// $addToSet - 添加不重复元素
db.role.updateOne(
    { roleCode: "ADMIN" },
    { $addToSet: { permissions: "user:create" } }
)

// $pull - 移除数组中的元素
db.role.updateOne(
    { roleCode: "ADMIN" },
    { $pull: { permissions: "user:delete" } }
)

// $pop - 移除第一个或最后一个元素
db.role.updateOne(
    { roleCode: "ADMIN" },
    { $pop: { permissions: 1 } }  // 1: 移除最后一个, -1: 移除第一个
)
```

### 2.3 其他实用操作符
```javascript
// $min - 只在指定值较小时更新
db.role.updateOne(
    { roleCode: "ADMIN" },
    { $min: { orderNum: 0 } }  // 如果0小于当前orderNum，则更新为0
)

// $max - 只在指定值较大时更新
db.role.updateOne(
    { roleCode: "USER" },
    { $max: { orderNum: 10 } }  // 如果10大于当前orderNum，则更新为10
)

// $currentDate - 设置当前日期
db.role.updateOne(
    { roleCode: "ADMIN" },
    { 
        $currentDate: { 
            updateTime: true,  // 设置为当前日期
            lastModified: { $type: "timestamp" }  // 设置为时间戳
        }
    }
)
```

## 3. 更新选项和条件

### 3.1 更新选项
```javascript
// upsert: true - 如果不存在则插入
db.role.updateOne(
    { roleCode: "NEW_ROLE" },  // 不存在的角色代码
    { 
        $set: {
            roleName: "新角色",
            roleType: 1,
            status: 1,
            createTime: new Date()
        }
    },
    { upsert: true }  // 如果不存在则创建新文档
)

// writeConcern - 写关注设置
db.role.updateOne(
    { roleCode: "ADMIN" },
    { $set: { updateTime: new Date() } },
    { 
        writeConcern: { 
            w: "majority", 
            wtimeout: 5000 
        }
    }
)
```

### 3.2 复杂条件更新
```javascript
// 多条件更新
db.role.updateMany(
    { 
        roleType: { $in: [1, 2] },  // 角色类型为1或2
        status: 1,                   // 状态为启用
        orderNum: { $gt: 5 }         // 排序号大于5
    },
    { 
        $set: { 
            dataScope: 4,
            updateTime: new Date()
        }
    }
)

// 使用正则表达式匹配
db.role.updateMany(
    { roleName: { $regex: /经理$/ } },  // 角色名以"经理"结尾
    { $set: { roleType: 2 } }
)
```

## 4. 替换文档操作

### 4.1 替换整个文档
```javascript
// replaceOne - 替换整个文档
db.role.replaceOne(
    { roleCode: "ADMIN" },
    {
        id: "1953368058467565570",
        roleCode: "ADMIN",
        roleName: "超级管理员",  // 修改名称
        roleType: 0,            // 修改类型
        dataScope: 1,
        orderNum: 0,
        status: 1,
        remark: "系统超级管理员角色",
        createTime: new Date("2025-08-07T16:10:12Z"),
        updateTime: new Date(),
        createUser: "1",
        updateUser: "1",
        createUsername: "Administrator",
        updateUsername: "CurrentUser"
    }
)
```

## 5. 批量写入操作

### 5.1 批量更新操作
```javascript
// bulkWrite - 批量执行多个操作
db.role.bulkWrite([
    {
        updateOne: {
            filter: { roleCode: "ADMIN" },
            update: { $set: { orderNum: 0, updateTime: new Date() } }
        }
    },
    {
        updateMany: {
            filter: { roleType: 3 },
            update: { $set: { status: 0, updateTime: new Date() } }
        }
    },
    {
        updateOne: {
            filter: { roleCode: "NEW_ROLE" },
            update: { 
                $set: {
                    roleName: "新角色",
                    roleType: 1,
                    status: 1,
                    createTime: new Date()
                }
            },
            upsert: true
        }
    }
])
```

## 6. 错误处理和验证

### 6.1 完整的错误处理
```javascript
try {
    const result = db.role.updateOne(
        { _id: ObjectId("68c8383f149bac1cfbdd4783") },
        { $set: { createUsername: "Bunny" } }
    );
    
    if (result.matchedCount === 0) {
        print("警告: 没有找到匹配的文档");
    }
    
    if (result.modifiedCount === 0) {
        print("警告: 文档已存在相同值，未进行更新");
    }
    
    print(`成功更新 ${result.modifiedCount} 个文档`);
    
} catch (error) {
    print("更新操作失败: " + error);
    
    // 处理特定错误
    if (error.code === 11000) {
        print("错误: 违反唯一性约束");
    }
}
```

### 6.2 更新前验证
```javascript
// 更新前先检查文档是否存在
const targetDoc = db.role.findOne({ _id: ObjectId("68c8383f149bac1cfbdd4783") });

if (targetDoc) {
    db.role.updateOne(
        { _id: targetDoc._id },
        { $set: { createUsername: "Bunny" } }
    );
    print("更新成功");
} else {
    print("文档不存在，无法更新");
}
```

## 7. 实用示例

### 7.1 批量更新状态
```javascript
// 禁用所有测试相关的角色
db.role.updateMany(
    { 
        $or: [
            { roleCode: { $regex: /TEST/i } },
            { roleName: { $regex: /测试/i } },
            { remark: { $regex: /测试/i } }
        ]
    },
    { 
        $set: { 
            status: 0,
            updateTime: new Date(),
            updateUsername: "SystemCleanup"
        }
    }
)
```

### 7.2 调整排序号
```javascript
// 为所有管理类角色增加排序号
db.role.updateMany(
    { roleName: { $regex: /经理|管理/i } },
    { 
        $inc: { orderNum: 10 },  // 排序号增加10
        $set: { updateTime: new Date() }
    }
)
```
