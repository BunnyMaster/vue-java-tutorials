# MongoDB CRUD 操作命令文档

## 简介

本文档提供了MongoDB中常用的CRUD（创建、读取、更新、删除）操作命令示例，以及相关的查询操作符和索引管理命令。

```sql
// 显示所有数据库
show dbs;

// 显示当前正在使用的数据库
db

// 删除数据库
db.dropDatabase()

// 集合显式创建
db.createCollection('article')

// 集合删除
db.article.drop()

// 显示当前集合
show collections
```

### 概览

```sql
// 创建文档
db.users.insertOne({
    "name": "李四",
    "age": 32,
    "email": "lisi@example.com",
    "city": "上海",
    "interests": ["音乐", "运动"],
    "created_at": new Date()
    })

// 插入多个
db.users.insertMany([
    {
        "name": "王五",
        "age": 25,
        "email": "wangwu@example.com",
        "city": "广州",
        "interests": ["美食", "电影"],
        "created_at": new Date()
        },
    {
        "name": "赵六",
        "age": 35,
        "email": "zhaoliu@example.com",
        "city": "深圳",
        "interests": ["摄影", "徒步"],
        "created_at": new Date()
        }
    ])

// 读取所有
db.users.find()

// 查询
// 查找年龄大于30的用户
db.users.find({ "age": { $gt: 30 } })

// 查找城市为北京的用户
db.users.find({ "city": "北京" })

// 查找兴趣包含"编程"的用户
db.users.find({ "interests": "编程" })
```

## 1. 创建(Create)操作

### 插入文档

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
```

### 示例
```javascript
// 插入用户文档
db.users.insertOne({
  name: "张三",
  age: 28,
  email: "zhangsan@example.com",
  city: "北京",
  interests: ["编程", "阅读", "旅行"],
  created_at: new Date()
})

// 插入多个产品文档
db.products.insertMany([
  { name: "笔记本电脑", price: 5999, category: "电子产品", stock: 50 },
  { name: "智能手机", price: 3999, category: "电子产品", stock: 100 },
  { name: "书籍", price: 59, category: "文化用品", stock: 200 }
])
```

## 2. 读取(Read)操作

### 基本查询

```javascript
// 查询所有文档
db.collection.find()

// 条件查询
db.collection.find({ field: value })

// 查询指定字段（投影）
db.collection.find({}, { field1: 1, field2: 1, _id: 0 })

// 查询一个文档
db.collection.findOne({ field: value })
```

### 查询操作符

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
db.collection.find({ $nor: [{age: {$lt: 18}}, {age: {$gt: 65}}] })
db.collection.find({ age: { $not: { $lt: 18 } } })

// 元素操作符
db.collection.find({ field: { $exists: true } })  // 字段存在
db.collection.find({ field: { $type: "string" } }) // 字段类型匹配

// 数组操作符
db.collection.find({ tags: { $all: ["tag1", "tag2"] } })  // 包含所有指定元素
db.collection.find({ tags: { $elemMatch: { $gt: 20, $lt: 30 } } }) // 数组元素匹配条件
db.collection.find({ "arrayField.size": "large" }) // 查询数组中的字段

// 求值操作符
db.collection.find({ $expr: { $gt: ["$price", "$cost"] } }) // 使用表达式
db.collection.find({ name: { $regex: /^张/ } }) // 正则表达式匹配
```

### 排序、限制和跳过

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

## 3. 更新(Update)操作

### 更新文档

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

### 更新操作符

