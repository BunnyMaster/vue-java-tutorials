# MongoDB操作指南

数据库创建时，使用`use article`，之后使用`show dbs`不会显示，因为目前的数据库在内存中，没有数据是不会被持久化的。

## 2. 基础命令

```javascript
// 显示所有数据库
show dbs;

// 创建数据库或使用数据库
use article

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

**注意**：在使用`use xxx`可以表示为使用某个数据库或者创建某个数据库，但是如果这个数据库使用`use xxx`但没有插入数据，那么在使用`show dbs`时是不会显示的。

## 3. 创建(Create)操作

### 3.1 插入文档

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

### 3.2 示例

```javascript
// 用户数据示例
db.users.insertOne({
  name: "张三",
  age: 28,
  email: "zhangsan@example.com",
  city: "北京",
  interests: ["编程", "阅读", "旅行"],
  created_at: new Date()
})

// 产品数据示例
db.products.insertMany([
  { 
    name: "笔记本电脑", 
    price: 5999, 
    category: "电子产品", 
    stock: 50,
    specifications: {
      brand: "Dell",
      model: "XPS 13",
      cpu: "Intel i7",
      ram: "16GB"
    }
  },
  { 
    name: "智能手机", 
    price: 3999, 
    category: "电子产品", 
    stock: 100,
    specifications: {
      brand: "Apple",
      model: "iPhone 14",
      storage: "128GB",
      color: "黑色"
    }
  },
  { 
    name: "编程书籍", 
    price: 89, 
    category: "图书", 
    stock: 200,
    author: "Robert C. Martin",
    publisher: "人民邮电出版社",
    publish_date: new Date("2018-01-01")
  }
])

// 订单数据示例
db.orders.insertOne({
  order_id: "ORD001",
  user_id: ObjectId("507f1f77bcf86cd799439011"),
  products: [
    {
      product_id: ObjectId("607f1f77bcf86cd799439012"),
      quantity: 1,
      price: 5999
    }
  ],
  total_amount: 5999,
  status: "pending",
  created_at: new Date(),
  shipping_address: {
    recipient: "张三",
    phone: "13800138000",
    province: "北京市",
    city: "北京市",
    district: "海淀区",
    detail: "中关村大街1号"
  }
})
```

### 3.3 使用try-catch处理错误

```javascript
use users;
try {
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
    ]);
} catch(e) {
    print(e);
}

db.users.find()
```

## 4. 读取(Read)操作

### 4.1 基本查询

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

### 4.2 查询操作符

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

### 4.3 排序、限制和跳过

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

## 5. 更新(Update)操作

### 5.1 更新文档

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

### 5.2 更新操作符

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

### 5.3 更新选项

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

## 6. 删除(Delete)操作

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

## 7. 聚合操作

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

## 8. 索引管理

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

## 9. 实用命令

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

## 10. 事务操作（MongoDB 4.0+）

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

## 11. 数据建模最佳实践

### 11.1 文档结构设计

```javascript
// 用户文档示例
{
  _id: ObjectId("507f1f77bcf86cd799439011"),
  username: "john_doe",
  email: "john@example.com",
  profile: {
    firstName: "John",
    lastName: "Doe",
    age: 30,
    address: {
      street: "123 Main St",
      city: "New York",
      state: "NY",
      zipCode: "10001"
    }
  },
  preferences: {
    theme: "dark",
    notifications: {
      email: true,
      sms: false,
      push: true
    }
  },
  created_at: ISODate("2023-01-01T00:00:00Z"),
  updated_at: ISODate("2023-01-15T10:30:00Z")
}

// 产品目录文档示例
{
  _id: ObjectId("607f1f77bcf86cd799439012"),
  sku: "PROD001",
  name: "Wireless Headphones",
  category: "electronics",
  price: 99.99,
  specifications: {
    brand: "Sony",
    model: "WH-1000XM4",
    connectivity: ["Bluetooth", "NFC"],
    batteryLife: 30,
    weight: 254
  },
  inventory: {
    stock: 150,
    reserved: 25,
    available: 125
  },
  ratings: {
    average: 4.5,
    count: 234,
    reviews: [
      {
        userId: ObjectId("507f1f77bcf86cd799439011"),
        rating: 5,
        comment: "Excellent sound quality",
        date: ISODate("2023-01-10T14:30:00Z")
      }
    ]
  }
}
```

### 11.2 关系建模

```javascript
// 引用式关系（适合一对多）
// users集合
{
  _id: ObjectId("507f1f77bcf86cd799439011"),
  name: "Alice",
  email: "alice@example.com"
}

// orders集合  
{
  _id: ObjectId("607f1f77bcf86cd799439013"),
  userId: ObjectId("507f1f77bcf86cd799439011"),
  total: 199.99,
  items: [
    {
      productId: ObjectId("607f1f77bcf86cd799439012"),
      quantity: 2,
      price: 99.99
    }
  ]
}

// 嵌入式关系（适合一对一或一对少）
// 用户档案文档
{
  _id: ObjectId("507f1f77bcf86cd799439011"),
  username: "bob_smith",
  profile: {
    firstName: "Bob",
    lastName: "Smith",
    contact: {
      phone: "+1234567890",
      emergencyContact: {
        name: "Alice Smith",
        phone: "+0987654321",
        relationship: "spouse"
      }
    }
  }
}
```
