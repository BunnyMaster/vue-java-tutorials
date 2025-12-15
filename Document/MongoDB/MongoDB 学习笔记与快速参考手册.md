# MongoDB 学习笔记与快速参考手册

> [!TIP]
>
> 1. **数据库显示**：空数据库不会显示在`show dbs`结果中，需要至少插入一个文档
> 2. **错误处理**：使用try-catch处理可能出错的写入操作
> 3. **索引策略**：为常用查询字段创建索引提升性能
> 4. **更新操作**：使用`$set`操作符更新特定字段，避免整个文档替换
> 5. **聚合管道**：合理使用`$match`和`$project`阶段减少数据处理量

## 1. 数据库基础操作

### 1.1 数据库创建与切换
```javascript
// 创建/切换到 article 数据库
use article

// 注意：空数据库不会显示，插入数据后才会持久化
// 显示所有数据库（只显示有数据的数据库）
show dbs

// 显示当前使用的数据库
db

// 删除当前数据库
db.dropDatabase()
```

### 1.2 集合操作
```javascript
// 显式创建集合
db.createCollection('article')

// 删除集合
db.article.drop()

// 显示当前数据库的所有集合
show collections
```

## 2. 文档CRUD操作

### 2.1 创建(Create)文档
```javascript
// 插入单个文档
db.collection.insertOne({
   field1: value1,
   field2: value2,
   ...
})

// 插入多个文档
db.collection.insertMany([
   { field1: value1, field2: value2 },
   { field1: value3, field2: value4 },
   ...
])

// 错误处理示例
try {
    db.users.insertMany([...]);
} catch(e) {
    print(e);
}
```

### 2.2 读取(Read)文档
```javascript
// 查询所有文档
db.collection.find()

// 条件查询
db.collection.find({ field: value })

// 查询指定字段（1-包含，0-排除）
db.collection.find({}, { field1: 1, field2: 1, _id: 0 })

// 查询单个文档
db.collection.findOne({ field: value })
```

#### 2.2.1 查询操作符
```javascript
// 比较操作符
db.collection.find({ age: { $gt: 25 } })        // 大于
db.collection.find({ age: { $gte: 25 } })       // 大于等于
db.collection.find({ age: { $lt: 25 } })        // 小于
db.collection.find({ age: { $lte: 25 } })       // 小于等于
db.collection.find({ age: { $ne: 25 } })        // 不等于
db.collection.find({ age: { $in: [25, 30, 35] } })  // 在数组中
db.collection.find({ age: { $nin: [25, 30, 35] } }) // 不在数组中

// 逻辑操作符
db.collection.find({ $and: [{age: {$gt: 25}}, {city: "北京"}] })
db.collection.find({ $or: [{city: "北京"}, {city: "上海"}] })
```

#### 2.2.2 结果处理
```javascript
// 排序 (1: 升序, -1: 降序)
db.collection.find().sort({ field: 1 })

// 限制返回结果数量
db.collection.find().limit(10)

// 跳过指定数量的文档
db.collection.find().skip(20)

// 组合使用
db.collection.find().sort({ age: -1 }).skip(10).limit(5)
```

### 2.3 更新(Update)文档
```javascript
// 更新单个文档
db.collection.updateOne(
   { filter },
   { update },
   { options }
)

// 更新多个文档
db.collection.updateMany(
   { filter },
   { update },
   { options }
)

// 替换文档
db.collection.replaceOne(
   { filter },
   { replacement }
)
```

#### 2.3.1 更新操作符
```javascript
// 字段操作符
db.collection.updateOne({_id: 1}, { $set: { field: value } })  // 设置字段值
db.collection.updateOne({_id: 1}, { $unset: { field: "" } })   // 删除字段
db.collection.updateOne({_id: 1}, { $inc: { quantity: 5 } })   // 增加数值

// 数组操作符
db.collection.updateOne({_id: 1}, { $push: { arrayField: value } }) // 添加元素到数组
db.collection.updateOne({_id: 1}, { $pull: { arrayField: value } }) // 移除数组中匹配的元素
```