```javascript
// 字段更新操作符
db.collection.updateOne({_id: 1}, { $set: { field: value } })  // 设置字段值
db.collection.updateOne({_id: 1}, { $unset: { field: "" } })   // 删除字段
db.collection.updateOne({_id: 1}, { $rename: { oldName: newName } }) // 重命名字段
db.collection.updateOne({_id: 1}, { $inc: { quantity: 5 } })   // 增加数值
db.collection.updateOne({_id: 1}, { $mul: { price: 1.1 } })    // 乘以数值
db.collection.updateOne({_id: 1}, { $min: { lowestPrice: 100 } }) // 设置最小值
db.collection.updateOne({_id: 1}, { $max: { highestPrice: 500 } }) // 设置最大值
db.collection.updateOne({_id: 1}, { $currentDate: { lastModified: true } }) // 设置当前日期

// 数组更新操作符
db.collection.updateOne({_id: 1}, { $push: { arrayField: value } }) // 添加元素到数组
db.collection.updateOne({_id: 1}, { $pop: { arrayField: 1 } })      // 移除数组第一个或最后一个元素
db.collection.updateOne({_id: 1}, { $pull: { arrayField: value } }) // 移除数组中匹配的元素
db.collection.updateOne({_id: 1}, { $addToSet: { arrayField: value } }) // 添加元素到数组(如不存在)
db.collection.updateOne({_id: 1}, { $pullAll: { arrayField: [value1, value2] } }) // 移除数组中多个元素
```

### 更新选项

```javascript
// 设置选项
db.collection.updateOne(
   { filter },
   { update },
   { 
     upsert: true,        // 如果不存在则插入
     writeConcern: { w: "majority", wtimeout: 5000 } // 写关注
   }
)
```

## 4. 删除(Delete)操作

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

## 5. 聚合操作

```javascript
// 基本聚合管道
db.collection.aggregate([
  { $match: { status: "A" } },          // 筛选文档
  { $group: { _id: "$cust_id", total: { $sum: "$amount" } } }, // 分组计算
  { $sort: { total: -1 } }              // 排序结果
])

// 常用聚合阶段
db.collection.aggregate([
  { $match: { ... } },      // 筛选文档
  { $group: { ... } },      // 分组
  { $sort: { ... } },       // 排序
  { $project: { ... } },    // 投影/重塑文档
  { $limit: n },            // 限制结果数量
  { $skip: n },             // 跳过文档
  { $unwind: "$arrayField" }, // 展开数组
  { $lookup: { ... } },     // 关联其他集合
  { $count: "fieldName" }   // 计数
])

// 常用聚合操作符
// 算术操作符: $add, $subtract, $multiply, $divide, $mod
// 比较操作符: $eq, $ne, $gt, $gte, $lt, $lte
// 条件操作符: $cond, $ifNull, $switch
// 日期操作符: $year, $month, $dayOfMonth, $hour, $minute, $second
// 字符串操作符: $concat, $substr, $toLower, $toUpper, $trim
// 数组操作符: $size, $slice, $map, $filter, $reduce
```

## 6. 索引管理

```javascript
// 创建索引
db.collection.createIndex({ field: 1 })              // 单字段索引
db.collection.createIndex({ field1: 1, field2: -1 }) // 复合索引
db.collection.createIndex({ field: "text" })         // 文本索引
db.collection.createIndex({ location: "2dsphere" })  // 地理空间索引

// 查看索引
db.collection.getIndexes()

// 删除索引
db.collection.dropIndex("indexName")
db.collection.dropIndex({ field: 1 })

// 索引选项
db.collection.createIndex(
  { field: 1 },
  {
    unique: true,           // 唯一索引
    sparse: true,           // 稀疏索引
    expireAfterSeconds: 3600, // TTL索引
    name: "indexName"       // 指定索引名称
  }
)
```

## 7. 实用命令

```javascript
// 统计文档数量
db.collection.countDocuments({ filter })

// 去重查询
db.collection.distinct("field", { filter })

// 执行批量写入
db.collection.bulkWrite([
  { insertOne: { document: { ... } } },
  { updateOne: { filter: { ... }, update: { ... } } },
  { deleteOne: { filter: { ... } } }
])

// 创建视图
db.createView("viewName", "sourceCollection", [ { $match: { ... } } ])
```

## 8. 事务操作（MongoDB 4.0+）

```javascript
// 开启会话
const session = db.getMongo().startSession();

// 开启事务
session.startTransaction({
  readConcern: { level: "snapshot" },
  writeConcern: { w: "majority" }
});

try {
  // 在事务中执行操作
  const collection = session.getDatabase("dbName").collection("collectionName");
  collection.insertOne({ ... }, { session });
  collection.updateOne({ ... }, { ... }, { session });
  
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
