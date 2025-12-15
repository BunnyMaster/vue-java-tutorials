# `Object.defineProperty` 方法

## 一、基本概念

`Object.defineProperty()` 是 JavaScript 中用于直接在一个对象上定义一个新属性，或者修改一个对象的现有属性的方法。它允许精确控制属性的行为特性。

## 属性描述符详解

### 1. 数据描述符（已注释部分）
```javascript
{
    value: 18,        // 属性值
    enumerable: true, // 是否可枚举（for...in或Object.keys()）
    writable: true,   // 是否可修改
    configurable: true // 是否可删除或修改特性
}
```

### 2. 存取描述符（实际使用部分）
```javascript
{
    get() {
        // 读取属性时调用
        return age;
    },
    set(value) {
        // 设置属性时调用
        age = value;
    }
}
```

## 特性说明

| 特性         | 类型 | 默认值    | 描述                     |
| ------------ | ---- | --------- | ------------------------ |
| configurable | 布尔 | false     | 是否可删除属性或修改特性 |
| enumerable   | 布尔 | false     | 是否出现在枚举属性中     |
| value        | 任意 | undefined | 属性值                   |
| writable     | 布尔 | false     | 是否可被赋值运算符改变   |
| get          | 函数 | undefined | 读取属性时调用的函数     |
| set          | 函数 | undefined | 设置属性时调用的函数     |

## 使用场景

1. **实现数据响应式**（如Vue2的核心实现）
2. **创建私有属性**（通过getter/setter控制访问）
3. **属性访问拦截**（在读取或设置时执行额外操作）
4. **定义不可枚举属性**（如内置对象的一些方法）

## 注意事项

1. 数据描述符（value, writable）和存取描述符（get, set）不能同时使用
2. 默认情况下，通过defineProperty添加的属性不可枚举、不可写、不可配置
3. 在严格模式下，setter必须设置一个参数，否则会抛出错误
4. getter不应有副作用（如修改其他属性值）

**实现简单响应式**

```javascript
function defineReactive(obj, key, val) {
    Object.defineProperty(obj, key, {
        get() {
            console.log(`读取 ${key}: ${val}`);
            return val;
        },
        set(newVal) {
            console.log(`设置 ${key}: ${newVal}`);
            val = newVal;
        }
    });
}

const data = {};
defineReactive(data, 'message', 'Hello');
```