### 2.4 删除(Delete)文档
```javascript
// 删除单个文档
db.collection.deleteOne({ filter })

// 删除多个文档
db.collection.deleteMany({ filter })

// 删除所有文档
db.collection.deleteMany({})

// 删除集合
db.collection.drop()
```

## 3. 聚合框架

### 3.1 基本聚合管道
```javascript
db.collection.aggregate([
  { $match: { status: "A" } },          // 筛选文档
  { $group: { _id: "$cust_id", total: { $sum: "$amount" } } }, // 分组计算
  { $sort: { total: -1 } }              // 排序结果
])
```

### 3.2 常用聚合阶段
```javascript
[
  { $match: { ... } },      // 筛选文档
  { $group: { ... } },      // 分组
  { $sort: { ... } },       // 排序
  { $project: { ... } },    // 投影/重塑文档
  { $limit: n },            // 限制结果数量
  { $skip: n },             // 跳过文档
  { $unwind: "$arrayField" }, // 展开数组
  { $lookup: { ... } },     // 关联其他集合
]
```

## 4. 索引管理

### 4.1 创建索引
```javascript
// 单字段索引
db.collection.createIndex({ field: 1 })

// 复合索引
db.collection.createIndex({ field1: 1, field2: -1 })

// 文本索引
db.collection.createIndex({ field: "text" })

// 地理空间索引
db.collection.createIndex({ location: "2dsphere" })
```

### 4.2 索引管理
```javascript
// 查看索引
db.collection.getIndexes()

// 删除索引
db.collection.dropIndex("indexName")

// 创建带选项的索引
db.collection.createIndex(
  { field: 1 },
  {
    unique: true,           // 唯一索引
    sparse: true,           // 稀疏索引
    expireAfterSeconds: 3600, // TTL索引(自动过期)
    name: "indexName"       // 指定索引名称
  }
)
```

## 5. 实用命令

### 5.1 数据统计
```javascript
// 统计文档数量
db.collection.countDocuments({ filter })

// 去重查询
db.collection.distinct("field", { filter })
```

### 5.2 批量操作
```javascript
// 执行批量写入
db.collection.bulkWrite([
  { insertOne: { document: { ... } } },
  { updateOne: { filter: { ... }, update: { ... } } },
  { deleteOne: { filter: { ... } } }
])
```

## 6. 数据建模最佳实践

### 6.1 文档结构设计
- 嵌入式文档：适合一对一或一对少关系
- 引用式关系：适合一对多关系

### 6.2 示例模式
```javascript
// 用户文档示例
{
  _id: ObjectId("507f1f77bcf86cd799439011"),
  username: "john_doe",
  email: "john@example.com",
  profile: {
    firstName: "John",
    lastName: "Doe",
    address: {
      street: "123 Main St",
      city: "New York"
    }
  },
  created_at: ISODate("2023-01-01T00:00:00Z")
}

// 订单文档示例（使用引用）
{
  _id: ObjectId("607f1f77bcf86cd799439013"),
  userId: ObjectId("507f1f77bcf86cd799439011"), // 引用用户
  total: 199.99,
  items: [
    {
      productId: ObjectId("607f1f77bcf86cd799439012"),
      quantity: 2,
      price: 99.99
    }
  ]
}
```

## 7. 事务操作（MongoDB 4.0+）

```javascript
// 开启会话和事务
const session = db.getMongo().startSession();
session.startTransaction();

try {
  // 在事务中执行操作
  const collection = session.getDatabase("dbName").collection("collectionName");
  collection.insertOne({ ... }, { session });
  
  // 提交事务
  session.commitTransaction();
} catch (error) {
  // 中止事务
  session.abortTransaction();
} finally {
  // 结束会话
  session.endSession();
}
```
